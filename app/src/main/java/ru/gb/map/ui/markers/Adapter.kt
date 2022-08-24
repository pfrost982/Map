package ru.gb.map.ui.markers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.map.databinding.ItemRecyclerBinding
import ru.gb.map.entity.PlaceMarker

class Adapter(private var onItemViewClickListener: OnItemViewClickListener) :
RecyclerView.Adapter<Adapter.ViewHolder>() {

    fun interface OnItemViewClickListener {
        fun onItemViewClick(mark: PlaceMarker)
    }

    private var markerData: List<PlaceMarker> = listOf()

    fun setMarkerData(data: List<PlaceMarker>) {
        markerData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRecyclerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(markerData[position])

    override fun getItemCount(): Int = markerData.size

    inner class ViewHolder(private val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mark: PlaceMarker) {

                binding.title.text = mark.name
                binding.description.text = mark.description
                val latitudeItem = "LatitudeItem - ${mark.markMapObject.geometry.latitude}"
                val longitudeItem = "LongitudeItem - ${mark.markMapObject.geometry.latitude}"
                binding.latitude.text = latitudeItem
                binding.longitude.text = longitudeItem
        }
    }
}