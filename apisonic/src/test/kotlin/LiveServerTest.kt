import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import ru.stersh.apisonic.ApiSonic

class LiveServerTest : WordSpec() {

    init {

        "A connection to the demo subsonic demo server" should {

            val demoServer = ApiSonic(
                "http://demo.subsonic.org/rest/",
                "guest2",
                "guest",
                "1.15.0",
                "apisonic"
            )

            "be possible to ping with an 'ok' status" {
                val ping = runBlocking {
                    demoServer.ping()
                }
                ping.status shouldBe "ok"
            }

            "return a valid licence" {
                val license = runBlocking {
                    demoServer.getLicense()
                }
                license.valid shouldBe true
            }
        }
    }
}