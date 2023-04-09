import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.*

class VideoPerformanceTest : Simulation() {

    private val httpProtocol = http
        .baseUrl(Configuration.BASE_URL)
        .header("Content-Type", "application/json")
        .maxConnectionsPerHost(10)

    private var timeUntilTokenExpire = 0L
    private var isTokenExpired = true
    private var bearerToken = ""

    private val scenario = scenario("Video API Test")
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
        .exec(http("Create Video")
            .post("/videos")
            .header("Authorization", "#{bearerToken}")
            .body(RawFileBody("video/createWithoutFiles.json"))
            .check(status().`is`(201))
        )
        .exec(http("List Videos")
            .get("/videos")
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
