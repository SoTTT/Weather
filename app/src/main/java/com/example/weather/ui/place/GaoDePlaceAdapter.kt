package com.example.weather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.logic.model.GaoDePlaceResponse
import com.example.weather.ui.weather.WeatherActivity

class GaoDePlaceAdapter(
    private val fragment: PlaceFragment,
    private val placeList: List<GaoDePlaceResponse.Place>
) : RecyclerView.Adapter<GaoDePlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.binding.drawerLayout.closeDrawers()
                activity.viewModel.placeName = place.city
                activity.viewModel.adcode = place.adcode
                activity.refreshWeather()
            } else {
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("place_adcode", place.adcode)
                    putExtra("place_name", place.city)
                }
                fragment.viewModel.saveGaoDePlace(place)
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.viewModel.saveGaoDePlace(place)
        }
        return holder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.city
        holder.placeAddress.text = place.address
    }

}