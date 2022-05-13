package bose.ankush.reposnews.view.fragment

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
import androidx.navigation.fragment.findNavController
import bose.ankush.reposnews.R
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.model.Article
import bose.ankush.reposnews.databinding.FragmentHomeBinding
import bose.ankush.reposnews.util.*
import bose.ankush.reposnews.view.adapter.NewsAdapter
import bose.ankush.reposnews.view.adapter.NewsLoadStateAdapter
import bose.ankush.reposnews.view.adapter.TopHeadlinesAdapter
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
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
    private lateinit var splitInstaller: SplitInstallManager
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var topHeadlinesAdapter: TopHeadlinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        splitInstaller = SplitInstallManagerFactory.create(requireContext())

        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fragmentNewsIncludedLayoutHeading?.layoutHeadingTvGreeting?.text =
            greetingMessage()

        topHeadlinesAdapter = TopHeadlinesAdapter(::openTopHeadlineItemDetailsInBrowser)
        newsAdapter = NewsAdapter(::goToNewsDetailsScreen, ::bookmarkNewsItem, ::shareNewsItem)

        setDataOnTopHeadlineArticleRecyclerView()
        setPagingNewsItemsOnRecyclerView()
        setClickListeners()
    }


    private fun setClickListeners() {
        // navigation to dynamic feature module 'weather_feature'
        binding?.fragmentIncludedWeatherLayout?.layoutCurrentWeatherContainer?.setOnClickListener {
            requestDFMInstall()
        }
    }


    private fun setDataOnTopHeadlineArticleRecyclerView() {
        binding?.fragmentIncludedNewsLayoutTopHeadlines?.layoutTopHeadlinesRv?.adapter =
            topHeadlinesAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.topHeadlines.collectLatest { response ->
                if (response is ResultData.Success && response.data != null)
                    topHeadlinesAdapter.submitList(response.data)
                else topHeadlinesAdapter.submitList(emptyList())
            }
        }
    }


    private fun setPagingNewsItemsOnRecyclerView() {
        binding?.fragmentIncludedNewsLayout?.fragmentNewsRvNews?.apply {
            addItemDecoration(RecyclerViewItemDecoration())
            adapter =
                newsAdapter.withLoadStateFooter(footer = NewsLoadStateAdapter { newsAdapter.retry() })
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getNews().collectLatest { response ->
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, response)
            }
        }
    }


    private fun goToNewsDetailsScreen(news: NewsEntity?) {
        news?.let {
            val action =
                FragmentHomeDirections.actionFragmentNewsToFragmentNewsDetails(it)
            requireView().findNavController().navigate(action)
        }
    }


    private fun bookmarkNewsItem(news: NewsEntity?) {
        viewModel.bookmarkNewsItem(news)
    }


    private fun shareNewsItem(news: NewsEntity?) {
        news?.let { this.shareNews(it) }
    }


    private fun openTopHeadlineItemDetailsInBrowser(article: Article) {
        val newsUrl = article.url
        val openUrl = Intent(Intent.ACTION_VIEW)
        openUrl.data = Uri.parse(newsUrl)
        startActivity(openUrl)
    }


    private var stateListener: SplitInstallStateUpdatedListener = SplitInstallStateUpdatedListener {
        when (it.status()) {
            SplitInstallSessionStatus.CANCELED -> logMessage("Installation cancelled")
            SplitInstallSessionStatus.FAILED -> logMessage("${it.errorCode()}")
            SplitInstallSessionStatus.INSTALLED -> {
                logMessage("Successfully installed")
                findNavController().navigate(R.id.action_fragmentNews_to_weatherFeatureGraph)
            }
            else -> logMessage("Something went wrong")
        }
    }


    override fun onResume() {
        super.onResume()
        splitInstaller.registerListener(stateListener)
    }


    override fun onPause() {
        super.onPause()
        splitInstaller.unregisterListener(stateListener)
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


    private fun requestDFMInstall() {
        val splitInstallRequest = SplitInstallRequest.newBuilder()
            .addModule("weather_feature")
            .build()

        splitInstaller.startInstall(splitInstallRequest)
            .addOnFailureListener { logMessage(it.message.toString()) }
    }
}