package com.example.sg_mobiledata_statistics.repositories

import androidx.lifecycle.LiveData
import com.example.sg_mobiledata_statistics.data.local.MobileDataUsageRecord
import com.example.sg_mobiledata_statistics.data.local.YearlyMobileDataUsageRecord
import com.example.sg_mobiledata_statistics.other.Resource

interface MobileDataUsageRepository {

    suspend fun insertMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord)

    suspend fun deleteMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord)

    suspend fun clearMobileDataUsageRecord()

    fun observeAllMobileDataUsageRecords(): LiveData<List<MobileDataUsageRecord>>

    fun observeYearlyMobileDataUsage(): LiveData<List<YearlyMobileDataUsageRecord>>

    suspend fun refreshMobileDataUsageRecords(): Resource<Boolean>
}