package bose.ankush.reposnews.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bose.ankush.reposnews.R
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.databinding.FragmentNewsBinding
import bose.ankush.reposnews.util.ResultData
import bose.ankush.reposnews.util.shareNews
import bose.ankush.reposnews.view.adapter.NewsAdapter
import bose.ankush.reposnews.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@AndroidEntryPoint
class FragmentNews : Fragment(R.layout.fragment_news) {

    private var binding: FragmentNewsBinding? = null
    private val viewModel: NewsViewModel by viewModels()

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

        binding?.fragmentIncludedNewsLayout?.fragmentNewsSwipeRefreshContainer
            ?.setOnRefreshListener { viewModel.updateFreshNewsFromRemote() }

        setDataOnNewsRecyclerView()
    }


    private fun setDataOnNewsRecyclerView() {
        val newsAdapter = NewsAdapter(::bookmarkNewsItem, ::shareNewsItem)
        binding?.fragmentIncludedNewsLayout?.fragmentNewsRvNews?.adapter = newsAdapter
        viewModel.newsData.observe(viewLifecycleOwner) { response ->
            if (response is ResultData.Success && response.data != null) newsAdapter.submitList(
                response.data
            )
            else newsAdapter.submitList(emptyList())
        }
    }


    private fun bookmarkNewsItem(news: NewsEntity) {
        viewModel.bookmarkNewsItem(news)
    }


    private fun shareNewsItem(news: NewsEntity) {
        // TODO: Share news item implementation
        this.shareNews(news)
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}