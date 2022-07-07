package com.garudpuran.pawpawadm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.garudpuran.pawpawadm.models.DashboardBillBoardModel
import com.garudpuran.pawpawadm.R

class DashboardBillboardAdapter(
    private val context: Context,
    private val dataset: List<DashboardBillBoardModel>
) : RecyclerView.Adapter<DashboardBillboardAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.billBoardImgView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_billboard_layout_rc, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(context).load(item.ImgResourceId).into(holder.imgView)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}