package ru.gb.map.ui.markers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.map.databinding.ItemRecyclerBinding
import ru.gb.map.entity.PlaceMark

class Adapter(
    private val deleteListener: OnDeleteClickListener,
    private val itemListener: OnItemViewClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    fun interface OnDeleteClickListener {
        fun onClick(placeMark: PlaceMark)
    }

    fun interface OnItemViewClickListener {
        fun onClick(position: Int)
    }

    private var dataList: List<PlaceMark> = listOf()

    fun setData(list: List<PlaceMark>) {
        dataList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRecyclerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener { itemListener.onClick(position) }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mark: PlaceMark) {
            binding.delete.setOnClickListener { deleteListener.onClick(mark) }
            binding.title.text = mark.name
            binding.description.text = mark.description
            binding.latitude.text = "Latitude - ${mark.markMapObject.geometry.latitude}"
            binding.longitude.text = "Longitude - ${mark.markMapObject.geometry.latitude}"
        }
    }
}