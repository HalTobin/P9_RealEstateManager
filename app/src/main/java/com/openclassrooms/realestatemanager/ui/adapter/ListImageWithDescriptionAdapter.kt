package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.databinding.ItemListImagesWithDescriptionBinding
import com.openclassrooms.realestatemanager.model.ImageWithDescription

class ListImageWithDescriptionAdapter(items: List<ImageWithDescription>?, context: Context, listener: OnItemClick) :
    RecyclerView.Adapter<ListImageWithDescriptionAdapter.ViewHolder>() {

    private val context: Context
    private var images: List<ImageWithDescription>? = ArrayList()
    private val mCallback: OnItemClick?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListImagesWithDescriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListImageWithDescriptionAdapter.ViewHolder, position: Int) {
        val myImage = images!![position]

        holder.binding.imageWithDescriptionDescription.text = myImage.description

        if(myImage.imageUrl.isNotEmpty()) {
            holder.binding.imageWithDescriptionImage.load(myImage.imageUrl)
        }

        // Set up the onClickListener to open the RestaurantDetailsActivity
        holder.itemView.setOnClickListener { mCallback!!.onClick(myImage) }
    }

    override fun getItemCount(): Int {
        return if (images != null) images!!.size else 0
    }

    fun updateList(images: List<ImageWithDescription>) {
        this.images = images
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemListImagesWithDescriptionBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    init {
        images = items
        this.context = context
        this.mCallback = listener
    }

    interface OnItemClick {
        fun onClick(imageWithDescription: ImageWithDescription)
    }
}