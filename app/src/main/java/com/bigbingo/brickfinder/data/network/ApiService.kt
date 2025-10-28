package com.bigbingo.brickfinder.data.network

import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartColor
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("lego/parts/{part_num}/")
    suspend fun getPartById(
        @Header("Authorization") apiKey: String,
        @retrofit2.http.Path("part_num") partNum: String
    ): Part

    @GET("lego/parts/{part_num}/colors/")
    suspend fun getPartColors(
        @Header("Authorization") apiKey: String,
        @retrofit2.http.Path("part_num") partNum: String
    ): ApiResponse<PartColor>
}

data class ApiResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)