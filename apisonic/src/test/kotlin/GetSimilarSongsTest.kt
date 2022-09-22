import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import ru.stersh.apisonic.ApiSonic


class GetSimilarSongsTest : WordSpec() {

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

        server.enqueue(MockResponse().setBody(getJson("responses/similarSongs.json")))

        val similarSongs = runBlocking { mockServer.getSimilarSongs("5", 10) }

        "A request for 10 similarSongs for the song id '5'" should {
            val request = server.takeRequest()

            "be made against the path '/getSimilarSongs'" {
                request.path shouldContain "/getSimilarSongs"
            }

            "contain the id '5'" {
                request.path shouldContain "id=5"
            }

            "contain the count 10" {
                request.path shouldContain "count=10"
            }
        }

        "The similarSongs object from the mock web server for song id '5'" should {

            "contain a list of similar songs containing a song with id '72'" {
                similarSongs.find { it.id == "72" } shouldNotBe null
            }
        }
    }
}