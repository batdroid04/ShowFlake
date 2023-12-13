package com.example.showflake.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showflake.db.Movie
import com.example.showflake.tmdb.TMDBUseCase
import com.example.showflake.tmdb.model.Program
import com.example.showflake.tmdb.model.ProgramInfoList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tmdbUseCase: TMDBUseCase) : ViewModel() {
    private val itemsEmitter =
        MutableStateFlow(ProgramInfoList())
    val items: StateFlow<ProgramInfoList> get() = itemsEmitter.asStateFlow()

    private val searchEmitter = MutableStateFlow<List<Program>>(emptyList())
    val searchResults: StateFlow<List<Program>> = searchEmitter
    var selectedContent: Program? = null
    private val favoriteListEmitter = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteList: StateFlow<List<Movie>> get() = favoriteListEmitter

    private val similarShowListEmitter = MutableStateFlow<ProgramInfoList>(ProgramInfoList())
    val similarShowList: StateFlow<ProgramInfoList> get() = similarShowListEmitter
    fun getTrending() {
        viewModelScope.launch {
            try {
                val result = tmdbUseCase.getTrending(hashMapOf(), hashMapOf())
                handleTrendingResult(result)
            } catch (e: Exception) {
                Log.d(">>>>", "getTrending: $e ")
            }
        }
    }

    fun searchContent(searchString: String) {
        viewModelScope.launch {
            try {
                val result = tmdbUseCase.searchContent(searchString)
                handleTrendingResult(result)
            } catch (e: Exception) {
                Log.d(">>>>", "getTrending: $e ")
            }
        }
    }

    fun markAsFavorite(movieId: Movie, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                tmdbUseCase.markAsFavorite(movieId, isFavorite)
            } catch (e: Exception) {
                Log.d(">>>>", "getTrending: $e ")
            }
        }
    }

    fun updateFavorite(movieId: Movie, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                tmdbUseCase.updateFavorite(movieId, isFavorite)
            } catch (e: Exception) {
                Log.d(">>>>", "getTrending: $e ")
            }
        }
    }

    fun getSimilarShows(tvId: Int) {
        viewModelScope.launch {
            try {
                similarShowListEmitter.emit(tmdbUseCase.getSimilarShows(tvId))
            } catch (e: Exception) {
                Log.d(">>>>", "getTrending: $e ")
            }
        }
    }

    suspend fun getFavoriteMovies() {
        favoriteListEmitter.value = tmdbUseCase.getFavoriteMovies()
    }

    private fun handleTrendingResult(response: ProgramInfoList) {
        viewModelScope.launch(Dispatchers.IO) { itemsEmitter.emit(response) }
    }

}