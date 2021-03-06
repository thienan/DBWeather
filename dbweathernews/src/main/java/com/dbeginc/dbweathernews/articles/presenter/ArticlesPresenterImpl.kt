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

package com.dbeginc.dbweathernews.articles.presenter

import com.dbeginc.dbweathernews.articles.contract.ArticlesPresenter
import com.dbeginc.dbweathernews.articles.contract.ArticlesView
import com.dbeginc.dbweathernews.viewmodels.ArticleModel

/**
 * Created by darel on 17.11.17.
 *
 * Articles Presenter Implementation
 */
class ArticlesPresenterImpl(private var data: List<ArticleModel>) : ArticlesPresenter{

    override fun bind(view: ArticlesView) = view.setupView()

    override fun unBind() {}

    override fun loadArticles(view: ArticlesView) = view.displayArticles(data)

    override fun updateModel(view: ArticlesView, articles: List<ArticleModel>) {
        data = articles
        view.displayArticles(data)
    }
}