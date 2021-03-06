
package com.example.sg_mobiledata_statistics.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sg_mobiledata_statistics.R
import com.example.sg_mobiledata_statistics.data.local.YearlyMobileDataUsageRecord
import kotlinx.android.synthetic.main.record_row.view.*

class MobileDataUsageListAdapter(private val yearlyMobileDataUsageRecords: List<YearlyMobileDataUsageRecord>) : RecyclerView.Adapter<MobileDataUsageListAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.record_row, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if(position!=itemCount-1 && yearlyMobileDataUsageRecords[position].totalVolume<yearlyMobileDataUsageRecords[position+1].totalVolume)
      holder.bindRepo(yearlyMobileDataUsageRecords[position],true)
    else
      holder.bindRepo(yearlyMobileDataUsageRecords[position])

  }

  override fun getItemCount(): Int = yearlyMobileDataUsageRecords.size

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindRepo(record: YearlyMobileDataUsageRecord,decreaseIcon:Boolean=false ) {
      with(record) {
        itemView.year.text = record.year.toString()
        itemView.volume.text= record.totalVolume.toString()
        if(decreaseIcon)
          itemView.decrease_icon.visibility=View.VISIBLE
        else
          itemView.decrease_icon.visibility=View.INVISIBLE
      }
    }
  }
}