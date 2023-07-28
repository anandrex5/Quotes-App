package com.company0ne.quotesapplication.Api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {

    @GET("quotes")
    fun getData() : Call<JsonArray>
}