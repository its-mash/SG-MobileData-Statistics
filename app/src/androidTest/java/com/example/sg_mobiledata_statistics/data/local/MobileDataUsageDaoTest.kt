package com.example.sg_mobiledata_statistics.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.sg_mobiledata_statistics.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MobileDataUsageDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MobileDataUsageDatabase
    private lateinit var dao: MobileDataUsageDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MobileDataUsageDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.mobileDataUsageDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertMobileDataUsageRecord() = runBlockingTest {
        val mobileDataUsageRecord= MobileDataUsageRecord(2018,"Q1",0.038, id=1)
        dao.insertMobileDataUsageRecord(mobileDataUsageRecord)
        val allRecords = dao.observeAllMobileDataUsageRecords().getOrAwaitValue()

        assertThat(allRecords).contains(mobileDataUsageRecord)
    }

    @Test
    fun deleteMobileDataUsageRecord() = runBlockingTest {
        val mobileDataUsageRecord= MobileDataUsageRecord(2018,"Q1",0.038)
        dao.insertMobileDataUsageRecord(mobileDataUsageRecord)
        dao.deleteMobileDataUsageRecord(mobileDataUsageRecord)

        val allRecords = dao.observeAllMobileDataUsageRecords().getOrAwaitValue()

        assertThat(allRecords).doesNotContain(mobileDataUsageRecord)
    }

    @Test
    fun observeYearlyMobileDataUsage() = runBlockingTest {
        val mobileDataUsageRecord1= MobileDataUsageRecord(2018,"Q1",0.020)
        val mobileDataUsageRecord2= MobileDataUsageRecord(2018,"Q1",0.020)
        val mobileDataUsageRecord3= MobileDataUsageRecord(2019,"Q1",0.020)

        dao.insertMobileDataUsageRecord(mobileDataUsageRecord1)
        dao.insertMobileDataUsageRecord(mobileDataUsageRecord2)
        dao.insertMobileDataUsageRecord(mobileDataUsageRecord3)

        val yearlyRecords = dao.observeYearlyMobileDataUsage().getOrAwaitValue()
        assertThat(yearlyRecords.count()).isEqualTo(2)
        assertThat(yearlyRecords[0].totalVolume).isEqualTo(.04)
        assertThat(yearlyRecords[1].totalVolume).isEqualTo(.02)
    }
}













