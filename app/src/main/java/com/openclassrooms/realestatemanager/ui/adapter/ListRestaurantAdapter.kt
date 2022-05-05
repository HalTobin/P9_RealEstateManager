package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import com.openclassrooms.realestatemanager.model.Estate
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.openclassrooms.realestatemanager.databinding.ItemListEstateBinding
import java.util.ArrayList

class ListEstateAdapter(items: List<Estate>?, context: Context) :
    RecyclerView.Adapter<ListEstateAdapter.ViewHolder>() {
    private val context: Context
    private var estates: List<Estate>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myEstate = estates!![position]

        holder.binding.itemEstateName.text = myEstate.title
        holder.binding.itemEstateLocation.text = myEstate.neighbor
        holder.binding.itemEstatePrice.text = myEstate.priceDollar.toString() + "€"

        //holder.binding.itemRestaurantName.setText(restaurant.getName());

        //holder.binding.itemRestaurantTypeAndAdress.setText(restaurant.getAddress());
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

    //private WorkmateViewModel workmateViewModel;
    init {
        estates = items
        this.context = context

        //this.workmateViewModel = WorkmateViewModel.getInstance();
    }
}