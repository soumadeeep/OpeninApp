package com.example.openinapp.getretrofit


import com.example.openinapp.dataclass.MainData
import retrofit2.http.GET
import retrofit2.http.Header

interface GetApiCall {
 @GET("api/v1/dashboardNew")
 fun getDetails(@Header("Authorization") authToken:String): retrofit2.Call<MainData>


}