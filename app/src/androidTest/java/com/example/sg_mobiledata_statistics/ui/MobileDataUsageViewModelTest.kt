package com.example.sg_mobiledata_statistics.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sg_mobiledata_statistics.getOrAwaitValue
import com.example.sg_mobiledata_statistics.other.Constants.INTERNAL_SERVER_ERROR_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.NO_INTERNET_CONNECTION_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.SUCCESS_REFRESH_MESSAGE
import com.example.sg_mobiledata_statistics.other.Constants.UNKNOWN_ERROR_MESSAGE
import com.example.sg_mobiledata_statistics.other.Status
import com.example.sg_mobiledata_statistics.repositories.FakeMobileDataUsageRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MobileDataUsageViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: MobileDataUsageViewModel
    lateinit var  repository: FakeMobileDataUsageRepository
    @Before
    fun setup(){
        repository= FakeMobileDataUsageRepository()
        viewModel= MobileDataUsageViewModel(repository)
    }
    @Test
    fun refreshMobileDataUsageStatusNoInternetConnectionTest() {
        repository.setNoInternetConnection(true)
        viewModel.refreshMobileDataUsageStatus()
        Thread.sleep(2000L)
        val resource= viewModel.mobileDataUsageRefreshStat.getOrAwaitValue().getContentIfNotHandled()
        assertThat(resource?.status).isEqualTo(Status.ERROR)
        assertThat(resource?.message).isEqualTo(NO_INTERNET_CONNECTION_MESSAGE)
    }
    @Test
    fun refreshMobileDataUsageStatusInternalServerError() {
        repository.setInternalServerError(true)
        viewModel.refreshMobileDataUsageStatus()
        Thread.sleep(2000L)
        val resource= viewModel.mobileDataUsageRefreshStat.getOrAwaitValue().getContentIfNotHandled()
        assertThat(resource?.status).isEqualTo(Status.ERROR)
        assertThat(resource?.message).isEqualTo(INTERNAL_SERVER_ERROR_MESSAGE)

    }

    @Test
    fun refreshMobileDataUsageStatusResponseParseError() {
        repository.setResponseParseError(true)
        viewModel.refreshMobileDataUsageStatus()
        Thread.sleep(2000L)
        val resource= viewModel.mobileDataUsageRefreshStat.getOrAwaitValue().getContentIfNotHandled()
        assertThat(resource?.status).isEqualTo(Status.ERROR)
        assertThat(resource?.message).isEqualTo(UNKNOWN_ERROR_MESSAGE)
    }
    @Test
    fun refreshMobileDataUsageStatusSuccessfulRefresh() {
        viewModel.refreshMobileDataUsageStatus()
        Thread.sleep(2000L)
        val resource= viewModel.mobileDataUsageRefreshStat.getOrAwaitValue().getContentIfNotHandled()

        assertThat(resource?.status).isEqualTo(Status.SUCCESS)
        assertThat(resource?.message).isEqualTo(SUCCESS_REFRESH_MESSAGE)

        val recordsCount=viewModel.mobileDataUsageRecords.getOrAwaitValue ().count()
        assertThat(recordsCount).isEqualTo(3)

        val yearlyRecordsCount=viewModel.yearlyMobileDataUsageRecords.getOrAwaitValue ().count()
        assertThat(yearlyRecordsCount).isEqualTo(2)
    }
}