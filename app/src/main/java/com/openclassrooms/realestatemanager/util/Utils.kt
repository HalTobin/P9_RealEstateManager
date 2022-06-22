package com.openclassrooms.realestatemanager.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.ConnectivityManager
import android.net.Uri
import android.os.CancellationSignal
import android.provider.OpenableColumns
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
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int = (dollars * 0.95).roundToInt()

    fun convertEuroToDollar(euros: Int): Int = (euros * 1.05).roundToInt()

    fun Int.fromEuroToDollar(): Int = convertEuroToDollar(this)

    fun Int.fromDollarToEuro(): Int = convertDollarToEuro(this)


    fun String.isValid(): Boolean = this != ""

    fun Uri.copyToInternal(context: Context) : File{
        var fileName = "IMG_".plus(System.currentTimeMillis())

        this.let { returnUri ->
            context.contentResolver.query(returnUri,null,null,null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }

        //  For extract file mimeType
        val fileType: String? = this.let { returnUri ->
            context.contentResolver.getType(returnUri)
        }

        val iStream : InputStream = context.contentResolver.openInputStream(this)!!
        val outputDir = File(context.filesDir.toPath().toString().plus("/Images/"))
        val outputFile = File(outputDir, fileName)
        copyStreamToFile(iStream, outputFile)
        iStream.close()
        return  outputFile
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    fun String.isAnImage(): Boolean = (this.getSuffix().lowercase() == ".jpg" || this.getSuffix().lowercase() == ".png")

    fun String.isAVideo(): Boolean = this.getSuffix().lowercase() == ".mp4"

    fun String.getSuffix(): String = this.removeRange(0, this.length-4)

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

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting

        /*val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled*/
    }

    fun fullAddress(address: String, zipCode: String?, city: String, country: String): String {
        return address.plus(", ").plus(zipCode).plus(" ").plus(city).plus(", ").plus(country)
    }

    fun openImageViewer(context: Context, previewList: List<ImageWithDescription>, startPosition: Int): Int {
        val overlayView: View = LayoutInflater.from(context).inflate(R.layout.overlay_imageview, null, false)

        return StfalconImageViewer.Builder(context, previewList) { view, image ->
            // If this is an picture, then it is directly displayed
            // Else, it means that this is a video, and we generate a thumbnail to display
            if (image.imageUrl.isAnImage()) view.load(image.imageUrl)
            else {
                val metaRetriever = MediaMetadataRetriever()
                metaRetriever.setDataSource(image.imageUrl)
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
                val thumbnail = ThumbnailUtils.createVideoThumbnail(File(image.imageUrl), mSize, ca)

                view.load(thumbnail)
            }
        }.allowZooming(false)
            .withOverlayView(overlayView)
            .withImageChangeListener { position ->
                val myImage = previewList[position]
                if(myImage.imageUrl.isAVideo()) {
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