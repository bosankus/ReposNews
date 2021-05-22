package bose.ankush.reposnews.view.adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.databinding.LayoutNewsListItemBinding
import bose.ankush.reposnews.view.fragment.FragmentNewsDirections

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

class NewsViewHolder(private var binding: LayoutNewsListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(newsEntityItem: NewsEntity) {
        binding.apply {
            news = newsEntityItem
            layoutNewsListItemContainer.setOnClickListener {
                val action =
                    FragmentNewsDirections.actionFragmentNewsToFragmentNewsDetails(newsEntityItem)
                it.findNavController().navigate(action)
            }
            executePendingBindings()
        }
    }
}