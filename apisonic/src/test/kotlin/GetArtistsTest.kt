import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import ru.stersh.apisonic.ApiSonic

class GetArtistsTest : WordSpec() {

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

        "The artists object from the mock web server" should {

            server.enqueue(MockResponse().setBody(getJson("responses/getArtists.json")))

            val artists = runBlocking { mockServer.getArtists() }

            "have ignored articles" {
                artists.ignoredArticles shouldBe "The El La Los Las Le"
            }

            "contain 'Los' as an ignored article" {
                artists.ignoredArticlesList() shouldContain "Los"
            }

            "have 10 indices" {
                artists.indices.size shouldBe 10
            }

            "contain the letter U" {
                artists.indices.firstOrNull { it.name == "U" } shouldNotBe null
            }

            "not contain the letter X" {
                artists.indices.firstOrNull { it.name == "X" } shouldBe null
            }

            "return the artists as a simple list containing the same amount of artists" {
                var count = 0
                artists.indices.forEach {
                    count += it.artists.size
                }
                artists.asArtistList().size shouldBe count
            }

            "return the artists as a simple list containing 'Silence is Sexy'" {
                artists.asArtistList().firstOrNull { it.name == "Silence is Sexy" } shouldNotBe null
            }

            "return a map with all indices" {
                artists.indices.size shouldBe artists.asIndexedMap().size
            }

            "return a map with same amount of artists" {
                var count = 0
                artists.indices.forEach {
                    count += it.artists.size
                }
                artists.asIndexedMap().values.flatten().size shouldBe count
            }

            "has the artist 'METISSE'" {
                val metisse = artists.indices
                    .firstOrNull { it.name == "M" }
                    ?.artists
                    ?.firstOrNull { it.name == "METISSE" }
                metisse shouldNotBe null
                metisse?.id shouldBe "6"
                metisse?.coverArt shouldBe "ar-6"
                metisse?.artistImageUrl shouldBe "https://lastfm-img2.akamaized.net/i/u/10f02d8fc33b4a5688f564b8137828bf.png"
                metisse?.albumCount shouldBe 1
            }
        }
    }
}