package com.bigbingo.brickfinder.data.network

import com.bigbingo.brickfinder.data.PartCategory
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("lego/part_categories/")
    suspend fun getPartCategories(
        @Header("Authorization") apiKey: String
    ): ApiResponse<PartCategory>
}

data class ApiResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)