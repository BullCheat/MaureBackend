/*
    when (msg.text()) {
        "messac" -> Button.MESSAC
        "ploermel" -> Button.PLOERMEL
        "voie1" -> Button.VOIE_1
        "voie3" -> Button.VOIE_3
        "voie5" -> Button.VOIE_5
        "voie7" -> Button.VOIE_7
        else -> null
    }*/

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket

lateinit var socket: ServerWebSocket
class WebsocketsClient : AbstractVerticle() {

    @Throws(Exception::class)
    override fun start() {
        Vertx.vertx()
                .createHttpServer()
                .websocketHandler { ws -> ws.handler({
                    socket = ws
                    val s = it.toString("UTF-8")
                    when (s) {
                        "messac" -> Button.MESSAC
                        "ploermel" -> Button.PLOERMEL
                        "voie1" -> Button.VOIE_1
                        "voie3" -> Button.VOIE_3
                        "voie5" -> Button.VOIE_5
                        "voie7" -> Button.VOIE_7
                        "depannage" -> Button.DEPANNAGE
                        else -> null
                    }?.onPress()
                    if (s == "request points") {
                        Track.values().forEach { track ->
                            Station.values().forEach { station ->
                                val point = station + track;
                                ws.writeTextMessage("$point ${points[point]}")
                            }
                        }
                    }
                })}
                .requestHandler { req -> req.response().sendFile(if (req.uri() == "/") "index.html" else req.uri().substring(1)) }
                .listen(80)
    }

}

fun main(args: Array<String>) {
    WebsocketsClient().start()
}