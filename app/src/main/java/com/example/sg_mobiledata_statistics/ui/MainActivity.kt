package com.example.sg_mobiledata_statistics.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sg_mobiledata_statistics.R
import ir.drax.netwatch.NetWatch
import ir.drax.netwatch.cb.NetworkChangeReceiver_navigator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MobileDataUsageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewModel=ViewModelProvider(this).get(MobileDataUsageViewModel::class.java)
//        viewModel.mobileDataUsageRecords.observe(this, Observer {
//            msg.text=it.toString()
//        })
//        viewModel.refreshMobileDataUsageStatus()
        NetWatch.builder(this)
            .setCallBack(object : NetworkChangeReceiver_navigator {
                override fun onConnected(source: Int) {
//                    msg.text="Internet"
                }

                override fun onDisconnected() {
//                    msg.text="Not Connected"
                }
            })
            .build()
    }
}