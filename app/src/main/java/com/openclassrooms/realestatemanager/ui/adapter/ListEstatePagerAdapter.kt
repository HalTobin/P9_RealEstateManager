package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import com.openclassrooms.realestatemanager.model.Estate
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemListEstateBinding
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import java.util.ArrayList

class ListEstatePagerAdapter(items: List<Estate>?, context: Context, listener: OnItemClick) :
    RecyclerView.Adapter<ListEstatePagerAdapter.ViewHolder>() {

    private val context: Context
    private var estates: List<Estate>? = ArrayList()
    private val mCallback: OnItemClick?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myEstate = estates!![position]

        holder.binding.itemEstateName.text = myEstate.title
        holder.binding.itemEstateLocation.text = myEstate.city
        //TODO - Change currency dynamically
        holder.binding.itemEstatePrice.text = myEstate.priceDollar.toString().plus("€")

        if(myEstate.pictures != null) {
            if(myEstate.pictures!!.isNotEmpty()) {
                Glide.with(context)
                    .load(myEstate.pictures!![0].imageUrl)
                    .into(holder.binding.itemEstateImage)
            }
        }

        // Set up the onClickListener to open the EstateDetailsActivity
        holder.itemView.setOnClickListener { mCallback!!.onClick(myEstate.id) }
    }

    override fun getItemCount(): Int {
        return if (estates != null) estates!!.size else 0
    }

    fun updateList(estates: List<Estate>) {
        this.estates = estates
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemListEstateBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    init {
        estates = items
        this.context = context
        this.mCallback = listener
    }

    interface OnItemClick {
        fun onClick(estateId: Long)
    }
}