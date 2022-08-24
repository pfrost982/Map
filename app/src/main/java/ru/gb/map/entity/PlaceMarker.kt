package ru.gb.map.entity

import com.yandex.mapkit.map.PlacemarkMapObject

data class PlaceMarker(
    val name: String,
    val description: String,
    val markMapObject: PlacemarkMapObject
)