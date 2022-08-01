package com.openclassrooms.realestatemanager.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.CancellationSignal
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.FileProvider
import coil.load
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.util.Utils.isAVideo
import com.openclassrooms.realestatemanager.util.Utils.isAnImage
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.overlay_imageview.view.*
import java.io.File

object ImageUtils {

    // Return a Bitmap from a Drawable object
    fun Drawable.toBitmap(): Bitmap? {
        if (this is BitmapDrawable) {
            return this.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
        this.draw(canvas)
        return bitmap
    }

    fun getThumbnailFromVideoUrl(path: String): Bitmap {
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(path)
        val height =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                ?.toInt()
        val width =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                ?.toInt()

        lateinit var mSize: Size
        if ((width != null) && (height != null)) mSize = Size(width, height)
        val ca = CancellationSignal()

        // Get a thumbnail from the video
        return ThumbnailUtils.createVideoThumbnail(File(path), mSize, ca)
    }

    fun openImageViewer(
        context: Context,
        previewList: List<ImageWithDescription>,
        startPosition: Int
    ): Int {
        val overlayView: View =
            LayoutInflater.from(context).inflate(R.layout.overlay_imageview, null, false)

        return StfalconImageViewer.Builder(context, previewList) { view, image ->
            // If this is an picture, then it is directly displayed
            // Else, it means that this is a video, and we generate a thumbnail to display
            if (image.imageUrl.isAnImage()) view.load(image.imageUrl)
            else view.load(getThumbnailFromVideoUrl(image.imageUrl))
        }.allowZooming(false)
            .withOverlayView(overlayView)
            .withImageChangeListener { position ->
                val myImage = previewList[position]
                if (myImage.imageUrl.isAVideo()) {
                    // Display an specific icon if the media is a video
                    // Set a clicklistener to open an intent that'll play the video
                    overlayView.overlay_imageview_is_a_video.load(R.drawable.ic_play)
                    overlayView.overlay_imageview_is_a_video.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(
                            FileProvider.getUriForFile(
                                context,
                                "${BuildConfig.APPLICATION_ID}.fileprovider",
                                File(myImage.imageUrl)
                            ), "video/mp4"
                        )
                        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        overlayView.context.startActivity(intent)
                    }
                }
                // If the media is an image, then no icon are displayed
                else overlayView.overlay_imageview_is_a_video.load(0x00000000)
                // Display the description of the media
                overlayView.overlay_imageview_description.text = previewList[position].description
            }
            .show()
            .setCurrentPosition(startPosition)
    }

}