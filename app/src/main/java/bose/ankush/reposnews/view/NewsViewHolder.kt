package bose.ankush.reposnews.view

import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.databinding.LayoutNewsListItemBinding

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

class NewsViewHolder(private var binding: LayoutNewsListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(newsEntityItem: NewsEntity) {
            binding.apply {
                news = newsEntityItem
                executePendingBindings()
            }
        }
}