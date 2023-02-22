package com.mohamedrafat.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohamedrafat.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        private var instance:ArticleDatabase? = null
        private val LOCK = Any()

        // ده يعتبر constructor
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        // هنا علشان لو ال database مش متكريته يبدا يكريتها
        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext ,
            ArticleDatabase::class.java ,
            "articles_db.db"
        ).build()

    }



}