package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.CancellationSignal
import android.util.Size
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemListEstateBinding
import com.openclassrooms.realestatemanager.model.EstateUI
import com.openclassrooms.realestatemanager.util.ImageUtils.getThumbnailFromVideoUrl
import com.openclassrooms.realestatemanager.util.Utils.fromDollarToEuro
import com.openclassrooms.realestatemanager.util.Utils.isAnImage
import java.io.File
import java.util.ArrayList

class ListEstatePagerAdapter(
    items: List<EstateUI>?,
    isDollar: Boolean,
    context: Context,
    listener: OnItemClick
) :
    RecyclerView.Adapter<ListEstatePagerAdapter.ViewHolder>() {

    private val context: Context
    private var isDollar = false
    private var estates: List<EstateUI>? = ArrayList()
    private val mCallback: OnItemClick?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myEstate = estates!![position]

        holder.binding.itemEstateName.text = myEstate.estate.title
        holder.binding.itemEstateLocation.text = myEstate.estate.city

        // Displays the price of the estate as EURO or as DOLLAR depending on the value 'isDollar'
        holder.binding.itemEstatePrice.text =
            if (isDollar) myEstate.estate.priceDollar?.toString().plus("$")
            else myEstate.estate.priceDollar?.fromDollarToEuro().toString().plus("€")

        if (myEstate.images.isNotEmpty()) {
            val myResUrl = myEstate.images[0].imageUrl
            if (myResUrl.isAnImage()) holder.binding.itemEstateImage.load(myResUrl)
            else holder.binding.itemEstateImage.load(getThumbnailFromVideoUrl(myResUrl))
        }
        else holder.binding.itemEstateImage.load(R.drawable.img_no_photo)

        // Set up the onClickListener to open the EstateDetailsActivity
        holder.itemView.setOnClickListener { mCallback!!.onClick(myEstate.estate.id!!) }

        // Displayed a "SOLD" image only if the estate has been sold
        if (myEstate.estate.sold == true) holder.binding.itemEstateSold.load(R.drawable.sold)
        else holder.binding.itemEstateSold.load(0x00000000)
    }

    override fun getItemCount(): Int {
        return if (estates != null) estates!!.size else 0
    }

    // Invert the displayed currency
    fun updateCurrency(isDollar: Boolean) {
        this.isDollar = isDollar
        notifyDataSetChanged()
    }

    fun updateList(estates: List<EstateUI>) {
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
        this.isDollar = isDollar
    }

    // Interface used to notify the parent of a click
    interface OnItemClick {
        fun onClick(estateId: Int)
    }
}