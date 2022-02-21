package bose.ankush.reposnews.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import bose.ankush.reposnews.data.local.Article
import bose.ankush.reposnews.databinding.LayoutTopHeadlinesItemBinding

class TopHeadlinesAdapter(
    private val itemContainerClickListener: (article: Article) -> Unit,
) : ListAdapter<Article, TopHeadlinesViewHolder>(TopHeadlinesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopHeadlinesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutTopHeadlinesItemBinding.inflate(layoutInflater, parent, false)
        return TopHeadlinesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopHeadlinesViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, itemContainerClickListener)
    }

}