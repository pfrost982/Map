package ru.gb.map.ui.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import ru.gb.map.PlaceMarker
import ru.gb.map.R
import ru.gb.map.databinding.FragmentMapBinding
import ru.gb.map.ui.MainViewModel


class MapFragment : Fragment(), InputListener {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this)[MainViewModel::class.java]

        MapKitFactory.initialize(requireContext())
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mapViewModel.text.observe(viewLifecycleOwner) {}
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapview
        mapView.map.isRotateGesturesEnabled = false

        mapView.map.addInputListener(this)

        //binding.mapview.map.mapObjects.remove(placeMark)
        requestLocationPermission()
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapview.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true

        mapView.viewTreeObserver.addOnGlobalLayoutListener {
            userLocationLayer.setAnchor(
                PointF((binding.root.width * 0.5).toFloat(), (binding.root.height * 0.5).toFloat()),
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

    private fun addPlaceMarker(marker: PlaceMarker): PlacemarkMapObject {
        val placeMark = binding.mapview.map.mapObjects.addPlacemark(marker.point)
        val textStyle = TextStyle()
            .setSize(12F)
            .setColor(Color.BLACK)
            .setOutlineColor(Color.WHITE)
            .setPlacement(TextStyle.Placement.BOTTOM)
        placeMark.setText(marker.name, textStyle)

        val imageProvider = ImageProvider.fromResource(requireActivity(), R.drawable.ic_marker)
        placeMark.setIcon(
            imageProvider, IconStyle()
                .setAnchor(PointF(0.5F, 1.0F))
                .setRotationType(RotationType.NO_ROTATION)
                .setScale(0.05F)
                .setFlat(false)
                .setZIndex(null)
        )
        return placeMark
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

    companion object {
        const val PERMISSIONS_REQUEST_FINE_LOCATION = 1
    }

    override fun onMapTap(p0: Map, p1: Point) {}

    override fun onMapLongTap(p0: Map, p1: Point) {
        addPlaceMarker(PlaceMarker("Marker", "Description", p1))
    }
}