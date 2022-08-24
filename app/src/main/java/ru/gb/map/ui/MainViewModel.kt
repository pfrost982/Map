package ru.gb.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.map.entity.PlaceMarker

class MainViewModel : ViewModel() {

    private val placeMarkerList: MutableList<PlaceMarker> = mutableListOf()
    private val _liveDataPlaceMarkersList = MutableLiveData<List<PlaceMarker>>()
    val liveDataPlaceMarkersList: LiveData<List<PlaceMarker>> = _liveDataPlaceMarkersList

    private val _liveDataPlaceMarkerForDelete = MutableLiveData<PlaceMarker>()
    val liveDataPlaceMarkerForDelete: LiveData<PlaceMarker> = _liveDataPlaceMarkerForDelete

    fun addPlaceMarker(marker: PlaceMarker) {
        placeMarkerList.add(marker)
        _liveDataPlaceMarkersList.postValue(placeMarkerList)
    }

    fun deletePlaceMarker(marker: PlaceMarker){
        placeMarkerList.remove(marker)
        _liveDataPlaceMarkerForDelete.postValue(marker)
    }
}