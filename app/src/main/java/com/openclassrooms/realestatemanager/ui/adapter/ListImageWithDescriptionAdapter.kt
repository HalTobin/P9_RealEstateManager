package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.os.CancellationSignal
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemListImagesWithDescriptionBinding
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.util.Utils.isAVideo
import com.openclassrooms.realestatemanager.util.Utils.isAnImage
import com.openclassrooms.realestatemanager.util.ImageUtils.toBitmap
import java.io.File


class ListImageWithDescriptionAdapter(
    items: List<ImageWithDescription>?,
    context: Context,
    listener: OnItemClick
) :
    RecyclerView.Adapter<ListImageWithDescriptionAdapter.ViewHolder>() {

    private val context: Context
    private var images: List<ImageWithDescription>? = ArrayList()
    private val mCallback: OnItemClick?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListImagesWithDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListImageWithDescriptionAdapter.ViewHolder,
        position: Int
    ) {
        val myImage = images!![position]

        holder.binding.imageWithDescriptionDescription.text = myImage.description

        if (myImage.imageUrl.isNotEmpty()) {
            if (myImage.imageUrl.isAnImage()) holder.binding.imageWithDescriptionImage.load(myImage.imageUrl)
            if (myImage.imageUrl.isAVideo()) {
                val mSize = Size(192, 192)
                val ca = CancellationSignal()

                // Get a thumbnail from the video
                val thumbnail =
                    ThumbnailUtils.createVideoThumbnail(File(myImage.imageUrl), mSize, ca)

                // Load the drawable resource "ic_play"
                val play: Drawable? = AppCompatResources.getDrawable(context, R.drawable.ic_play)

                val canvas = Canvas(thumbnail)
                // Draw "ic_play" on top of the thumbnail
                if (play != null) canvas.drawBitmap((play.toBitmap())!!, 20f, 50f, null)

                holder.binding.imageWithDescriptionImage.load(thumbnail)
            }
        }

        // Set up the onClickListener to open the RestaurantDetailsActivity
        holder.itemView.setOnClickListener { mCallback!!.onImageClick(myImage, images!!) }
    }

    override fun getItemCount(): Int {
        return if (images != null) images!!.size else 0
    }

    fun updateList(images: List<ImageWithDescription>) {
        this.images = images
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemListImagesWithDescriptionBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

    init {
        images = items
        this.context = context
        this.mCallback = listener
    }

    interface OnItemClick {
        fun onImageClick(
            imageWithDescription: ImageWithDescription,
            images: List<ImageWithDescription>
        )
    }
}