import java.io.File

fun getJson(path: String): String {
    val uri = Thread.currentThread().contextClassLoader.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}