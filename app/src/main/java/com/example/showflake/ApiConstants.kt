package com.example.showflake

object ApiConstants {
    const val BASE_URL: String = "https://api.themoviedb.org/3/"
    const val TRENDING: String = "trending/all/day"
    const val SEARCH: String = "/3/search/multi"

    // TODO - Move this to [local.properties]
    const val API_KEY: String = "0ba0f15dab03995d9c2a2aa587bb22be"

    fun getPosterPath(posterDetail: String) = "https://image.tmdb.org/t/p/w500/$posterDetail"
}