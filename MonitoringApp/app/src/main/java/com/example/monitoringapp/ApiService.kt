package com.example.monitoringapp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/api.php")
    fun getSuhu(@Query("data") data: String): Call<SuhuResponse>
}

