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

package com.dbeginc.dbweatherdata.proxies.remote.news

import android.support.annotation.RestrictTo
import com.squareup.moshi.Json

/**
 * Created by darel on 04.10.17.
 *
 * Remote Source
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class RemoteSource(@Json(name="id") val id: String,
                        @Json(name="name") val name: String,
                        @Json(name="description") val description: String,
                        @Json(name="url") val url: String,
                        @Json(name="category") val category: String,
                        @Json(name = "language") val language: String,
                        @Json(name = "country") val country: String,
                        @Json(name = "sortBysAvailable") val sortBysAvailable: List<String>
)