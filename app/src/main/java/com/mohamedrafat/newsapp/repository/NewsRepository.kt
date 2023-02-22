package com.mohamedrafat.newsapp.repository

import com.mohamedrafat.newsapp.api.RetrofitInstance
import com.mohamedrafat.newsapp.db.ArticleDatabase
import com.mohamedrafat.newsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun updateOrInsertArticle(article: Article) =
        db.getArticleDao().updateOrInsertArticle(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

   suspend fun isArtAlreadySaved(article: Article):Long = db.getArticleDao().isArtAlreadySaved(article.url)

}