package com.icanerdogan.countriesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.icanerdogan.countriesapp.databinding.RowLayoutBinding
import com.icanerdogan.countriesapp.model.Country
import com.icanerdogan.countriesapp.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.row_layout.view.*


class RecyclerViewCountryAdapter(val countryList: ArrayList<Country>) :
    RecyclerView.Adapter<RecyclerViewCountryAdapter.RowHolder>(),
    CountryClickListener{

    class RowHolder(val rowLayoutBinding: RowLayoutBinding) : RecyclerView.ViewHolder(rowLayoutBinding.root) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        return RowHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {

        holder.rowLayoutBinding.country = countryList[position]

        holder.rowLayoutBinding.listener = this

        /*holder.rowLayoutBinding.textViewRowCountryName.text = countryList[position].countryName
        holder.rowLayoutBinding.textViewRowCountryRegion.text = countryList[position].countryRegion

        holder.itemView.setOnClickListener {
            val action =
                FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
        holder.rowLayoutBinding.imageViewRowCountryFlag.downloadFromURL(
            countryList[position].countryFlagURL,
            placeHolderProgressBar(holder.itemView.context)
        )
         */


    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    // Adapter güncellemesi için kullanılacak fonksiyon
    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()

    }

    override fun onCountryClicked(v: View) {
        val uuid = v.textViewCountryUUID.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }

}