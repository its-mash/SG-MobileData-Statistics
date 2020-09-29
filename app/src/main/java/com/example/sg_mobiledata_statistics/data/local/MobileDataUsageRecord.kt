package com.example.sg_mobiledata_statistics.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mobile_data_usage_record")
data class MobileDataUsageRecord (
    var year:Int,
    var quarter:String,
    var volume:Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)