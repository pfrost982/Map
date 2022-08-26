package ru.gb.map.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import ru.gb.map.ui.MainViewModel
import ru.gb.map.R
import ru.gb.map.ViewModelSaver
import ru.gb.map.databinding.DialogNewMarkBinding
import ru.gb.map.databinding.FragmentMapBinding
import ru.gb.map.entity.PlaceMark


class MapFragment : Fragment(), InputListener {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (requireActivity() as ViewModelSaver).getViewModel()
        MapKitFactory.initialize(requireContext())
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapview
        mapView.map.isRotateGesturesEnabled = false
        mapView.map.addInputListener(this)

        viewModel.liveDataPlaceMarkForDelete.observe(viewLifecycleOwner) {
            binding.mapview.map.mapObjects.remove(it.markMapObject)
        }

        viewModel.liveDataPlaceMarkForUpdate.observe(viewLifecycleOwner) {
            updatePlaceMark(it)
        }

        requestLocationPermission()

        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapview.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true

        mapView.viewTreeObserver.addOnGlobalLayoutListener {
            userLocationLayer.setAnchor(
                PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
                PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
            )
            mapView.map.move(
                CameraPosition(mapView.map.cameraPosition.target, 11.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2f),
                null
            )
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                "android.permission.ACCESS_FINE_LOCATION"
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                PERMISSIONS_REQUEST_FINE_LOCATION
            )
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapTap(p0: Map, p1: Point) {}

    override fun onMapLongTap(p0: Map, p1: Point) {
        createMark(p1)
    }

    private fun createMark(point: Point) {
        val dialogView = DialogNewMarkBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setView(dialogView.root)
        val inputName: EditText = dialogView.dialogInputName
        val inputDescription: EditText = dialogView.dialogInputDescription
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                val placeMarkMapObject = addPlaceMark(inputName.text.toString(), point)
                viewModel.addPlaceMark(
                    PlaceMark(
                        inputName.text.toString(),
                        inputDescription.text.toString(),
                        placeMarkMapObject
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun addPlaceMark(name: String, point: Point): PlacemarkMapObject {
        val placeMarkMapObject = binding.mapview.map.mapObjects.addPlacemark(point)
        placeMarkMapObject.addText(name)
        placeMarkMapObject.addIcon(requireActivity())
        return placeMarkMapObject
    }

    private fun updatePlaceMark(placeMark: PlaceMark) {
        placeMark.markMapObject.setText(placeMark.name)
    }

    companion object {
        const val PERMISSIONS_REQUEST_FINE_LOCATION = 1
    }
}

fun PlacemarkMapObject.addText(name: String) {
    val textStyle = TextStyle()
        .setSize(12F)
        .setColor(Color.BLACK)
        .setOutlineColor(Color.WHITE)
        .setPlacement(TextStyle.Placement.BOTTOM)
    this.setText(name, textStyle)
}

fun PlacemarkMapObject.addIcon(context: Context) {
    val imageProvider = ImageProvider.fromResource(context, R.drawable.ic_marker)
    this.setIcon(
        imageProvider, IconStyle()
            .setAnchor(PointF(0.5F, 1.0F))
            .setRotationType(RotationType.NO_ROTATION)
            .setScale(0.05F)
            .setFlat(false)
            .setZIndex(null)
    )
}
