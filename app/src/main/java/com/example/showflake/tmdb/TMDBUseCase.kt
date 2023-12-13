package com.example.showflake.tmdb

import com.example.showflake.db.Movie
import com.example.showflake.tmdb.model.ProgramInfoList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TMDBUseCase {
    /**
     * Used to fetch latest trending shows of the day
     */
    suspend fun getTrending(
        @HeaderMap headers: HashMap<String, String>?,
        @QueryMap queryParams: HashMap<String, String>?
    ): ProgramInfoList

    //Data source - Network
    /**
     *
     * Used to fetch contents based on user input string
     *
     * @param searchString - String provided by user in search bar
     */
    suspend fun searchContent(@Body searchString: String): ProgramInfoList

    /**
     *
     * Used to get similar contents based on opened content
     *
     * @param tvId - Content ID of Program of which similar content are to be fetched
     */
    suspend fun getSimilarShows(tvId: Int): ProgramInfoList

    // Data source - Local
    /**
     *
     * Used to mark a content as favorite
     */
    suspend fun markAsFavorite(movieId: Movie, isFavorite: Boolean)

    /**
     *
     * Used to update already existing content
     */
    suspend fun updateFavorite(movieId: Movie, isFavorite: Boolean)

    /**
     *
     * Used to fetch list of all favorite contents
     */
    suspend fun getFavoriteMovies(): List<Movie>
}