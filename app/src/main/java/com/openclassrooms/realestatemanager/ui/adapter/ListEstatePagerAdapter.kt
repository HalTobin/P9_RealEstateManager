package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemListEstateBinding
import com.openclassrooms.realestatemanager.model.EstateWithImages
import com.openclassrooms.realestatemanager.util.Utils.fromDollarToEuro
import java.util.ArrayList

class ListEstatePagerAdapter(items: List<EstateWithImages>?, isDollar: Boolean, context: Context, listener: OnItemClick) :
    RecyclerView.Adapter<ListEstatePagerAdapter.ViewHolder>() {

    private val context: Context
    private var isDollar = false
    private var estates: List<EstateWithImages>? = ArrayList()
    private val mCallback: OnItemClick?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myEstate = estates!![position]

        holder.binding.itemEstateName.text = myEstate.estate.title
        holder.binding.itemEstateLocation.text = myEstate.estate.city
        holder.binding.itemEstatePrice.text =
            if(isDollar) myEstate.estate.priceDollar?.toString().plus("$")
            else myEstate.estate.priceDollar?.fromDollarToEuro().toString().plus("€")

        if(myEstate.images!!.isNotEmpty()) {
            holder.binding.itemEstateImage.load(myEstate.images!![0].imageUrl)
        }

        // Set up the onClickListener to open the EstateDetailsActivity
        holder.itemView.setOnClickListener { mCallback!!.onClick(myEstate.estate.id!!) }
        if(myEstate.estate.sold) { holder.binding.itemEstateSold.load(R.drawable.sold) }
    }

    override fun getItemCount(): Int {
        return if (estates != null) estates!!.size else 0
    }

    fun updateCurrency(isDollar: Boolean) {
        this.isDollar = isDollar
        notifyDataSetChanged()
    }

    fun updateList(estates: List<EstateWithImages>) {
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

    interface OnItemClick {
        fun onClick(estateId: Int)
    }
}