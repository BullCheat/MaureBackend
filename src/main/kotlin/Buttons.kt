const val BUTTON_TIMEOUT = 2000 // ms

val buttons = listOf(MESSAC, PLOERMEL, VOIE_1, VOIE_3, VOIE_5, VOIE_7, DEPANNAGE)

sealed class Button {
    open fun onPress() {}
}

object MESSAC : RoutingButton(Station.MESSAC)
object PLOERMEL : RoutingButton(Station.PLOERMEL)
object VOIE_1 : RoutingButton(Track.VOIE_1)
object VOIE_3 : RoutingButton(Track.VOIE_3)
object VOIE_5 : RoutingButton(Track.VOIE_5)
object VOIE_7 : RoutingButton(Track.VOIE_7)
object DEPANNAGE : Button() {
    private val pointIndex: Int = Station.PLOERMEL + Track.VOIE_7
    private val isEnabled = false
    override fun onPress() {
        setPoint(pointIndex, isEnabled)
        Routing.currentPressedStation = null
    }
}

abstract class RoutingButton(private val element: Any) : Button() {
    override fun onPress() {
        val cps = Routing.currentPressedStation
        Routing.currentPressedStation = if (element is Track || cps == null) element else null
        if (cps != null && cps::class != element::class) {
            if (cps is Station && element is Track) Routing.traceRoute(cps, element)
            else if (cps is Track && element is Station) Routing.traceRoute(element, cps)
            else println("Error, cps !is track or station $cps")
        }
    }
}