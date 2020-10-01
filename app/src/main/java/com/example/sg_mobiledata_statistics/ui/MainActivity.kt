package com.example.sg_mobiledata_statistics.ui

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sg_mobiledata_statistics.R
import com.example.sg_mobiledata_statistics.ui.adapters.MobileDataUsageListAdapter
import dagger.hilt.android.AndroidEntryPoint
import ir.drax.netwatch.NetWatch
import ir.drax.netwatch.cb.NetworkChangeReceiver_navigator
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MobileDataUsageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rvDataUsage.layoutManager=LinearLayoutManager(this)


//        viewModel.refreshMobileDataUsageStatus()

        viewModel.yearlyMobileDataUsageRecords.observe(this, Observer {
            for(x in 0 until it.size-2) {
                it[x].decrease= it[x+1].totalVolume > it[x].totalVolume
            }
            rvDataUsage.adapter=MobileDataUsageListAdapter(it)
        })
        viewModel.mobileDataUsageRefreshStat.observe(this, Observer {
            val resource=it.getContentIfNotHandled()
//            status.text=resource?.status.toString()+resource?.message.toString()
        })
        NetWatch.builder(this)
            .setCallBack(object : NetworkChangeReceiver_navigator {
                override fun onConnected(source: Int) {
                    status_text.text=getString(R.string.onlineMessage)
                    status_icon.setBackgroundColor(resources.getColor(R.color.online))
                    viewModel.refreshMobileDataUsageStatus()
                }

                override fun onDisconnected() {
                    status_text.text=getString(R.string.offlineMessage)
                    status_icon.setBackgroundColor(resources.getColor(R.color.offline))
                }
            })
            .build()
    }
}