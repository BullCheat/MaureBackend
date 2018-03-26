import com.pi4j.io.serial.Baud
import com.pi4j.io.serial.SerialConfig
import com.pi4j.io.serial.SerialDataEventListener
import com.pi4j.io.serial.SerialFactory

private val serial = SerialFactory.createInstance()

internal fun serialInit() {
    serial.addListener(SerialDataEventListener {
        val b = it.bytes
        var index = -1
        for (octet in b) {
            if (octet == 0xFF.toByte()) index = 0;
            else when (++index) {
                1 -> A
                2 -> B
                else -> null
            }?.speed = octet.toInt() and 0xFF
        }
    })

    serial.open(SerialConfig().apply {
        device("/dev/ttyAMA0")
        baud(Baud._57600)
    })
}

infix fun String.toLCD(index : Int) {
    serial.outputStream.write(byteArrayOf('T'.toByte(), index.toByte()) + toByteArray())
}
