package com.example.poc.restservices

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MathService {
    @GET("?")
    fun myObjectById(@Query("expr") expression: String): Call<String>
}
