package com.example.reuniteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchAdapter = SearchAdapter(listOf())
        binding.searchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        binding.searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performSearch(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it)
                }
                return false
            }
        })

        return root
    }

    private fun performSearch(query: String) {
        // Perform your search logic here, update the adapter with results
        val results = searchItems(query)
        searchAdapter.updateResults(results)
    }

    private fun searchItems(query: String): List<String> {
        // Dummy search logic, replace with actual search logic
        return listOf("Result 1", "Result 2", "Result 3").filter {
            it.contains(query, ignoreCase = true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
