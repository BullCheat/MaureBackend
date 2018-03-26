private val stream = ProcessBuilder(listOf("/home/pi/go/bin/dccpi")).start().outputStream.bufferedWriter()

sealed class Locomotive(val name: String) {
    var speed: Int = 0
    set(value) {
        stream.appendln("speed $name $value")
        field = value
    }
}

object A : Locomotive("a")
object B : Locomotive("b")
