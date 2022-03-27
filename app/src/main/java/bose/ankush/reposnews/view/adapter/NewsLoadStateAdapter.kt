package bose.ankush.reposnews.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.databinding.LayoutNewsLoadingStateBinding

class NewsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<NewsLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(
        holder: NewsLoadStateAdapter.LoadStateViewHolder,
        loadState: LoadState
    ) {

        holder.binding.apply {
            when (loadState) {
                is LoadState.Loading -> {
                    pbMembersListState.isVisible = true
                    tvRetry.isVisible = false
                    tvErrorMsg.isVisible = false
                }
                is LoadState.Error -> {
                    pbMembersListState.isVisible = false
                    tvRetry.isVisible = true
                    tvErrorMsg.apply {
                        isVisible = true
                        text = loadState.error.message
                    }
                }
                else -> pbMembersListState.isVisible = true
            }

            tvRetry.setOnClickListener { retry.invoke() }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NewsLoadStateAdapter.LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutNewsLoadingStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    inner class LoadStateViewHolder(val binding: LayoutNewsLoadingStateBinding) :
        RecyclerView.ViewHolder(binding.root)
}