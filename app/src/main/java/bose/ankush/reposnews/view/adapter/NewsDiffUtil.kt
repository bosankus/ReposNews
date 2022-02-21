package bose.ankush.reposnews.view.adapter

import androidx.recyclerview.widget.DiffUtil
import bose.ankush.reposnews.data.local.NewsEntity

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

class NewsDiffUtil : DiffUtil.ItemCallback<NewsEntity>() {
    override fun areItemsTheSame(
        oldItem: NewsEntity,
        newItem: NewsEntity,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: NewsEntity,
        newItem: NewsEntity,
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}