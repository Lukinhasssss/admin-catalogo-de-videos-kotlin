import io.gatling.javaapi.core.CoreDsl.RawFileBody
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class AdminDoCatalogoPerformanceTest : Simulation() {

    private val httpProtocol = http
        .baseUrl(Configuration.BASE_URL)
        .header("Content-Type", "application/json")
        .maxConnectionsPerHost(10)

    private var timeUntilTokenExpire = 0L
    private var isTokenExpired = true
    private var bearerToken = ""

    private val scenario = scenario("Admin do Catálogo API Test")
        .exec { session ->
            isTokenExpired = timeUntilTokenExpire <= System.currentTimeMillis()

            if (isTokenExpired) {
                timeUntilTokenExpire = System.currentTimeMillis() + (5 * 60 * 1000)
                return@exec session
            }

            return@exec session.set("bearerToken", bearerToken)
        }
        .doIf { _ -> isTokenExpired }.then(
            Authentication.setToken().exec { session ->
                bearerToken = "Bearer ${session.getString("token")!!}"
                return@exec session.set("bearerToken", bearerToken)
            }.pause(1)
        )
        .pause(2)
        .exec(
            http("Create Category")
                .post("/categories")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("category/create.json"))
                .check(status().`is`(201))
        )
        .exec(
            http("Create Genre")
                .post("/genres")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("genre/create.json"))
                .check(status().`is`(201))
        )
        .exec(
            http("Create Cast Member")
                .post("/cast_members")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("castMember/create.json"))
                .check(status().`is`(201))
        )
        .exec(
            http("Create Video")
                .post("/videos")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("video/createWithoutFiles.json"))
                .check(status().`is`(201))
        )

    init {
        setUp(
            scenario.injectClosed(
                constantConcurrentUsers(50).during(300)
            ).protocols(httpProtocol)
        )
    }
}
