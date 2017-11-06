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

package com.dbeginc.dbweather.weather.adapters.daily.presenter

import com.dbeginc.dbweather.weather.adapters.daily.DayContract
import com.dbeginc.dbweather.viewmodels.weather.DayWeatherModel

/**
 * Created by darel on 23.09.17.
 *
 * Day View Presenter Implementation
 */
class DayPresenterImpl(private var day: DayWeatherModel) : DayContract.DayPresenter {
    private lateinit var view: DayContract.DayView

    override fun bind(view: DayContract.DayView) {
        this.view = view
        this.view.setupView()
    }

    override fun getModel(): DayWeatherModel = day

    override fun setModel(dayWeatherModel: DayWeatherModel) { day = dayWeatherModel }

    override fun loadDay() = view.displayDay(day)

    override fun goToDayDetail() = view.showDetail()

    override fun unBind() {/* not needed here */}
}