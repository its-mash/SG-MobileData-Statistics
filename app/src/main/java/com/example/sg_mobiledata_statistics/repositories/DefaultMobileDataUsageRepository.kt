package com.example.sg_mobiledata_statistics.repositories

import androidx.lifecycle.LiveData
import com.example.sg_mobiledata_statistics.data.local.MobileDataUsageDao
import com.example.sg_mobiledata_statistics.data.local.MobileDataUsageRecord
import com.example.sg_mobiledata_statistics.data.local.YearlyMobileDataUsageRecord
import com.example.sg_mobiledata_statistics.data.remote.SingaporeGovDataAPI
import com.example.sg_mobiledata_statistics.data.remote.responses.MobileDataUsageResponse
import com.example.sg_mobiledata_statistics.other.Constants.RESOURCE_ID
import com.example.sg_mobiledata_statistics.other.Constants.SUCCESS_REFRESH_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.UNKNOWN_ERROR_MESSAGE
import com.example.sg_mobiledata_statistics.other.Resource
import javax.inject.Inject

class DefaultMobileDataUsageRepository @Inject constructor(
    private val mobileDataUsageDao: MobileDataUsageDao,
    private val singaporeGovDataAPI: SingaporeGovDataAPI
) : MobileDataUsageRepository {

    override suspend fun insertMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord) {
        mobileDataUsageDao.insertMobileDataUsageRecord(mobileDataUsageRecord)
    }

    override suspend fun deleteMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord) {
        mobileDataUsageDao.deleteMobileDataUsageRecord(mobileDataUsageRecord)
    }

    override suspend fun clearMobileDataUsageRecord() {
        mobileDataUsageDao.clearMobileDataUsageRecord()
    }
    override fun observeAllMobileDataUsageRecords(): LiveData<List<MobileDataUsageRecord>> {
        return mobileDataUsageDao.observeAllMobileDataUsageRecords()
    }

    override fun observeYearlyMobileDataUsage(): LiveData<List<YearlyMobileDataUsageRecord> > {
        return mobileDataUsageDao.observeYearlyMobileDataUsage()
    }



    override suspend fun refreshMobileDataUsageRecords(): Resource<Boolean> {
        return try {
            val response = singaporeGovDataAPI.getMobileDataUsage(RESOURCE_ID)
            if(response.isSuccessful) {
                response.body()?.let {

                    clearMobileDataUsageRecord()

                    val records=it.result.records
                    for (record in records){
                        val (year,quarter)=record.quarter.split('-')
                        insertMobileDataUsageRecord(MobileDataUsageRecord(year.toInt(),quarter,record.volume_of_mobile_data.toDouble()))
                    }
                    return@let Resource.success(true, SUCCESS_REFRESH_MESSAGE)
                } ?: Resource.error(UNKNOWN_ERROR_MESSAGE, null)
            } else {
                Resource.error(response.message(), null)
            }
        } catch(e: Exception) {
            Resource.error(e.localizedMessage, null)
        }
    }
}














