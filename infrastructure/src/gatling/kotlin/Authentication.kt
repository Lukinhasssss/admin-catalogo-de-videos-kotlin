import io.gatling.javaapi.core.ChainBuilder
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.jsonPath
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

object Authentication {
    fun setToken(): ChainBuilder {
        val formParams = mapOf(
            "grant_type" to Configuration.GRANT_TYPE,
            "client_id" to Configuration.CLIENT_ID,
            "client_secret" to Configuration.CLIENT_SECRET,
            "username" to Configuration.USERNAME,
            "password" to Configuration.PASSWORD
        )

        return exec(
            http("Authentication")
                .post(Configuration.TOKEN_URL)
                .formParamMap(formParams)
                .asFormUrlEncoded()
                .check(status().`is`(200))
                .check(jsonPath("$.access_token").saveAs("token"))
        )
    }
}
