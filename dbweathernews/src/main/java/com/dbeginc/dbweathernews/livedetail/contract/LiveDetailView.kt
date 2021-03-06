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

package com.dbeginc.dbweathernews.livedetail.contract

import com.dbeginc.dbweathercommon.view.IView
import com.dbeginc.dbweathernews.viewmodels.LiveModel

/**
 * Created by darel on 17.11.17.
 *
 * Live Detail View
 */
interface LiveDetailView : IView {
    fun displayLive(live: LiveModel)

    fun getLiveName(): String

    fun showLiveIsNotFavorite()

    fun showLiveIsFavorite()

    fun shareLive()

    fun close()

    fun showLoading()

    fun hideLoading()
}