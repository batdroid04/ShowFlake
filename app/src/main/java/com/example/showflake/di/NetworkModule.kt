package com.example.showflake.di

import android.content.Context
import androidx.room.Room
import com.example.showflake.ApiConstants
import com.example.showflake.db.DBConstants
import com.example.showflake.db.MovieDao
import com.example.showflake.db.MovieDatabase
import com.example.showflake.tmdb.TMDBUseCase
import com.example.showflake.tmdb.TMDBUseCaseImpl
import com.example.showflake.tmdb.api.TMDBApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            DBConstants.DB_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(appDatabase: MovieDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideTMDbApi(retrofit: Retrofit): TMDBApi {
        return retrofit.create(TMDBApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTMDBUseCase(tmdbApi: TMDBApi, movieDao: MovieDao): TMDBUseCase {
        return TMDBUseCaseImpl(tmdbApi, movieDao)
    }
}
