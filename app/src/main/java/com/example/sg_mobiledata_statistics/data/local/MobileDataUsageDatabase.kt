package com.example.sg_mobiledata_statistics.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MobileDataUsageRecord::class],
    version = 1
)
abstract class MobileDataUsageDatabase : RoomDatabase() {

    abstract fun mobileDataUsageDao(): MobileDataUsageDao
}