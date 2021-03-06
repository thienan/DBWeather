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

package com.dbeginc.dbweatherdata.proxies.local.weather

import android.arch.persistence.room.Ignore
import android.support.annotation.RestrictTo
import com.dbeginc.dbweatherdomain.entities.weather.Alert

/**
 * Created by darel on 16.09.17.
 *
 * Local Weather Alert
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class LocalAlert(val time: Long, val title: String, val description: String, val uri: String,
                 val expires: Long, val regions: List<String>, val severity: String
)