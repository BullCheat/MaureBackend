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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

enum class Point(val trace: (Station, Track) -> Boolean?) { // Aiguillage
    P_1,
    P_3,
    P_5,
    M_1,
    M_3,
    M_5,
    M_7
}