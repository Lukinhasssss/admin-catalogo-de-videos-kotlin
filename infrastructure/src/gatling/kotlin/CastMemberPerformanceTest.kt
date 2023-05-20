import io.gatling.javaapi.core.CoreDsl.RawFileBody
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.jsonPath
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class CastMemberPerformanceTest : Simulation() {

    private val httpProtocol = http
        .baseUrl(Configuration.BASE_URL)
        .header("Content-Type", "application/json")
        .maxConnectionsPerHost(10)

    private var timeUntilTokenExpire = 0L
    private var isTokenExpired = true
    private var bearerToken = ""

    private val scenario = scenario("Cast Member API Test")
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
            http("Create Cast Member")
                .post("/cast_members")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("castMember/create.json"))
                .check(status().`is`(201))
                .check(jsonPath("$.id").saveAs("castMemberId"))
        )
        .exec(
            http("Get Cast Member By ID")
                .get("/cast_members/#{castMemberId}")
                .header("Authorization", "#{bearerToken}")
                .check(status().`is`(200))
        )
        .exec(
            http("Update Cast Member By ID")
                .put("/cast_members/#{castMemberId}")
                .header("Authorization", "#{bearerToken}")
                .body(RawFileBody("castMember/update.json"))
                .check(status().`is`(200))
        )
        .exec(
            http("List Cast Members")
                .get("/cast_members")
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
