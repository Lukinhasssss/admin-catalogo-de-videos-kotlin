import io.gatling.javaapi.core.CoreDsl.RawFileBody
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http

class CategoryPerformanceTest : Simulation() {

    private val httpProtocol = http
        .baseUrl(Configuration.BASE_URL)
        .header("Content-Type", "application/json")
        .maxConnectionsPerHost(10)

    private var timeUntilTokenExpire = 0L
    private var isTokenExpired = true
    private var bearerToken = ""

    private val scenario = scenario("Category API Test")
        .exec { session ->
            isTokenExpired =
                timeUntilTokenExpire <= System.currentTimeMillis() // Armazena a validade do token por 5 minutos

            // println("${(timeUntilTokenExpire)} é menor ou igual a ${System.currentTimeMillis()} ? $isTokenExpired")

            if (isTokenExpired) {
                // println("O token esta expirado")
                timeUntilTokenExpire = System.currentTimeMillis() + (5 * 60 * 1000)
                // println("Resetando o token, agora ele expira em: $timeUntilTokenExpire")
                return@exec session
            }

            // println("O token esta valido")
            return@exec session.set("bearerToken", bearerToken)
        }
        .doIf { _ -> isTokenExpired }.then(
            Authentication.setToken().exec { session ->
                bearerToken = "Bearer ${session.getString("token")!!}"
                return@exec session.set("bearerToken", bearerToken)
            }
        )
        .exec(http("Category Creation")
            .post("/categories")
            .header("Authorization", "#{bearerToken}")
            .body(RawFileBody("category/validCategory.json"))
        )

    init {
        setUp(
            scenario.injectClosed(
                constantConcurrentUsers(100)
                    .during(360)
            ).protocols(httpProtocol)
        )
    }
}
