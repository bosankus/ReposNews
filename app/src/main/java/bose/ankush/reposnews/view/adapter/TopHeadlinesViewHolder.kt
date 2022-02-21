package bose.ankush.reposnews.view.adapter

import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.data.local.Article
import bose.ankush.reposnews.databinding.LayoutTopHeadlinesItemBinding

class TopHeadlinesViewHolder(
    private val binding: LayoutTopHeadlinesItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        article: Article,
        itemClickListener: (article: Article) -> Unit
    ) {
        binding.apply {
            articles = article
            layoutTopHeadlinesContainer.setOnClickListener { itemClickListener(article) }
            executePendingBindings()
        }
    }
}