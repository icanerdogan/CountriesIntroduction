package com.icanerdogan.countriesapp.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.icanerdogan.countriesapp.R

// Extension
/*
fun String.myExtensions(myParameter : String){
    println(myParameter)
}
 */

fun ImageView.downloadFromURL(url : String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
         // Textler çok hızlı indiği için görseller geri kalır bu yüzden place holder kullanıyoruz!
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

//BindingAdapter: Bu fonksiyonun XML'de çalıştırılmasını sağlar
@BindingAdapter("android:downloadURL")
fun downloadImage(view: ImageView, url: String?){
    view.downloadFromURL(url, placeHolderProgressBar(view.context))
}