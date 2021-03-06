
class Routing {
    companion object {

        private var currentPressedStationTimestamp: Long? = null
        var currentPressedStation: Any? = null
            set(value) {
                currentPressedStationTimestamp = System.currentTimeMillis()
                field = value
            }
            get() = if (System.currentTimeMillis() - (currentPressedStationTimestamp?:0) <= BUTTON_TIMEOUT) field else null

        fun traceRoute(station: Station, track: Track) {
            var i = track.ordinal
            while (i >= 0) {
                setPoint(station + Track.values()[i], i-- == track.ordinal)
            }
            val size = lights.size/2
            (if (station == Station.MESSAC) lights.take(size) else lights.takeLast(size)).forEach { it.on = false }
            // Notify LCD
            "$station -> $track" toLCD 40 + 20*station.ordinal
        }

    }
}

val points = BooleanArray(8)

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


fun setPoint(index: Int, value: Boolean) {
    points[index] = value
    socket.writeTextMessage("point $index $value")
    value.toPin((if (index > 3) index - 1 else index) + 22)
}