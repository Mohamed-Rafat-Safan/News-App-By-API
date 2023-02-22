package com.mohamedrafat.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mohamedrafat.newsapp.databinding.FragmentArticleBinding
import com.mohamedrafat.newsapp.ui.MainActivity
import com.mohamedrafat.newsapp.ui.NewsViewModel

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel

    private val args:ArticleFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel


        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.fab.setOnClickListener {
//           val a = Article(5,null ,"content","description","publishedAt", Source("id","name"),"title","url","urlImage")
            viewModel.saveArticle(article)

            Snackbar.make(it , "Article Saved Successfully",Snackbar.LENGTH_LONG).show()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticleBinding.inflate(inflater, container, false)



        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}