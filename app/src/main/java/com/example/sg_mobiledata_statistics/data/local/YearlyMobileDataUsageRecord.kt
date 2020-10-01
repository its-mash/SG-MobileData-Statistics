package com.example.sg_mobiledata_statistics.data.local

data class YearlyMobileDataUsageRecord(val year:Int,val totalVolume:Double,var decrease:Boolean) {
    constructor(year: Int,totalVolume: Double) : this(year,totalVolume,false)
}

