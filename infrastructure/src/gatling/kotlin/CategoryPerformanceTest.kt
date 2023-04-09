import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.*

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
        .pause(2)
        .exec(http("Create Category")
            .post("/categories")
            .header("Authorization", "#{bearerToken}")
            .body(RawFileBody("category/create.json"))
            .check(status().`is`(201))
            .check(jsonPath("$.id").saveAs("categoryId"))
        )
        .exec(http("Get Category By ID")
            .get("/categories/#{categoryId}")
            .header("Authorization", "#{bearerToken}")
            .check(status().`is`(200))
        )
        .exec(http("Update Category By ID")
            .put("/categories/#{categoryId}")
            .header("Authorization", "#{bearerToken}")
            .body(RawFileBody("category/update.json"))
            .check(status().`is`(200))
        )
        .exec(http("List Categories")
            .get("/categories")
            .header("Authorization", "#{bearerToken}")
            .check(status().`is`(200))
        )

    init {
        setUp(
            scenario.injectClosed(
                constantConcurrentUsers(100).during(360),
                // rampConcurrentUsers(10).to(20).during(10)
            ).protocols(httpProtocol)
        )

        // setUp(
        //     scenario.injectOpen(
        //         // nothingFor(4), // 1
        //         // atOnceUsers(10), // 2
        //         rampUsers(1).during(5), // 3
        //         // constantUsersPerSec(20.0).during(15), // 4
        //         // constantUsersPerSec(20.0).during(15).randomized(), // 5
        //         // rampUsersPerSec(10.0).to(20.0).during(10), // 6
        //         // rampUsersPerSec(10.0).to(20.0).during(10).randomized(), // 7
        //         // stressPeakUsers(1000).during(20) // 8
        //     ).protocols(httpProtocol)
        // )
    }
}

/**
 * The building blocks for open model profile injection are:
 *
 * nothingFor(duration): Pause for a given duration.
 * atOnceUsers(nbUsers): Injects a given number of users at once.
 * rampUsers(nbUsers).during(duration): Injects a given number of users distributed evenly on a time window of a given duration.
 * constantUsersPerSec(rate).during(duration): Injects users at a constant rate, defined in users per second, during a given duration. Users will be injected at regular intervals.
 * constantUsersPerSec(rate).during(duration).randomized: Injects users at a constant rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
 * rampUsersPerSec(rate1).to.(rate2).during(duration): Injects users from starting rate to target rate, defined in users per second, during a given duration. Users will be injected at regular intervals.
 * rampUsersPerSec(rate1).to(rate2).during(duration).randomized: Injects users from starting rate to target rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
 * stressPeakUsers(nbUsers).during(duration): Injects a given number of users following a smooth approximation of the heaviside step function stretched to a given duration.
 */
