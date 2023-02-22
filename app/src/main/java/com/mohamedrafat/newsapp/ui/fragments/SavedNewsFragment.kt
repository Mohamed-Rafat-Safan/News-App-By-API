package com.mohamedrafat.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mohamedrafat.newsapp.adapters.NewsAdapter
import com.mohamedrafat.newsapp.databinding.FragmentSavedNewsBinding
import com.mohamedrafat.newsapp.ui.MainActivity
import com.mohamedrafat.newsapp.ui.NewsViewModel

class SavedNewsFragment : Fragment() {
    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mNavController = findNavController()
        viewModel = (activity as MainActivity).viewModel


        setUpRecyclerView()

        // هنا علشان لو انت ضغط علي اي خبر يفتحها في صفحه تانيه
        newsAdapter.setOnItemClickListener { article ->
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
            mNavController.navigate(action)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles->
            newsAdapter.differ.submitList(articles)
        })


        // هذا المتغير علشان لما احرك كل عنصر من مكانه سواء يمين او شمال
        // او فوق وتحت يتمسح من ال room db
        val itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN ,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(binding.root,"Successfully Deleted Article",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        // هنا pass المغير لل recyclerView
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)



        return binding.root
    }


    fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
