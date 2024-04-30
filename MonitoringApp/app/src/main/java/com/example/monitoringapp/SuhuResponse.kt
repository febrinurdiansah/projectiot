package com.example.monitoringapp

data class SuhuData(
    val id: String,
    val temp: String,
    val hum: String,
    val updated_at: String
)

data class SuhuResponse(
    val data: List<SuhuData>
)


