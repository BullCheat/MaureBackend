import Button.Companion.DEPANNAGE
import Button.Companion.MESSAC
import Button.Companion.PLOERMEL
import Button.Companion.VOIE_1
import Button.Companion.VOIE_3
import Button.Companion.VOIE_5
import Button.Companion.VOIE_7
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin.*
import com.pi4j.io.gpio.event.GpioPinListenerAnalog
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import com.pi4j.io.serial.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket

lateinit var socket: ServerWebSocket
object WebsocketsClient : AbstractVerticle() {

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

val controller: GpioController
    get() = GpioFactory.getInstance()

val aiguillage_outputs = mutableMapOf<Int, GpioPinDigitalOutput>()

fun main(args: Array<String>) {
    WebsocketsClient.start()
    mapOf(GPIO_21 to Button.MESSAC, GPIO_22 to PLOERMEL, GPIO_23 to DEPANNAGE, GPIO_07 to VOIE_1, GPIO_00 to VOIE_3, GPIO_02 to VOIE_5, GPIO_03 to VOIE_7).forEach { entry ->
        val pin = controller.provisionDigitalInputPin(entry.key, entry.value.toString())
        pin.addListener(GpioPinListenerDigital {
            if (it.state.isHigh)
                entry.value.onPress()
        })
    }

    serialInit()

    listOf(GPIO_04, GPIO_05, GPIO_06, null, GPIO_10, GPIO_11, GPIO_26, GPIO_27).forEachIndexed { i, pin ->
        if (pin != null)
            aiguillage_outputs[i] = controller.provisionDigitalOutputPin(pin, "Aiguillage_$i")
    }

}