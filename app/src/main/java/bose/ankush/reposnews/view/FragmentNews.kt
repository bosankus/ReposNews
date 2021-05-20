package bose.ankush.reposnews.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import bose.ankush.reposnews.R
import bose.ankush.reposnews.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@AndroidEntryPoint
class FragmentNews : Fragment() {

    private var binding: FragmentNewsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }?.root
    }
}