package bose.ankush.reposnews.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import bose.ankush.reposnews.R
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.local.model.Article
import bose.ankush.reposnews.databinding.FragmentHomeBinding
import bose.ankush.reposnews.util.ResultData
import bose.ankush.reposnews.util.greetingMessage
import bose.ankush.reposnews.util.shareNews
import bose.ankush.reposnews.util.toCelsius
import bose.ankush.reposnews.view.adapter.NewsAdapter
import bose.ankush.reposnews.view.adapter.TopHeadlinesAdapter
import bose.ankush.reposnews.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@AndroidEntryPoint
class FragmentHome : Fragment(R.layout.fragment_home) {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fragmentIncludedNewsLayout?.fragmentNewsSwipeRefreshContainer
            ?.setOnRefreshListener { viewModel.updateFreshNewsFromRemote() }

        binding?.fragmentNewsIncludedLayoutHeading?.layoutHeadingTvGreeting?.text =
            greetingMessage()

        setWeatherDataOnUi()
        setDataOnTopHeadlineArticleRecyclerView()
        setDataOnNewsRecyclerView()
    }


    @SuppressLint("SetTextI18n")
    private fun setWeatherDataOnUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.weatherFlow.collectLatest { result ->
                binding?.fragmentIncludedWeatherLayout?.apply {
                    when (result) {
                        is ResultData.Loading -> this.layoutCurrentWeatherCity.text =
                            "Loading..."
                        is ResultData.Success -> {

                            this.layoutCurrentWeatherTemp.text =
                                "${result.data?.main?.temp?.toCelsius()}°C"
                            this.layoutCurrentWeatherCity.text = result.data?.name.toString()
                        }
                        is ResultData.Failed -> this.layoutCurrentWeatherCity.text = "Failed!"
                        else -> {}
                    }
                }
            }
        }
    }


    private fun setDataOnTopHeadlineArticleRecyclerView() {
        val topHeadlinesAdapter = TopHeadlinesAdapter(::openTopHeadlineItemDetailsInBrowser)
        binding?.fragmentIncludedNewsLayoutTopHeadlines?.layoutTopHeadlinesRv?.adapter =
            topHeadlinesAdapter
        viewModel.topHeadlines.observe(viewLifecycleOwner) { response ->
            if (response is ResultData.Success && response.data != null)
                topHeadlinesAdapter.submitList(response.data)
            else topHeadlinesAdapter.submitList(emptyList())
        }
    }


    private fun setDataOnNewsRecyclerView() {
        val newsAdapter = NewsAdapter(::goToNewsDetailsScreen, ::bookmarkNewsItem, ::shareNewsItem)
        binding?.fragmentIncludedNewsLayout?.fragmentNewsRvNews?.adapter = newsAdapter
        viewModel.newsData.observe(viewLifecycleOwner) { response ->
            if (response is ResultData.Success && response.data != null)
                newsAdapter.submitList(response.data)
            else newsAdapter.submitList(emptyList())
        }
    }


    private fun goToNewsDetailsScreen(news: NewsEntity) {
        val action =
            FragmentHomeDirections.actionFragmentNewsToFragmentNewsDetails(news)
        requireView().findNavController().navigate(action)
    }


    private fun bookmarkNewsItem(news: NewsEntity) {
        viewModel.bookmarkNewsItem(news)
    }


    private fun shareNewsItem(news: NewsEntity) {
        this.shareNews(news)
    }

    private fun openTopHeadlineItemDetailsInBrowser(article: Article) {
        val newsUrl = article.url
        val openUrl = Intent(Intent.ACTION_VIEW)
        openUrl.data = Uri.parse(newsUrl)
        startActivity(openUrl)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}