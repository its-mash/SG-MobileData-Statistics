package com.example.sg_mobiledata_statistics.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.sg_mobiledata_statistics.R
import dagger.hilt.android.AndroidEntryPoint
import ir.drax.netwatch.NetWatch
import ir.drax.netwatch.cb.NetworkChangeReceiver_navigator
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MobileDataUsageViewModel by viewModels()
//    lateinit var viewModel:MobileDataUsageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.refreshMobileDataUsageStatus()
//        viewModel=ViewModelProvider(this).get(MobileDataUsageViewModel::class.java)

        viewModel.mobileDataUsageRecords.observe(this, Observer {
            msg.text=it.toString()
        })
        viewModel.mobileDataUsageRefreshStat.observe(this, Observer {
            status.text=it.toString()
        })
        NetWatch.builder(this)
            .setCallBack(object : NetworkChangeReceiver_navigator {
                override fun onConnected(source: Int) {
                    viewModel.refreshMobileDataUsageStatus()
                }

                override fun onDisconnected() {
                    msg.text="Not Connected"
                }
            })
            .build()
    }
}