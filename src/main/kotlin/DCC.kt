private val stream = ProcessBuilder(listOf("/home/pi/go/bin/dccpi")).start().outputStream.bufferedWriter()

fun Int.setLocoSpeed(value: Int) {
    stream.appendln("speed $this $value")
}

val locoMap = arrayOf(0, 1)