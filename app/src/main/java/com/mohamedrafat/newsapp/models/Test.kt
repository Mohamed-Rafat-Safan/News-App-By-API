package com.mohamedrafat.newsapp.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ahmed")
data class Test(
    val name:String,
    val age:Int,
    val source: Source):Parcelable {
}

