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

package com.dbeginc.dbweather.news.lives.page.alllives.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dbeginc.dbweather.R
import com.dbeginc.dbweather.news.lives.page.LiveDiffUtils
import com.dbeginc.dbweather.news.lives.page.alllives.adapter.contract.LivePresenter
import com.dbeginc.dbweather.news.lives.page.alllives.adapter.presenter.LivePresenterImpl
import com.dbeginc.dbweather.news.lives.page.alllives.adapter.view.LiveViewHolder
import com.dbeginc.dbweatherdomain.repositories.news.NewsRepository
import com.dbeginc.dbweathernews.viewmodels.LiveModel

/**
 * Created by darel on 18.10.17.
 *
 * Live adapter
 */
class LiveAdapter(private val model: NewsRepository) : RecyclerView.Adapter<LiveViewHolder>() {
    private var container: RecyclerView? = null
    private var presenters: Array<LivePresenter> = emptyArray()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)

        container = recyclerView
    }

    override fun getItemCount(): Int = presenters.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveViewHolder {
        return LiveViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent?.context),
                        R.layout.live_item, parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: LiveViewHolder, position: Int) {
        val presenter = presenters[position]

        // cleaning state of view holder before binding it to new data
        holder.cleanState()

        holder.definePresenter(presenter)

        presenter.bind(holder)

        presenter.loadLive(holder)
    }

    @Synchronized
    fun updateData(newData: List<LiveModel>) {
        val oldList = presenters.map { presenter -> presenter.getData() }.toTypedArray()

        val newList = newData.toTypedArray().sortedArray()

        val result = DiffUtil.calculateDiff(LiveDiffUtils(oldList, newList))

        presenters = newList.map { live -> LivePresenterImpl(live, model) }.toTypedArray()

        result.dispatchUpdatesTo(this@LiveAdapter)
    }
}