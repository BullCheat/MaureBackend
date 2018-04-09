import com.google.common.io.ByteStreams
import com.pi4j.io.serial.Baud
import com.pi4j.io.serial.SerialConfig
import com.pi4j.io.serial.SerialDataEventListener
import com.pi4j.io.serial.SerialFactory
import java.io.EOFException

private val serial = SerialFactory.createInstance()

internal fun serialInit() {
    serial.addListener(SerialDataEventListener {
        val reader = ByteStreams.newDataInput(it.bytes)
        try {
            while (true) {
                while (reader.readUnsignedByte() != 0);
                val c = reader.readUnsignedByte().toChar()
                if (c == 'T') {
                    val channel = reader.readUnsignedByte()
                    val value = reader.readUnsignedByte()
                    setLocoSpeed(channel, value)
                } else if (c == 'B') {
                    buttons[reader.readUnsignedByte()].onPress()
                }
            }

        } catch (ignored: EOFException) {}
    })

    serial.open(SerialConfig().apply {
        device("/dev/ttyAMA0")
        baud(Baud._57600)
    })
}

infix fun String.toLCD(index : Int) {
    serial.outputStream.write(byteArrayOf(0, 'T'.toByte(), index.toByte()) + toByteArray())
}

infix fun Boolean.toPin(index: Int) {
    serial.outputStream.write(byteArrayOf(0, 'O'.toByte(), ((index shl 1) or if (this) 1 else 0).toByte()))
}