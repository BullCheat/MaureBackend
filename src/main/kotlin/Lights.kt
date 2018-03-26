import com.pi4j.io.gpio.GpioPinDigitalOutput

val lights = Array(10, {Light(it, null)}) // TODO pin

class Light(private val id: Int, private val pin: GpioPinDigitalOutput?) {
    var on = false
    set(value) {
        field = value
        pin?.apply { if (value) high() else low() }
        socket.writeTextMessage("light $id $value")
    }
}
