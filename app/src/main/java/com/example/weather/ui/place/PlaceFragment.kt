package com.example.weather.ui.place

import android.annotation.SuppressLint
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
import com.example.weather.R
import com.example.weather.databinding.FragmentPlaceBinding


class PlaceFragment : Fragment() {

    private lateinit var binding: FragmentPlaceBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        //将View和它对应的Adapter联系起来
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.adapter = adapter
        binding.searchPlaceEdit.addTextChangedListener {
            val context = it.toString()
            if (context.isNotEmpty()) {
                viewModel.searchPlaces(context)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        //观察viewModel中的LiveData并处理观察的结果
        viewModel.placeLiveData.observe(this as LifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                //将place中包含的结果（一个List<Place>类型的对象）的内容添加到viewModel中
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.no_search_result, Toast.LENGTH_LONG)
                    .show()
                result.exceptionOrNull()?.printStackTrace()
            }

        })
    }

}