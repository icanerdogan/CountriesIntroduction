package com.icanerdogan.countriesapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.icanerdogan.countriesapp.model.Country

// entities: arrayOf içidne yazılır ve istenilen entity sınıfı eklenebilir. Ve her değişiklikte version artırılır
@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase : RoomDatabase(){

    abstract fun countryDao() : CountryDAO

    // Diğer Scopelar içinden ulaşmak için bu şekilde yapıyoruz!
    companion object {
        // Farklı threadlarda kullanılacağı için Volatile kulllanıyoruz!
        @Volatile private var instance : CountryDatabase? = null

        private val lock = Any()
        // Birden fazla thread bunu oluşturmaya çalıştırdığında aynı anda 2 obje bunu oluşturmaya çalışamıyor!
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        // Database oluşturulan fonksiyon
        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, CountryDatabase::class.java, "countrydatabase"
        ).build()
    }
}