package com.mohamedrafat.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamedrafat.newsapp.NewsApplication
import com.mohamedrafat.newsapp.models.Article
import com.mohamedrafat.newsapp.models.NewsResponse
import com.mohamedrafat.newsapp.repository.NewsRepository
import com.mohamedrafat.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val listBreakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    //  ديه رقم الصفحه الي هجيب منها الاخبار كلها
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null


    val listSearchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // ديه رقم الصفحه الي هيحث فيها
    var searcheNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
       safeSearchNewsCall(searchQuery)
    }


    private fun handelBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        // هنا في حالة لو الداتا رجعت وكله تمام
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        // لو حصل خطأ والداتا مرجعتش
        return Resource.Error(response.message())
    }


    private fun handelSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        // هنا في حالة لو الداتا رجعت وكله تمام
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searcheNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        // لو حصل خطأ والداتا مرجعتش
        return Resource.Error(response.message())
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        val articleNumber: Long = newsRepository.isArtAlreadySaved(article)
        if (articleNumber <= 0) {
            newsRepository.updateOrInsertArticle(article)
        }
    }


    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }


    suspend fun safeBreakingNewsCall(countryCode: String){
        listBreakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                listBreakingNews.postValue(handelBreakingNewsResponse(response))
            }else{
                listBreakingNews.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> listBreakingNews.postValue(Resource.Error("Network failure"))
                else -> listBreakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    suspend fun safeSearchNewsCall(searchQuery: String){
        listSearchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery, searcheNewsPage)
                listSearchNews.postValue(handelSearchNewsResponse(response))
            }else{
                listSearchNews.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> listSearchNews.postValue(Resource.Error("Network failure"))
                else -> listSearchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }

}


