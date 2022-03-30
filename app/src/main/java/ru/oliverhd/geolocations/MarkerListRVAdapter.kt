package ru.oliverhd.geolocations

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.oliverhd.geolocations.databinding.ItemMarkerBinding

class MarkerListRVAdapter : RecyclerView.Adapter<MarkerListRVAdapter.MarkerListViewHolder>() {

    private var markerList: MutableList<Marker> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerListViewHolder {
        val binding = ItemMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerListViewHolder(binding)
    }

    fun setList(markers: MutableList<Marker>) {
        markerList = markers
    }

    override fun onBindViewHolder(holder: MarkerListViewHolder, position: Int) {
        holder.bind(markerList[position])
    }

    override fun getItemCount(): Int {
        return markerList.size
    }

    inner class MarkerListViewHolder(private val binding: ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(marker: Marker) {
            binding.markerNameEditText.setText(marker.name)
            binding.markerNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    marker.name = p0.toString()
                }
            })
            binding.markerAnnotationEditText.setText(marker.annotation)
            binding.markerAnnotationEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    marker.annotation = p0.toString()
                }
            })
            binding.removeButton.setOnClickListener {
                markerList.remove(marker)
                notifyItemRemoved(layoutPosition)
            }
        }
    }
}