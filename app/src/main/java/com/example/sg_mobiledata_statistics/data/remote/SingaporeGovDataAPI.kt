package com.example.sg_mobiledata_statistics.data.remote

import com.example.sg_mobiledata_statistics.data.remote.responses.MobileDataUsageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface SingaporeGovDataAPI {

    @GET("/api/action/datastore_search")
    suspend fun getMobileDataUsage(
        @Query("resource_id") resourceId: String
    ): Response<MobileDataUsageResponse>
}

