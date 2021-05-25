package com.formationandroid.madrental.ws

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingleton {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://s519716619.onlinehome.fr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}