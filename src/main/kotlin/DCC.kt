private val stream = ProcessBuilder(listOf("/home/pi/go/bin/dccpi")).start().outputStream.bufferedWriter()

fun setLocoSpeed(loco: Int, value: Int) {
    stream.appendln("speed $loco $value")
}