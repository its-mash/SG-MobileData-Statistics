package com.example.sg_mobiledata_statistics.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.sg_mobiledata_statistics.other.Event
import com.example.sg_mobiledata_statistics.other.Resource
import com.example.sg_mobiledata_statistics.repositories.MobileDataUsageRepository

import kotlinx.coroutines.launch
import kotlin.Exception

class MobileDataUsageViewModel @ViewModelInject constructor(
    private val repository: MobileDataUsageRepository
) : ViewModel() {

    val mobileDataUsageRecords = repository.observeAllMobileDataUsageRecords()
    val yearlyMobileDataUsageRecords=repository.observeYearlyMobileDataUsage()

    private val _mobileDataUsageRefreshStat = MutableLiveData<Event<Resource<Boolean>>>()
    val mobileDataUsageRefreshStat: LiveData<Event<Resource<Boolean>>> = _mobileDataUsageRefreshStat


    fun refreshMobileDataUsageStatus() {
        _mobileDataUsageRefreshStat.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.refreshMobileDataUsageRecords()
            _mobileDataUsageRefreshStat.value = Event(response)
        }
    }
}













