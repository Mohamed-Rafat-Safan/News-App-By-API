package com.mohamedrafat.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamedrafat.newsapp.databinding.ItemArticlePreviewBinding
import com.mohamedrafat.newsapp.models.Article

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    // هذا المتغير باستخدمه بدل معمل list فوق ده افضل في حاجات كتيير
    // انه مش بيوقف main thread علشان يشتغل لا دا بيشتغل في ال background thread
    // وبيمشي انه بيقارن ال list القديمه مع ال list الجديده الي جايهويشوف حصل تغيير ولا لأ
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        with(holder){
            Glide.with(binding.root).load(article.urlToImage).into(binding.ivArticleImage)
            binding.tvSource.text = article.source.name
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvPublishedAt.text = article.publishedAt

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class ArticleViewHolder(val binding:ItemArticlePreviewBinding ) :RecyclerView.ViewHolder(binding.root) {

    }


    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener : (Article)-> Unit){
        onItemClickListener = listener
    }


}