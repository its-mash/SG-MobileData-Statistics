package com.example.sg_mobiledata_statistics.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sg_mobiledata_statistics.data.local.MobileDataUsageRecord
import com.example.sg_mobiledata_statistics.data.local.YearlyMobileDataUsageRecord
import com.example.sg_mobiledata_statistics.other.Constants
import com.example.sg_mobiledata_statistics.other.Constants.INTERNAL_SERVER_ERROR_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.NO_INTERNET_CONNECTION_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.SUCCESS_REFRESH_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.UNKNOWN_ERROR_MESSAGE
import com.example.sg_mobiledata_statistics.other.Resource
import java.net.ConnectException

class FakeMobileDataUsageRepository : MobileDataUsageRepository {

    private val mobileDataUsageRecords = mutableListOf<MobileDataUsageRecord>()

    private val observableMobileDataRecords = MutableLiveData<List<MobileDataUsageRecord>>(mobileDataUsageRecords)
    private val observableYearlyMobileDataUsageRecords = MutableLiveData<List<YearlyMobileDataUsageRecord>>()

    private var noInternetConnection = false
    fun setNoInternetConnection(flag: Boolean){
        noInternetConnection=flag
    }
    private var internalServerError = false
    fun setInternalServerError(flag: Boolean){
        internalServerError=flag
    }
    private var responseParseError=false
    fun setResponseParseError(flag: Boolean){
        responseParseError=flag
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

    override suspend fun clearMobileDataUsageRecord() {
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
                noInternetConnection -> throw Exception(NO_INTERNET_CONNECTION_MESSAGE)
                internalServerError -> Resource.error(INTERNAL_SERVER_ERROR_MESSAGE,null)
                responseParseError -> throw Exception(UNKNOWN_ERROR_MESSAGE)
                else -> {
                    val mobileDataUsageRecord1= MobileDataUsageRecord(2018,"Q1",0.020)
                    val mobileDataUsageRecord2= MobileDataUsageRecord(2018,"Q1",0.020)
                    val mobileDataUsageRecord3= MobileDataUsageRecord(2019,"Q1",0.020)

                    insertMobileDataUsageRecord(mobileDataUsageRecord1)
                    insertMobileDataUsageRecord(mobileDataUsageRecord2)
                    insertMobileDataUsageRecord(mobileDataUsageRecord3)

                    Resource.success(true, SUCCESS_REFRESH_MESSAGE)
                }
            }
        } catch(e: Exception) {
            Resource.error(e.localizedMessage, null)
        }
    }
}











