package bose.ankush.reposnews.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.databinding.LayoutNewsListItemBinding

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

class NewsAdapter(
    private val itemContainerClickListener: (news: NewsEntity?) -> Unit,
    private val itemBookmarkClickListener: (news: NewsEntity?) -> Unit,
    private val itemShareClickListener: (newsId: NewsEntity?) -> Unit
) : PagingDataAdapter<NewsEntity, NewsViewHolder>(NewsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutNewsListItemBinding.inflate(layoutInflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsEntityItem = getItem(position)
        holder.bind(
            newsEntityItem,
            itemContainerClickListener,
            itemBookmarkClickListener,
            itemShareClickListener
        )
    }
}