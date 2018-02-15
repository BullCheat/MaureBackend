class Routing {
    companion object {

        private var currentPressedStationTimestamp: Long? = null
        var currentPressedStation: Station? = null
            set(value) {
                currentPressedStationTimestamp = System.currentTimeMillis()
                field = value
            }
            get() = if (System.currentTimeMillis() - (currentPressedStationTimestamp?:0) <= BUTTON_TIMEOUT) field else null

        fun traceRoute(station: Station, track: Track) {
            var i = station.ordinal
            while (i >= 0) {
                @Suppress("UNUSED_CHANGED_VALUE") // WTF?! Bug Kotlin 1.2.21
                output(station + track, i-- == station.ordinal)

            }
        }

    }
}

enum class Track {
    VOIE_1,
    VOIE_3,
    VOIE_5,
    VOIE_7
}

enum class Station {
    MESSAC,
    PLOERMEL
}

operator fun Station.plus(track: Track): Int {
    return ordinal * Track.values().size + track.ordinal
}

fun output(pin: Int, value: Boolean) {
    TODO()
}