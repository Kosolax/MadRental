package com.formationandroid.madrental.ws

import com.formationandroid.madrental.ws.ReturnWSVehicles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET

interface WSInterface {
    @GET("exchange/madrental/get-vehicules.php")
    fun getVehicles(): Call<MutableList<ReturnWSVehicle>>
}
