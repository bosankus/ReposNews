package com.bosankus.reposnews.weather_feature.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bosankus.reposnews.weather_feature.R
import com.bosankus.reposnews.weather_feature.databinding.FragmentWeatherDetailsBinding


class WeatherDetailsFragment : Fragment() {

    private var binding: FragmentWeatherDetailsBinding? = null
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather_details, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }?.root
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}