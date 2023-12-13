package com.example.showflake.tmdb

import com.example.showflake.ApiConstants
import com.example.showflake.db.Movie
import com.example.showflake.db.MovieDao
import com.example.showflake.tmdb.api.TMDBApi
import com.example.showflake.tmdb.model.ProgramInfoList
import javax.inject.Inject

class TMDBUseCaseImpl @Inject constructor(
    private val tmdbApi: TMDBApi,
    private val movieDao: MovieDao
) : TMDBUseCase {
    override suspend fun getTrending(
        headers: HashMap<String, String>?,
        queryParams: HashMap<String, String>?
    ): ProgramInfoList {
        return tmdbApi.getPopularMovies(ApiConstants.API_KEY)
    }

    override suspend fun searchContent(searchString: String): ProgramInfoList {
        return tmdbApi.searchContent(searchString, ApiConstants.API_KEY)
    }

    override suspend fun getSimilarShows(tvId: Int): ProgramInfoList {
        return tmdbApi.getSimilarShows(tvId, ApiConstants.API_KEY)
    }

    override suspend fun markAsFavorite(movieId: Movie, isFavorite: Boolean) {
        movieDao.insertFavoriteMovie(movieId)
    }

    override suspend fun updateFavorite(movieId: Movie, isFavorite: Boolean) {
        movieDao.updateMovie(movieId)
    }

    override suspend fun getFavoriteMovies(): List<Movie> {
        return movieDao.getFavoriteMovies()
    }
}