object Websocket : AbstractVerticle() {

    @Throws(Exception::class)
    override fun start() {
        Vertx.vertx()
                .createHttpServer()
                .websocketHandler { ws -> ws.handler({
                    socket = ws
                    val s = it.toString("UTF-8")
                    when (s) {
                        "messac" -> MESSAC
                        "ploermel" -> PLOERMEL
                        "voie1" -> VOIE_1
                        "voie3" -> VOIE_3
                        "voie5" -> VOIE_5
                        "voie7" -> VOIE_7
                        "depannage" -> DEPANNAGE
                        else -> null
                    }?.onPress()
                    if (s == "request points") {
                        Track.values().forEach { track ->
                            Station.values().forEach { station ->
                                val point = station + track
                                ws.writeTextMessage("$point ${points[point]}")
                            }
                        }
                    }
                    if (s.startsWith("light_")) {
                        try {
                            val id = s.substring("light_".length).toInt()
                            lights[id].on = !lights[id].on
                        } catch (t: Throwable) {
                            t.printStackTrace()
                        }
                    }
                })}
                .requestHandler { req -> req.response().sendFile(if (req.uri() == "/") "index.html" else req.uri().substring(1)) }
                .listen(800)
    }

}