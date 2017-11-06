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

package com.dbeginc.dbweatherdomain.usecases

import io.reactivex.Single

/**
 * Created by darel on 18.09.17.
 *
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstract class represents a execution unit for different use cases
 * which only return the value requested or a error
 *
 * By convention each UseCase implementation will return the result using a {@link DisposableSubscriber}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCaseSingle<T, in Params>  {
    /**
     * Builds an [Single] which will be used when executing the current [UseCase].
     */
    internal abstract fun buildUseCase(params: Params): Single<T>

    /**
     * Executes the current use case.
     *
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    fun execute(params: Params) : Single<T> = buildUseCase(params)

    /**
     * Clean all resource used use case.
     */
    abstract fun clean()
}