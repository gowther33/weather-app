package com.example.core.di

import android.content.Context
import com.example.core.data.WeatherDataSource
import com.example.core.implementation.WeatherRepositoryImplementation
import com.example.core.network.ApiInterface
import com.example.core.repository.WeatherRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesWeatherDataSource(
        @ApplicationContext context:Context,
        api:ApiInterface
    ):WeatherDataSource {
        return WeatherDataSource(api, context)
    }


    @Provides
    @ViewModelScoped
    fun providesWeatherDataRepository(
        dataSource: WeatherDataSource
    ):WeatherRepositoryInterface{
        return WeatherRepositoryImplementation(dataSource)
    }

}