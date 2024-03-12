package com.makco.galacticon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.makco.galacticon.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse {

    private var photosList: ArrayList<Photo> = ArrayList()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var imageRequester: ImageRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(photosList)
        binding.recyclerView.adapter = adapter

        imageRequester = ImageRequester(this)
    }

    override fun onStart() {
        super.onStart()

//        if(photosList.size == 0){
        if(photosList.isEmpty()){
            requestPhoto()
        }
    }

    private fun requestPhoto(){
        try{
            imageRequester.getPhoto()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    override fun receivedNewPhoto(newPhoto: Photo) {
        runOnUiThread{
            photosList.add(newPhoto)
            adapter.notifyItemInserted(photosList.size - 1)
        }
    }
}
