package bose.ankush.reposnews.view.adapter

import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.databinding.LayoutNewsListItemBinding

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

class NewsViewHolder(private var binding: LayoutNewsListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        newsEntityItem: NewsEntity?,
        itemContainerClickListener: (news: NewsEntity?) -> Unit,
        itemBookmarkClickListener: (news: NewsEntity?) -> Unit,
        itemShareClickListener: (newsId: NewsEntity?) -> Unit,
    ) {
        binding.apply {
            news = newsEntityItem
            layoutNewsListItemContainer.setOnClickListener {
                itemContainerClickListener(newsEntityItem)
            }
            layoutNewsListItemImgBookmark.setOnClickListener {
                itemBookmarkClickListener(newsEntityItem)
            }
            layoutNewsListItemImgShare.setOnClickListener { itemShareClickListener(newsEntityItem) }
            executePendingBindings()
        }
    }
}