import io.vertx.core.http.ServerWebSocket

lateinit var socket: ServerWebSocket

const val PORT = 80

fun main(args: Array<String>) {
    Websocket.start()
    serialInit()
}