package ru.gb.map

import android.graphics.Point

data class PlaceMarker(
    val name: String,
    val description: String,
    val point: com.yandex.mapkit.geometry.Point
)