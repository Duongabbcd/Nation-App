package com.example.nationapp.presentation

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nationapp.R
import com.example.nationapp.databinding.ActivityMainBinding
import com.example.nationapp.presentation.adapter.NationAdapter
import com.example.nationapp.presentation.dialog.LoadingDialog
import com.example.nationapp.presentation.dialog.NationInfoDialog
import com.example.nationapp.presentation.viewmodel.NationViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: NationViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog

    private var isSortByName = false
    private lateinit var adapter: NationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NationAdapter(viewModel)
        binding.nationList.adapter = adapter

        loadingDialog = LoadingDialog(this)
        viewModel.fetchCountries()
        viewModel.getCountries()
        loadingDialog.show("Loading data")
        initMethod()
        initView()

        viewModel.allNations.observe(this) { event ->
            adapter.submitList(event)
            binding.totalNations.text = if (event.size == 1) "We get ${event.size} country!" else
                "We get ${event.size} countries!"
            binding.nationList.scrollToPosition(0)
            loadingDialog.dismiss()
        }

        viewModel.nations.observe(this) { event ->
            adapter.submitList(event)
            binding.totalNations.text = if (event.size == 1) "We get ${event.size} country!" else
                "We get ${event.size} countries!"
            binding.nationList.scrollToPosition(0)
            loadingDialog.dismiss()
        }
    }

    private fun initView() {
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val columnCount = width / 300 // Assuming each item has a width of 300dp
        val layoutManager = GridLayoutManager(this, columnCount)
        binding.nationList.layoutManager = layoutManager
        viewModel.selectedNation.observe(this) { event ->
            val dialog = NationInfoDialog.newInstance(event)
            dialog.show(supportFragmentManager, "NationInfoDialog")
        }

    }

    private fun initMethod() {
        binding.searchBar.doOnTextChanged { text, start, before, count ->
            viewModel.searchingNation(text.toString())
            if (text.toString().isNotEmpty()) {
                binding.clearText.visibility = View.VISIBLE
            } else binding.clearText.visibility = View.GONE
        }

        binding.clearText.setOnClickListener {
            binding.searchBar.text.clear()
        }

        binding.sort.setOnClickListener {
            isSortByName = !isSortByName
          viewModel.sortByName(isSortByName)
        }
        viewModel.isSortByName.observe(this) {event ->
            if (event) {
                viewModel.sortCountryByName()
            } else {
                viewModel.getCountries()
            }
        }

    }
}
