package bose.ankush.reposnews.view.adapter

import androidx.recyclerview.widget.DiffUtil
import bose.ankush.reposnews.data.model.Article

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

class TopHeadlinesDiffUtil : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(
        oldItem: Article,
        newItem: Article,
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(
        oldItem: Article,
        newItem: Article,
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}