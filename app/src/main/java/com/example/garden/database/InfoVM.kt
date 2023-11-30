package com.example.garden

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garden.database.Garden
import com.example.garden.database.GardenRepository
import kotlinx.coroutines.launch

class InfoVM(private val GardenRepository: GardenRepository): ViewModel()  {
    var garden: MutableLiveData<Garden> = MutableLiveData()
    fun getById(id: Int){
        viewModelScope.launch {
            garden.value = GardenRepository.getById(id)
        }
    }
}