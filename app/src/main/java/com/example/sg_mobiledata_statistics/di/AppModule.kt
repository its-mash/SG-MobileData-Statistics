package com.example.sg_mobiledata_statistics.di

import android.content.Context
import androidx.room.Room
import com.example.sg_mobiledata_statistics.other.Constants.BASE_URL
import com.example.sg_mobiledata_statistics.other.Constants.DATABASE_NAME
import com.example.sg_mobiledata_statistics.data.local.MobileDataUsageDatabase
import com.example.sg_mobiledata_statistics.data.remote.SingaporeGovDataAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMobileDataUsageDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MobileDataUsageDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideMobileDataUsageDao(
        database: MobileDataUsageDatabase
    ) = database.mobileDataUsageDao()

    @Singleton
    @Provides
    fun provideSingaporeGovDataApi(): SingaporeGovDataAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SingaporeGovDataAPI::class.java)
    }
}

















