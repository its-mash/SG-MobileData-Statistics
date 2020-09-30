package com.example.sg_mobiledata_statistics.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sg_mobiledata_statistics.data.local.MobileDataUsageRecord
import com.example.sg_mobiledata_statistics.data.local.YearlyMobileDataUsageRecord
import com.example.sg_mobiledata_statistics.other.Constants
import com.example.sg_mobiledata_statistics.other.Resource
import java.net.ConnectException

class FakeMobileDataUsageRepository : MobileDataUsageRepository {

    private val mobileDataUsageRecords = mutableListOf<MobileDataUsageRecord>()

    private val observableMobileDataRecords = MutableLiveData<List<MobileDataUsageRecord>>(mobileDataUsageRecords)
    private val observableYearlyMobileDataUsageRecords = MutableLiveData<List<YearlyMobileDataUsageRecord>>()

    private var notInternetConnection = false
        set(value) {
            field = value
        }
    private var internalServerError = false
        set(value) {
            field = value
        }
    private var responseParseError=false
        set(value) {
            field = value
        }

    private fun refreshLiveData() {
        observableMobileDataRecords.postValue(mobileDataUsageRecords)
        val yearlyMobileDataUsageRecords = mutableListOf<YearlyMobileDataUsageRecord>()
        val records=mobileDataUsageRecords.groupBy { it.year }.mapValues { (year,record)->{record.sumByDouble { it.volume }} }
        records.forEach { (k,v)->yearlyMobileDataUsageRecords.add(YearlyMobileDataUsageRecord(k,v())) }
        observableYearlyMobileDataUsageRecords.postValue(yearlyMobileDataUsageRecords)
    }

    override suspend fun insertMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord) {
        mobileDataUsageRecords.add(mobileDataUsageRecord)
        refreshLiveData()
    }

    override suspend fun deleteMobileDataUsageRecord(mobileDataUsageRecord: MobileDataUsageRecord) {
        mobileDataUsageRecords.remove(mobileDataUsageRecord)
        refreshLiveData()
    }

    override fun clearMobileDataUsageRecord() {
        mobileDataUsageRecords.clear()
        refreshLiveData()
    }

    override fun observeAllMobileDataUsageRecords(): LiveData<List<MobileDataUsageRecord>> {
        return observableMobileDataRecords
    }

    override fun observeYearlyMobileDataUsage(): LiveData<List<YearlyMobileDataUsageRecord>> {
        return observableYearlyMobileDataUsageRecords
    }

    override suspend fun refreshMobileDataUsageRecords(): Resource<Boolean> {
        return try {
            when {
                notInternetConnection -> throw Exception("Check Internet Connection availability")
                internalServerError -> Resource.error("Internal Server Error",null)
                responseParseError -> throw Exception("An Unknown Error Occurred")
                else -> {
                    val mobileDataUsageRecord1= MobileDataUsageRecord(2018,"Q1",0.020)
                    val mobileDataUsageRecord2= MobileDataUsageRecord(2018,"Q1",0.020)
                    val mobileDataUsageRecord3= MobileDataUsageRecord(2018,"Q1",0.020)

                    insertMobileDataUsageRecord(mobileDataUsageRecord1)
                    insertMobileDataUsageRecord(mobileDataUsageRecord2)
                    insertMobileDataUsageRecord(mobileDataUsageRecord3)

                    Resource.success(true, "Successfully Refreshed")
                }
            }
        } catch(e: Exception) {
            Resource.error(e.localizedMessage, null)
        }
    }
}











