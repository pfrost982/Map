package ru.gb.map.entity

import com.yandex.mapkit.map.PlacemarkMapObject

data class PlaceMark(
    val name: String,
    val description: String,
    val markMapObject: PlacemarkMapObject
)