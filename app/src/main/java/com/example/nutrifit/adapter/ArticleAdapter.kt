package com.example.nutrifit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.data.Article
import com.example.nutrifit.databinding.ArticleItemBinding

class ArticleAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(private val binding: ArticleItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.titleArticle.text = article.title
            binding.descArticle.text = article.description
            binding.imageArticle.setImageResource(article.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }
}