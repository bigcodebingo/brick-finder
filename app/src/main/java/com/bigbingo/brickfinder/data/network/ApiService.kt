package com.bigbingo.brickfinder.data.network

import com.bigbingo.brickfinder.data.LegoPart
import com.bigbingo.brickfinder.data.PartCategory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("lego/part_categories/")
    suspend fun getPartCategories(
        @Header("Authorization") apiKey: String
    ): ApiResponse<PartCategory>

    @GET("lego/parts/")
    suspend fun getPartsByCategory(
        @Header("Authorization") apiKey: String,
        @Query("part_cat_id") categoryId: Int
    ): ApiResponse<LegoPart>
}

data class ApiResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)