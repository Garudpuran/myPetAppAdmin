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

class ServiceProviderGalleryRcViewAdapter(
    private val context: Context,
    private val dataset: List<DashboardBillBoardModel>
) : RecyclerView.Adapter<ServiceProviderGalleryRcViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.servicesImgView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_provider_gallery_rcview_sample_item, parent, false)
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