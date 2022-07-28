package com.openclassrooms.realestatemanager.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailsBinding
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate.Companion.getEstateTypeFromInt
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.activity.MainActivity.Companion.navigateToAddEditActivity
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.util.ImageUtils.openImageViewer
import com.openclassrooms.realestatemanager.util.MapUtils
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailsFragment : BaseFragment<FragmentEstateDetailsBinding>(), OnMapReadyCallback,
    ListImageWithDescriptionAdapter.OnItemClick {

    private var mAdapter: ListImageWithDescriptionAdapter? = null

    private val mainViewModel: MainViewModel by sharedViewModel()

    private var map: GoogleMap? = null
    private var marker: Marker? = null

    private var options = arrayOf<CharSequence>("", "")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateDetailsBinding.inflate(layoutInflater)

        options[1] = getString(R.string.estate_details_edit_estate_button_edit)
        initMapView(savedInstanceState)
        initRecycler()

        return binding!!.root
    }

    private fun setUpListenersAndObservers() {
        mainViewModel.estate.observe(viewLifecycleOwner) { estate ->
            refreshList(estate.images)
            binding?.estateDetailsTypeAndName?.text =
                getEstateTypeFromInt(requireContext(), estate.estate.type!!).plus(" - ")
                    .plus(estate.estate.title)
            binding?.estateDetailsDescriptionText?.text = estate.estate.description
            binding?.estateDetailsArea?.text = estate.estate.area.toString().plus("m²")
            binding?.estateDetailsRooms?.text = estate.estate.nbRooms.toString()
            binding?.estateDetailsBedrooms?.text = estate.estate.nbBedrooms.toString()
            binding?.estateDetailsBathrooms?.text = estate.estate.nbBathrooms.toString()
            binding?.estateDetailsLocationAddress?.text = estate.estate.getAddressInPresentation()
            if (estate.estate.nearbyShop == true)
                binding?.estateDetailsIsShop?.setImageDrawable(
                    getDrawable(
                        requireContext(),
                        R.drawable.ic_done
                    )
                )
            else binding?.estateDetailsIsShop?.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_cross
                )
            )
            if (estate.estate.nearbySchool == true)
                binding?.estateDetailsIsSchool?.setImageDrawable(
                    getDrawable(
                        requireContext(),
                        R.drawable.ic_done
                    )
                )
            else binding?.estateDetailsIsSchool?.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_cross
                )
            )
            if (estate.estate.nearbyPark == true)
                binding?.estateDetailsIsPark?.setImageDrawable(
                    getDrawable(
                        requireContext(),
                        R.drawable.ic_done
                    )
                )
            else binding?.estateDetailsIsPark?.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_cross
                )
            )
            map!!.navigateTo(
                Coordinates(estate.estate.xCoordinate!!, estate.estate.yCoordinate!!),
                16f
            )
            if (estate.estate.sold!!) {
                binding?.estateDetailsIsSoldImage?.load(R.drawable.sold)
                options[0] = getString(R.string.estate_details_edit_estate_button_sold_to_unsold)
            } else {
                binding?.estateDetailsIsSoldImage?.load(0x00000000)
                options[0] = getString(R.string.estate_details_edit_estate_button_sold_to_sold)
            }
            if (marker != null) marker!!.remove()
            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(estate.estate.xCoordinate!!, estate.estate.yCoordinate!!))
            )?.let { marker = it }

            binding?.estateDetailsEditButton?.setOnClickListener {
                estate.estate.id?.let { it1 -> editDialog(it1) }
            }
        }

    }

    // Show a pop-up menu that asks to the user if he wants to edit the estate
    // or invert its 'soldState' value
    private fun editDialog(id: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_edit_estate_save_choose_image_title))
        builder.setItems(options) { _, item ->
            when (item) {
                UPDATE_SOLD_STATE -> mainViewModel.updateSoldState()
                EDIT_ESTATE -> navigateToAddEditActivity(requireActivity(), id)
            }
        }
        builder.show()
    }

    // Initialize the recycler that displays the list of ImageWithDescription associated to the estate
    private fun initRecycler() {
        mAdapter = ListImageWithDescriptionAdapter(ArrayList(), requireContext(), this)
        binding!!.estateDetailsImageList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    // Refresh the list of ImageWithDescription
    private fun refreshList(myList: List<ImageWithDescription>) {
        if (myList.isEmpty()) {
            binding?.estateDetailsNoImagesImg?.load(R.drawable.img_no_photo)
        } else {
            binding?.estateDetailsNoImagesImg?.load(0x00000000)
        }

        mAdapter!!.updateList(myList)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding?.apply {
            estateDetailsLiteMap.onCreate(savedInstanceState)
            estateDetailsLiteMap.getMapAsync(this@DetailsFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Depending on the phone's settings, dark mode is used for the map
        googleMap.setMapStyle(MapUtils.getMapStyle(requireContext()))
        setUpListenersAndObservers()
    }

    override fun onImageClick(
        imageWithDescription: ImageWithDescription,
        images: List<ImageWithDescription>
    ) {
        openImageViewer(requireContext(), images, images.indexOf(imageWithDescription))
    }

    companion object {
        const val UPDATE_SOLD_STATE = 0
        const val EDIT_ESTATE = 1
    }

}