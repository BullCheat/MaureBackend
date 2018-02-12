import Button.Companion.MESSAC
import Button.Companion.PLOERMEL
import Button.Companion.VOIE_1
import Button.Companion.VOIE_3
import Button.Companion.VOIE_5
import Button.Companion.VOIE_7

val BUTTON_TIMEOUT = 5000 // ms

fun Int.getButton() = when(this) {
    1 -> MESSAC
    2 -> PLOERMEL
    3 -> VOIE_1
    4 -> VOIE_3
    5 -> VOIE_5
    6 -> VOIE_7
    else -> throw IllegalArgumentException("Invalid button pin")
}

open class Button() {
    companion object {
        val MESSAC = StationButton(Station.MESSAC)
        val PLOERMEL = StationButton(Station.PLOERMEL)
        val VOIE_1 = TrackButton(Track.VOIE_1)
        val VOIE_3 = TrackButton(Track.VOIE_3)
        val VOIE_5 = TrackButton(Track.VOIE_5)
        val VOIE_7 = TrackButton(Track.VOIE_7)
    }

    open fun onPress() {}
    open fun onRelease() {}
}

class StationButton(val gare: Station) : Button() {
    override fun onPress() {
        Routing.currentPressedStation = gare
    }
}

class TrackButton(val rail: Track) : Button() {
    override fun onRelease() {
        val cps = Routing.currentPressedStation // need to cache it because we're using a dynamic getter -> inconsistent
        if (cps != null) {
            Routing.traceRoute(cps, rail)
        }
    }
}