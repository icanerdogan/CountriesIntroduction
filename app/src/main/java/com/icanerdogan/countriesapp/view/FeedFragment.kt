package com.icanerdogan.countriesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.icanerdogan.countriesapp.R
import com.icanerdogan.countriesapp.adapter.RecyclerViewCountryAdapter
import com.icanerdogan.countriesapp.databinding.FragmentFeedBinding
import com.icanerdogan.countriesapp.viewmodel.FeedViewModel

class FeedFragment : Fragment() {
    private lateinit var feedBinding: FragmentFeedBinding
    private lateinit var feedViewModel: FeedViewModel
    private val countryAdapter = RecyclerViewCountryAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        feedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        return feedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        feedViewModel.refreshData()

        feedBinding.recyclerViewCountryList.layoutManager = LinearLayoutManager(context)
        feedBinding.recyclerViewCountryList.adapter = countryAdapter

        // Ekranda refresh yapmaya çalışıldığında!
        feedBinding.swipeRefreshLayout.setOnRefreshListener {
            feedBinding.recyclerViewCountryList.visibility = View.GONE
            feedBinding.textViewCountryError.visibility = View.GONE
            feedBinding.progressBarCountryLoading.visibility = View.GONE

            //Refresh yapıldığında internetten çekicek verileri
            //feedViewModel.refreshData()
            feedViewModel.refreshFromAPI()

            //Yukarıdaki refresh etme kısmını kaldırmak!
            feedBinding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData(){
        feedViewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                feedBinding.recyclerViewCountryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })

        feedViewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it){
                    feedBinding.textViewCountryError.visibility = View.VISIBLE
                }
                else{
                    feedBinding.textViewCountryError.visibility = View.GONE
                }
            }
        })

        feedViewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it){
                    feedBinding.progressBarCountryLoading.visibility = View.VISIBLE
                    feedBinding.textViewCountryError.visibility = View.GONE
                    feedBinding.recyclerViewCountryList.visibility = View.GONE

                }
                else{
                    feedBinding.progressBarCountryLoading.visibility = View.GONE
                }
            }
        })
    }

}