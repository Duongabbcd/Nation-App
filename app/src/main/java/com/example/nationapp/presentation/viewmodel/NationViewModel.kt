package com.example.nationapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nationapp.local.AppDatabase
import com.example.nationapp.local.entity.LocalNation
import com.example.nationapp.local.repository.LocalNationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NationViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val localNationDao =
        AppDatabase.getDatabase(application.applicationContext).countryDao()
    private val localNationRepository =
        LocalNationRepository(localNationDao, application.applicationContext)

    private var _inputtedString = MutableLiveData<String>()

    private var _nations = MutableLiveData<List<LocalNation>>()
    var nations: LiveData<List<LocalNation>> = _nations

    lateinit var allNations: LiveData<List<LocalNation>>

    private var _selectedNation = MutableLiveData<LocalNation>()
    val selectedNation: LiveData<LocalNation> = _selectedNation

    private var _isSortByName = MutableLiveData<Boolean>()
    val isSortByName: LiveData<Boolean> = _isSortByName

    fun getCountries() {
        runBlocking(Dispatchers.IO) {
            allNations = localNationRepository.getCountries()
        }
    }

    fun fetchCountries() = viewModelScope.launch(Dispatchers.IO) {
        localNationRepository.fetchCountries()
    }

    fun searchingNation(inputtedStr: String) {
        _inputtedString.value = inputtedStr
        println("inputtedStr: ${_inputtedString.value} and $inputtedStr")
        _inputtedString.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val result = localNationRepository.getCountriesByName(it).also {
                    println("Result: ${it.size}")
                }
                _nations.postValue(result)
            }
        }
    }

    fun sortCountryByName() {
        runBlocking(Dispatchers.IO) {
            _nations.postValue(localNationRepository.sortCountryByName())
        }
    }

    fun setCurrentFragment(localNation: LocalNation) {
        _selectedNation.postValue(localNation)
    }

    fun sortByName(sortByName: Boolean) {
        if (sortByName) {
            sortCountryByName()
        } else _nations.postValue(allNations.value)
    }
}