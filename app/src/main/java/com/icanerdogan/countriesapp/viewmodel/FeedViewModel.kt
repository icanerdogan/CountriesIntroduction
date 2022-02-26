package com.icanerdogan.countriesapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.icanerdogan.countriesapp.model.Country
import com.icanerdogan.countriesapp.service.CountryAPIService
import com.icanerdogan.countriesapp.service.CountryDatabase
import com.icanerdogan.countriesapp.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private val countryAPIService = CountryAPIService()

    //Disposible kullan at şeklinde hafızada yer tutmaya yarayan araçtır.
    private val disposable = CompositeDisposable()

    private var customPreferences = CustomSharedPreferences(getApplication())

    //Nano saniye cinsinden 10 Dakika
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()

    // Bu iki fonksiyon error varsa veya yükleniyor durumunda gösterileceği için Boolean olarak tanımlandı!
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData() {
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        }
        //10 dakikayı geçtiyse API'dan al!
        else {
            getDataFromAPI()
        }

    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        // Yükleniyor çubuğu gözüksün!
        countryLoading.value = true

        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(), "Countries From SQLite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromAPI() {
        countryLoading.value = true

        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    // Single değil Observable yapsaydık onSuccess ve onError çıkmaz!
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(), "Countries From API", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun showCountries(countryList: List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }

    private fun storeInSQLite(list: List<Country>) {
        // Database ViewModel içinde çağıracaz ve işlemleri Coroutines içinde ekliyoruz!
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()

            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())

            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i = i + 1
            }
            showCountries(list)
        }
        // Hangi zamanda oluşturulduğunu tutuyoruz!
        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}