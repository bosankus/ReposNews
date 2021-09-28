package bose.ankush.reposnews.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bose.ankush.reposnews.R
import bose.ankush.reposnews.databinding.FragmentNewsBinding
import bose.ankush.reposnews.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@AndroidEntryPoint
class FragmentNews : Fragment(R.layout.fragment_news) {

    private var binding: FragmentNewsBinding? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fragmentNewsSwipeRefreshContainer?.setOnRefreshListener {
            viewModel.updateNewsFromRemote()
        }

        viewModel.getNews()
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}