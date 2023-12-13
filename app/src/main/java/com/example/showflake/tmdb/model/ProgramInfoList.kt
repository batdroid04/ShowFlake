package com.example.showflake.tmdb.model

import com.google.gson.annotations.SerializedName

data class ProgramInfoList(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<Program> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)