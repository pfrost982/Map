package ru.gb.map.ui.markers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MarkersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is markers Fragment"
    }
    val text: LiveData<String> = _text
}