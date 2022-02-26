package com.icanerdogan.countriesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icanerdogan.countriesapp.R
import com.icanerdogan.countriesapp.databinding.FragmentCountryBinding
import com.icanerdogan.countriesapp.util.downloadFromURL
import com.icanerdogan.countriesapp.util.placeHolderProgressBar
import com.icanerdogan.countriesapp.viewmodel.CountryViewModel

class CountryFragment : Fragment() {
    private lateinit var countryBinding: FragmentCountryBinding
    private lateinit var countryViewModel: CountryViewModel
    private var countryUid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        countryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_country, container, false)
        return countryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUid = CountryFragmentArgs.fromBundle(it).countryID
        }
        countryViewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        countryViewModel.getDataFromRoom(countryUid)

        observeLiveData()
    }

    private fun observeLiveData() {
        countryViewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                countryBinding.selectedCountry = it
                /*countryBinding.textViewCountryCapital.text = country.countryCapital
                countryBinding.textViewCountryCurrency.text = country.countryCurrency
                countryBinding.textViewCountryLanguage.text = country.countryLanguage
                countryBinding.textViewCountryName.text = country.countryName
                countryBinding.textViewCountryRegion.text = country.countryRegion

                context?.let {
                    countryBinding.imageViewCountryFlag.downloadFromURL(
                        country.countryFlagURL,
                        placeHolderProgressBar(it)
                    )
                }
                 */
            }
        })
    }
}