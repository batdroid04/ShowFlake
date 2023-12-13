package com.example.showflake.tmdb.api

import com.example.showflake.ApiConstants
import com.example.showflake.tmdb.model.ProgramInfoList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {
    @GET(ApiConstants.TRENDING)
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): ProgramInfoList

    @GET(ApiConstants.SEARCH)
    suspend fun searchContent(
        @Query("query") searchString: String,
        @Query("api_key") apiKey: String
    ): ProgramInfoList

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarShows(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): ProgramInfoList


}