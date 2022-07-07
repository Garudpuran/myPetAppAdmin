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
import com.garudpuran.pawpawadm.fragments.SalesFragmentDirections
import com.garudpuran.pawpawadm.models.SalesModel

class SalesMainRcViewAdapter(
    private val context: Context,
    private val dataset: List<SalesModel>
) : RecyclerView.Adapter<SalesMainRcViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.salesProfilePic)
        val titleTv: TextView = view.findViewById(R.id.sales_title)
        val ratingsGot: RatingBar = view.findViewById(R.id.sales_got_ratings)
        val noOfRatings: TextView = view.findViewById(R.id.no_of_ratings_tv)
        val postDate: TextView = view.findViewById(R.id.sale_post_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.sales_main_list_rc_sample, parent, false)
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
        holder.postDate.text = item.postDate
        Glide.with(context).load(item.salesProfilePicUrl).into(holder.imgView)
        holder.itemView.setOnClickListener {
            val salesId = dataset[holder.adapterPosition].salesId
            val salesType = dataset[holder.adapterPosition].salesCategory
            val action = SalesFragmentDirections.actionSalesFragmentToSalesDetailFragment(salesId!!,salesType!!)
            holder.itemView.findNavController().navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}