package bose.ankush.reposnews.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import bose.ankush.reposnews.R
import bose.ankush.reposnews.databinding.FragmentNewsDetailsBinding

/**Created by
Author: Ankush Bose
Date: 21,May,2021
 **/

class FragmentNewsDetails : Fragment() {

    private var binding: FragmentNewsDetailsBinding? = null

    private val newsDetailsArgs by lazy {
        FragmentNewsDetailsArgs.fromBundle(requireArguments()).selectedNews
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news_details, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            news = newsDetailsArgs
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}