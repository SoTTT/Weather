package com.example.weather.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.databinding.FragmentPlaceBinding
import com.example.weather.ui.weather.WeatherActivity


class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding
        get() = _binding!!

    @Suppress("MemberVisibilityCanBePrivate")
    val viewModel by lazy {
        ViewModelProvider(this)[PlaceViewModel::class.java]
    }

    private lateinit var adapter: GaoDePlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.isPlaceSaved() && activity is MainActivity) {
            val place = viewModel.getSavedGaoDePlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.lng)
                putExtra("location_lat", place.lat)
                putExtra("place_name", place.city)
                putExtra("place_adcode", place.adcode)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        //将View和它对应的Adapter联系起来
        adapter = GaoDePlaceAdapter(this, viewModel.gaoDePlaceList)
        binding.recyclerView.adapter = adapter
        binding.searchPlaceEdit.addTextChangedListener {
            val context = it.toString()
            if (context.isNotEmpty()) {
                viewModel.searchPlaces(context)
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.gaoDePlaceList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        //观察viewModel中的LiveData并处理观察的结果
        viewModel.gaoDePlaceLiveData.observe(this as LifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.gaoDePlaceList.clear()
                //将place中包含的结果（一个List<Place>类型的对象）的内容添加到viewModel中
                viewModel.gaoDePlaceList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.no_search_result, Toast.LENGTH_LONG)
                    .show()
                result.exceptionOrNull()?.printStackTrace()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}