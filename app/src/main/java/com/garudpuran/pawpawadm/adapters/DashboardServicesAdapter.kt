package com.garudpuran.pawpawadm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.garudpuran.pawpawadm.fragments.DashboardFragmentDirections
import com.garudpuran.pawpawadm.models.DashboardServiceModel


class DashboardServicesAdapter(
    private val context: Context,
    private val dataset: MutableList<DashboardServiceModel>
) : RecyclerView.Adapter<DashboardServicesAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceImg: ImageView = view.findViewById(R.id.servicesImgView)
        val serviceTxt:TextView = view.findViewById(R.id.services_title_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_services_single_view_sample_rc, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(context).load(item.imgDownloadUrl)
            .into(holder.serviceImg)
        holder.serviceTxt.text = item.title
        holder.itemView.setOnClickListener {
           val action = DashboardFragmentDirections.actionDashboardFragmentToServicesFragment(holder.serviceTxt.text.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}