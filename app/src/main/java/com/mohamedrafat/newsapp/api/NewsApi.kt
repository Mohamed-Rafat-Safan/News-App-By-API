package com.mohamedrafat.newsapp.api

import com.mohamedrafat.newsapp.models.NewsResponse
import com.mohamedrafat.newsapp.util.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    // هذه الداله لكي تجيب كل الاخبار العاجله فقط
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String = "us",
        @Query("page")
        pageNumber:Int = 1 ,
        @Query("apiKey")
        apiKey:String = API_KEY
    ):Response<NewsResponse>


    // هذه الداله لكي تجيب كل الاخبار ,وليس الاخبار العاجله فقط
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String ,
        @Query("page")
        pageNumber:Int = 1 ,
        @Query("apiKey")
        apiKey:String = API_KEY
    ):Response<NewsResponse>
}