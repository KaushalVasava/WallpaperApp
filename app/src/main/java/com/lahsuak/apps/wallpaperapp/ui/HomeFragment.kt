package com.lahsuak.apps.wallpaperapp.ui

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.lahsuak.apps.wallpaperapp.R
import com.lahsuak.apps.wallpaperapp.databinding.FragmentHomeBinding
import com.lahsuak.apps.wallpaperapp.ui.adapter.ImageAdapter
import com.lahsuak.apps.wallpaperapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home),
    SearchView.OnQueryTextListener, ImageAdapter.ImageListener {

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
        }
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            val decoration = DividerItemDecoration(
                requireContext().applicationContext,
                DividerItemDecoration.VERTICAL
            )
            addItemDecoration(decoration)
            imageAdapter = ImageAdapter(this@HomeFragment)
            adapter = imageAdapter
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                imageAdapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_menu, menu)
        val searchItem = menu.findItem(R.id.searchView)

        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = getString(R.string.search_image)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query==null)
            viewModel.userSearch("")
        else {
            viewModel.userSearch(query)
            lifecycleScope.launch {
                viewModel.flow.collectLatest {
                    imageAdapter.submitData(it)
                }
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onItemClick(url: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToWallpaperFragment(url)
        findNavController().navigate(action)
    }
}