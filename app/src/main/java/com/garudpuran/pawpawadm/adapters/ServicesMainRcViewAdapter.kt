package com.pawpaw.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.garudpuran.pawpawadm.R
import com.garudpuran.pawpawadm.fragments.ServicesFragmentDirections
import com.garudpuran.pawpawadm.models.ServiceModel

class ServicesMainRcViewAdapter(
    private val context: Context,
    private val dataset: List<ServiceModel>
) : RecyclerView.Adapter<ServicesMainRcViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.services_provider_profile_pic)
        val titleTv: TextView = view.findViewById(R.id.sales_title)
        val ratingsGot: RatingBar = view.findViewById(R.id.sales_got_ratings)
        val noOfRatings: TextView = view.findViewById(R.id.no_of_ratings_tv)
        val location: TextView = view.findViewById(R.id.provider_location_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.services_main_list_rc_sample, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.titleTv.text = item.title
        if(item.ratings != null){
            holder.ratingsGot.rating = item.ratings!!.toFloat()
        }
        if(item.noOfRatings != null){
            holder.noOfRatings.text = item.noOfRatings
        }
        holder.location.text = item.address
        Glide.with(context).load(item.serviceProfilePicUrl).into(holder.imgView)
        holder.itemView.setOnClickListener {
            val serviceId = dataset[holder.adapterPosition].serviceId
            val serviceType = dataset[holder.adapterPosition].serviceCategory
            val action = ServicesFragmentDirections.actionServicesFragmentToSelectedServicesDetailFragment(serviceId!!,serviceType!!)
            holder.itemView.findNavController().navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}