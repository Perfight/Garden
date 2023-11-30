package com.example.garden.database

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainVM(private val GardenRepository: GardenRepository): ViewModel() {
    //инициализация
    var temp_list1: MutableLiveData<List<String>> = MutableLiveData()
    var gardlist: MutableLiveData<List<Garden>> = MutableLiveData()

    //получение списка объедененных одним симптомом
    fun getGardenList(listName: String){
        viewModelScope.launch { //чтобы не засорять основной код корутинами запускаем здесь
            gardlist.value = GardenRepository.getGardenList(listName)
        }
    }
    //получение списка для бокового меню
    fun getLists(){
        viewModelScope.launch {
            temp_list1.value = GardenRepository.getLists()
        }
    }

    //добавляем без ид
    fun insertGarden(name : String, RecommendedDrugs : String, GardenProblems : String){
        viewModelScope.launch {
            GardenRepository.insertGarden(name, RecommendedDrugs, GardenProblems)
        }
    }

    //удаляем элемент
    fun deleteGarden(garden: Int){
        viewModelScope.launch {
            if(temp_list1.value!!.size == 1){ //решение проблемы с удалением первого элемента
                val id = temp_list1.value!!.indexOf(GardenRepository.getById(garden).GardenProblems)
                temp_list1.value!!.drop(id)
            }
            gardlist.value!!.drop(garden)
            GardenRepository.deleteGarden(garden)

        }
    }
}