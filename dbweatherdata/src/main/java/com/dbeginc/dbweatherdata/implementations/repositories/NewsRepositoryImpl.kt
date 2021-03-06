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

package com.dbeginc.dbweatherdata.implementations.repositories

import android.content.Context
import android.util.Log
import com.dbeginc.dbweatherdata.ConstantHolder
import com.dbeginc.dbweatherdata.ThreadProvider
import com.dbeginc.dbweatherdata.implementations.datasources.local.LocalNewsDataSource
import com.dbeginc.dbweatherdata.implementations.datasources.local.news.LocalNewsDataSourceImpl
import com.dbeginc.dbweatherdata.implementations.datasources.remote.RemoteNewsDataSource
import com.dbeginc.dbweatherdata.implementations.datasources.remote.news.RemoteNewsDataSourceImpl
import com.dbeginc.dbweatherdomain.entities.news.Article
import com.dbeginc.dbweatherdomain.entities.news.Live
import com.dbeginc.dbweatherdomain.entities.news.Source
import com.dbeginc.dbweatherdomain.entities.requests.news.ArticleRequest
import com.dbeginc.dbweatherdomain.entities.requests.news.LiveRequest
import com.dbeginc.dbweatherdomain.entities.requests.news.NewsRequest
import com.dbeginc.dbweatherdomain.entities.requests.news.SourceRequest
import com.dbeginc.dbweatherdomain.repositories.news.NewsRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableCompletableObserver

/**
 * Created by darel on 06.10.17.
 *
 * News Repository Implementation
 */
class NewsRepositoryImpl private constructor(private val thread: ThreadProvider,
                                             private val local: LocalNewsDataSource,
                                             private val remote: RemoteNewsDataSource) : NewsRepository, Consumer<Throwable> {

    companion object {
        fun create(context: Context): NewsRepository {
            return NewsRepositoryImpl(
                    ThreadProvider,
                    LocalNewsDataSourceImpl.create(context),
                    RemoteNewsDataSourceImpl.create(context)
            )
        }
    }

    private val subscriptions = CompositeDisposable()

    override fun getArticles(request: NewsRequest<Unit>): Flowable<List<Article>> {
        return local.getArticles(request)
                .subscribeOn(thread.computation)
                .doOnSubscribe {
                    remote.getArticles(request.sources)
                            .subscribeOn(thread.io)
                            .subscribe(
                                    { articles -> subscriptions.addArticles(articles) },
                                    this::accept
                            )
                }
                .observeOn(thread.ui)
    }

    override fun getTranslatedArticles(request: NewsRequest<Unit>): Flowable<List<Article>> {
        return local.getArticles(request)
                .subscribeOn(thread.computation)
                .doOnSubscribe {
                    remote.getTranslatedArticles(request.sources)
                            .subscribeOn(thread.io)
                            .subscribe(
                                    { articles -> subscriptions.addArticles(articles) },
                                    this::accept
                            )
                }
                .observeOn(thread.ui)
    }

    override fun getArticle(request: ArticleRequest<Unit>): Single<Article> {
        return local.getArticle(request)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
    }

    override fun getAllSources(): Flowable<List<Source>> {
        return local.getSources()
                .subscribeOn(thread.computation)
                .doOnSubscribe {
                    remote.getSources().subscribeOn(thread.io)
                            .zipWith(
                                    local.getSources().subscribeOn(thread.computation),
                                    BiFunction<List<Source>, List<Source>, List<Source>> { remoteResponse, localResponse ->
                                        remoteResponse.zip(localResponse, { right, left -> right.apply { subscribed = left.subscribed } })
                                    }
                            )
                            .subscribe({ sources -> subscriptions.addSources(sources) }, this::accept)
                }.observeOn(thread.ui)
    }

    override fun getSubscribedSources(): Flowable<List<Source>> {
        return local.getSubscribedSources()
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
                .toFlowable()
    }

    override fun subscribeToSource(request: SourceRequest<Source>): Completable {
        return local.updateSource(request.arg)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
    }

    override fun unSubscribeToSource(request: SourceRequest<Source>): Completable {
        return local.updateSource(request.arg)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
    }

    override fun getSource(request: SourceRequest<Unit>): Single<Source> {
        return local.getSource(request.sourceId)
                .subscribeOn(thread.computation)
                .observeOn(thread.io)
    }

    override fun getAllLives(): Flowable<List<Live>> {
        return local.getAllLives()
                .subscribeOn(thread.computation)
                .doOnSubscribe {
                    remote.getAllLives().subscribeOn(thread.io)
                            .subscribe(
                                    { lives -> subscriptions.addLives(lives) },
                                    this::accept
                            )
                }.observeOn(thread.ui)
    }

    override fun getLives(names: List<String>): Flowable<List<Live>> {
        return local.getLives(names)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
                .toFlowable()
    }

    override fun getFavoriteLives(): Flowable<List<String>> {
        return local.getFavoriteLives()
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
                .toFlowable()
    }

    override fun getLive(name: String): Single<Live> {
        return local.getLive(name)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
    }

    override fun addLiveToFavorites(request: LiveRequest<Unit>): Completable {
        return local.addLiveToFavorite(request)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
    }

    override fun removeLiveFromFavorites(request: LiveRequest<Unit>): Completable {
        return local.removeLiveFromFavorite(request)
                .subscribeOn(thread.computation)
                .observeOn(thread.ui)
    }

    override fun defineDefaultSubscribedSources(sourcesId: List<String>): Completable {
        return remote.getSources()
                .subscribeOn(thread.io)
                .map { sources -> sources.map { source -> source.apply { if (sourcesId.contains(source.id)) subscribed = true } } }
                .flatMapCompletable { sources -> local.defineDefaultSubscribedSources(sources).subscribeOn(thread.computation) }
                .observeOn(thread.ui)
    }

    override fun accept(error: Throwable?) {
        Log.e(ConstantHolder.TAG, "Error: ${NewsRepository::class.java.simpleName}", error)
    }

    override fun clean() = subscriptions.clear()

    private fun CompositeDisposable.addSources(sources: List<Source>) {
        add(local.putSources(sources)
                .subscribeOn(thread.computation)
                .subscribeWith(TaskObserver())
        )
    }

    private fun CompositeDisposable.addArticles(articles: List<Article>) {
        add(local.putArticles(articles)
                .subscribeOn(thread.computation)
                .subscribeWith(TaskObserver())
        )
    }

    private fun CompositeDisposable.addLives(lives: List<Live>) {
        add(local.putLives(lives).subscribeOn(thread.computation)
                .subscribeWith(TaskObserver())
        )
    }

    private inner class TaskObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            Log.i(ConstantHolder.TAG, "Update of data done in ${NewsRepository::class.java.simpleName}")
        }

        override fun onError(e: Throwable) {
            Log.e(ConstantHolder.TAG, "Error: ${NewsRepository::class.java.simpleName}", e)
        }
    }
}