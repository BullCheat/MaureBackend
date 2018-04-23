import io.vertx.core.http.ServerWebSocket

lateinit var socket: ServerWebSocket


fun main(args: Array<String>) {
    Websocket.start()
    serialInit()
}