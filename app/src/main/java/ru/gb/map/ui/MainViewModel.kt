package ru.gb.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.map.entity.PlaceMark

class MainViewModel : ViewModel() {

    private val placeMarksList: MutableList<PlaceMark> = mutableListOf()

    private val _liveDataPlaceMarkersList = MutableLiveData<List<PlaceMark>>()
    val liveDataPlaceMarkersList: LiveData<List<PlaceMark>> = _liveDataPlaceMarkersList

    private val _liveDataPlaceMarkForDelete = MutableLiveData<PlaceMark>()
    val liveDataPlaceMarkForDelete: LiveData<PlaceMark> = _liveDataPlaceMarkForDelete

    private val _liveDataPlaceMarkForUpdate = MutableLiveData<PlaceMark>()
    val liveDataPlaceMarkForUpdate: LiveData<PlaceMark> = _liveDataPlaceMarkForUpdate

    fun addPlaceMark(placeMark: PlaceMark) {
        placeMarksList.add(placeMark)
        _liveDataPlaceMarkersList.postValue(placeMarksList)
    }

    fun deletePlaceMark(placeMark: PlaceMark) {
        placeMarksList.remove(placeMark)
        _liveDataPlaceMarkForDelete.postValue(placeMark)
        _liveDataPlaceMarkersList.postValue(placeMarksList)
    }

    fun updatePlaceMark(position: Int, newName: String, newDescription: String) {
        val oldPlaceMark = placeMarksList.removeAt(position)
        val newPlaceMark = PlaceMark(newName, newDescription, oldPlaceMark.markMapObject)
        placeMarksList.add(position, newPlaceMark)
        _liveDataPlaceMarkForUpdate.postValue(newPlaceMark)
        _liveDataPlaceMarkersList.postValue(placeMarksList)
    }
}