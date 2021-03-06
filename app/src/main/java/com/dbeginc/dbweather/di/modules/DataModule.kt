/*
 *  Copyright (C) 2017 Darel Bitsy
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.dbeginc.dbweather.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.dbeginc.dbweather.utils.helper.ApplicationPreferences
import com.dbeginc.dbweathercommon.utils.AppScope
import com.dbeginc.dbweatherdata.implementations.repositories.ConfigurationRepositoryImpl
import com.dbeginc.dbweatherdata.implementations.repositories.NewsRepositoryImpl
import com.dbeginc.dbweatherdata.implementations.repositories.WeatherRepositoryImpl
import com.dbeginc.dbweatherdomain.repositories.configurations.ConfigurationsRepository
import com.dbeginc.dbweatherdomain.repositories.news.NewsRepository
import com.dbeginc.dbweatherdomain.repositories.weather.WeatherRepository
import dagger.Module
import dagger.Provides

/**
 * Created by darel on 18.09.17.
 *
 * Application Data Module
 */
@Module
class DataModule {

    @AppScope
    @Provides
    fun provideWeatherRepository(context: Context): WeatherRepository {
        return WeatherRepositoryImpl.create(context)
    }

    @AppScope
    @Provides
    fun provideNewsRepository(context: Context): NewsRepository {
        return NewsRepositoryImpl.create(context)
    }

    @AppScope
    @Provides
    fun provideConfigurationRepository(context: Context): ConfigurationsRepository {
        return ConfigurationRepositoryImpl.create(context)
    }

    @Provides
    @AppScope
    fun provideApplicationPreferences(sharedPreferences: SharedPreferences): ApplicationPreferences = ApplicationPreferences(sharedPreferences)
}