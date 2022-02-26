package com.icanerdogan.countriesapp.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.icanerdogan.countriesapp.model.Country

// DAO : Data Access Object
// Entity etiketli sınıfımızı (Country) Database ile bağlantısını sağlayan sınıfımızdır.

@Dao
interface CountryDAO {

    @Insert
    suspend fun insertAll(vararg  countries: Country) : List<Long>

    // Insert -> INSERT INTO
    // suspend -> coroutine, paste & resume
    // varatg -> sayısı belli olmayan çoklu veri tutmaya yarar.
    // List<Long> -> primary keys döner

    @Query("SELECT * FROM country")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM Country WHERE uuid = :countryId")
    suspend fun getCountry(countryId : Int) : Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()
}