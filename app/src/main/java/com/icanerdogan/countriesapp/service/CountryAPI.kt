package com.icanerdogan.countriesapp.service

import com.icanerdogan.countriesapp.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface CountryAPI {

    // Single: Uygulama sürekli veri güncellenmediği için 10 dakika istersen 5 saat sonra çekmek için kullanılır!
    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries():Single<List<Country>>


}