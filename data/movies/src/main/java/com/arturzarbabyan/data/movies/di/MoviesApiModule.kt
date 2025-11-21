package com.arturzarbabyan.data.movies.di

import com.arturzarbabyan.data.movies.remote.api.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object MoviesApiModule {

    @Provides
    @Singleton
    fun provideMoviesApi(
        retrofit: Retrofit
    ): MoviesApi = retrofit.create(MoviesApi::class.java)
}