package com.example.nationapp.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.nationapp.local.entity.LocalNation
import com.example.nationapp.local.LocalNationDao
import com.example.nationapp.remote.CountryService
import com.example.nationapp.remote.CountryServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalNationRepository(private val countryDao: LocalNationDao, private val context: Context) {
    suspend fun fetchCountries() {
        val countryService: CountryService = CountryServiceImpl(context)
        val countries = countryService.fetchCountries()

        withContext(Dispatchers.IO) {
            if (!countryService.isCacheEmpty()) {
                val localCountries = countries.map {
                    LocalNation(
                        nationalId = countries.indexOf(it) - 1,
                        common = it.name.common,
                        official = it.name.official,
                        png = it.flags.png,
                        svg = it.flags.svg,
                        alt = it.flags.alt
                    )
                }
                countryDao.insertAll(localCountries)
            }
        }
    }

    fun getCountries(): LiveData<List<LocalNation>> {
        return countryDao.getAllCountries()
    }

    suspend fun getCountriesByName(input: String): List<LocalNation> {
        return countryDao.getCountriesByName(input)
    }

    fun sortCountryByName(): List<LocalNation> {
        return countryDao.sortCountryByName()
    }
}