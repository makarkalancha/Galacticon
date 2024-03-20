package com.makco.galacticon

import Http
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makco.galacticon.databinding.ActivityPhotoBinding
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

class PhotoActivity : AppCompatActivity(){

    private var selectedPhoto: Photo? = null
    private lateinit var binding: ActivityPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        selectedPhoto = intent.getSerializableExtra(PHOTO_KEY) as Photo
        val builder : Picasso.Builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(Http.client()))
        val picasso: Picasso = builder.build()
        picasso.load(selectedPhoto?.url).into(binding.itemImage)

        binding.itemDescription?.text = selectedPhoto?.expalnation
    }

    companion object{
        private val PHOTO_KEY = "PHOTO"
    }
}