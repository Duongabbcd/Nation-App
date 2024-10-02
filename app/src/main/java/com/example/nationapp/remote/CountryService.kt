package com.example.nationapp.remote

import com.example.nationapp.remote.model.Country

interface CountryService {
    suspend fun fetchCountries(): List<Country>
    suspend fun isCacheEmpty(): Boolean
}