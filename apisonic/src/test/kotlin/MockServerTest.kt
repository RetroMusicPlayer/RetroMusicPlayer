import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import ru.stersh.apisonic.ApiSonic


class MockServerTest : WordSpec() {

    init {

        val server = MockWebServer().apply { start() }

        val baseUrl = server.url("/")

        val password = "aStrongPassword"
        val userName = "someUser"
        val apiVersion = "1.15.0"
        val clientId = "apisonictest"

        val mockServer = ApiSonic(
            baseUrl.toString(),
            userName,
            password,
            apiVersion,
            clientId
        )

        "A call to the api" should {

            server.enqueue(MockResponse().setBody(getJson("responses/getArtists.json")))
            runBlocking {
                mockServer.getArtists() //TODO ping
            }
            val request = server.takeRequest()

            "contain the password" {
                request.path shouldContain password
            }

            "contain the userName" {
                request.path shouldContain userName
            }

            "contain the apiVersion" {
                request.path shouldContain apiVersion
            }

            "contain the clientId" {
                request.path shouldContain clientId
            }
        }
    }
}