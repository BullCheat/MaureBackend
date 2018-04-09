
val lights = Array(8, {Light(it, it + 29)}) // TODO pin

class Light(private val id: Int, private val pin: Int) {
    var on = false
    set(value) {
        field = value
        value.toPin(pin)
        socket.writeTextMessage("light $id $value")
    }
}
