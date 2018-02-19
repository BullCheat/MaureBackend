const val BUTTON_TIMEOUT = 2000 // ms

open class Button {
    companion object {
        val MESSAC = RoutingButton(Station.MESSAC)
        val PLOERMEL = RoutingButton(Station.PLOERMEL)
        val VOIE_1 = RoutingButton(Track.VOIE_1)
        val VOIE_3 = RoutingButton(Track.VOIE_3)
        val VOIE_5 = RoutingButton(Track.VOIE_5)
        val VOIE_7 = RoutingButton(Track.VOIE_7)
        val DEPANNAGE = DepannageButton(Station.PLOERMEL + Track.VOIE_7, false)
    }

    open fun onPress() {}
}

class DepannageButton(private val pointIndex: Int, private val enabledValue: Boolean) : Button() {
    override fun onPress() {
        setPoint(pointIndex, enabledValue)
        Routing.currentPressedStation = null
    }
}

class RoutingButton(private val element: Any) : Button() {
    override fun onPress() {
        val cps = Routing.currentPressedStation
        if (cps == null || cps::class == element::class) Routing.currentPressedStation = element
        else {
            if (cps is Station && element is Track) Routing.traceRoute(cps, element)
            else if (cps is Track && element is Station) Routing.traceRoute(element, cps)
            else println("Error, cps !is track or station $cps")
        }
    }
}