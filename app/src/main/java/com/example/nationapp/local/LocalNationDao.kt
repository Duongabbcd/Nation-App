package com.example.nationapp.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nationapp.local.entity.LocalNation

@Dao
interface LocalNationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<LocalNation>)

    @Query("SELECT * FROM ${LocalNation.NATION_INFO_TABLE}")
     fun getAllCountries(): LiveData<List<LocalNation>>

    @Query("SELECT * FROM ${LocalNation.NATION_INFO_TABLE} WHERE `Nation Common Name` LIKE '%' || :input || '%'")
    suspend fun getCountriesByName(input: String): List<LocalNation>

    @Query("SELECT * FROM ${LocalNation.NATION_INFO_TABLE} ORDER BY `Nation Common Name` ASC ")
    fun sortCountryByName(): List<LocalNation>
}