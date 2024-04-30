package com.example.monitoringapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private val refreshIntervalMillis: Long = 500 // Refresh setiap 0.5 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //inisialisasi web yang ingin diambil datanya
        val retrofit = Retrofit.Builder()
            .baseUrl("localhost")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        getDataSuhu()
        getDataSuhuPeriodically()
    }
    // fungsi refresh
    private fun getDataSuhuPeriodically() {
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                getDataSuhu()
                handler.postDelayed(this, refreshIntervalMillis)
            }
        }
        handler.postDelayed(runnable, 0)
    }

    // get data dari API
    private fun getDataSuhu() {
        apiService.getSuhu("realtime").enqueue(object : Callback<SuhuResponse> {
            override fun onResponse(call: Call<SuhuResponse>, response: Response<SuhuResponse>) {
                if (response.isSuccessful) {
                    val suhuResponse = response.body()
                    suhuResponse?.let {
                        val suhuData = it.data.firstOrNull()
                        suhuData?.let { data ->
                            val temp = if (data.temp.length >= 2) data.temp.substring(0, 2) else data.temp
                            val hum = if (data.hum.length >= 3) data.hum.substring(0, 3) else data.hum
                            var txtSuhu = findViewById<TextView>(R.id.txtSuhu)
                            var txtHumidity = findViewById<TextView>(R.id.txtHumidity)
                            txtSuhu.text = "$temp °C"
                            txtHumidity.text = "$hum %"
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SuhuResponse>, t: Throwable) {
                // Handle error
            }
        })
    }
}
