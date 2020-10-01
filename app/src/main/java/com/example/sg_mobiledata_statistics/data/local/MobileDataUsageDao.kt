package com.example.sg_mobiledata_statistics.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MobileDataUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord)

    @Delete
    suspend fun deleteMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord)

    @Query("DELETE from mobile_data_usage_record")
    suspend fun clearMobileDataUsageRecord()

    @Query("SELECT * FROM mobile_data_usage_record")
    fun observeAllMobileDataUsageRecords(): LiveData<List<MobileDataUsageRecord>>

    @Query("SELECT year, SUM(volume) as totalVolume FROM mobile_data_usage_record GROUP BY year")
    fun observeYearlyMobileDataUsage(): LiveData<List<YearlyMobileDataUsageRecord>>

}