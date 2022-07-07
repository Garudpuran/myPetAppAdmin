package com.garudpuran.pawpawadm.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.garudpuran.pawpawadm.R

class AddServiceProviderGalleryRcViewAdapter(
    private val dataset: MutableList<Uri>?
) : RecyclerView.Adapter<AddServiceProviderGalleryRcViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.servicesImgView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_provider_gallery_rcview_sample_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset?.get(position)
        holder.imgView.setImageURI(item)
    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }
}