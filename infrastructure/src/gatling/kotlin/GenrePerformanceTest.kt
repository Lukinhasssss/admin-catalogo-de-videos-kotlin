import io.gatling.javaapi.core.CoreDsl.RawFileBody
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.jsonPath
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class GenrePerformanceTest : Simulation() {

    private val httpProtocol = http
        .baseUrl(Configuration.BASE_URL)
        .header("Content-Type", "application/json")
        .maxConnectionsPerHost(10)

    private var timeUntilTokenExpire = 0L
    private var isTokenExpired = true
    private var bearerToken = ""

    private val scenario = scenario("Genre API Test")
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
            http("Create Genre")
                .post("/genres")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("genre/create.json"))
                .check(status().`is`(201))
                .check(jsonPath("$.id").saveAs("genreId"))
        )
        .exec(
            http("Get Genre By ID")
                .get("/genres/#{genreId}")
                .header("Authorization", "#{bearerToken}")
                .check(status().`is`(200))
        )
        .exec(
            http("Update Genre By ID")
                .put("/genres/#{genreId}")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("genre/update.json"))
                .check(status().`is`(200))
        )
        .exec(
            http("List Genres")
                .get("/genres")
                .header("Authorization", "#{bearerToken}")
                .check(status().`is`(200))
        )

    init {
        setUp(
            scenario.injectClosed(
                constantConcurrentUsers(100).during(360)
            ).protocols(httpProtocol)
        )
    }
}
