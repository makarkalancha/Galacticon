package com.makco.galacticon

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.makco.galacticon.databinding.RecyclerviewItemRowBinding
import com.squareup.picasso.Picasso

class RecyclerAdapter (private val photos: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>() {

    private lateinit var itemRowBinding: RecyclerviewItemRowBinding

    //Make the class extend RecyclerView.ViewHolder, allowing the adapter to use it as as a ViewHolder.
    class PhotoHolder(private val itemRowBinding: RecyclerviewItemRowBinding) : RecyclerView.ViewHolder(itemRowBinding.root), View.OnClickListener{

        //Add a reference to the view you’ve inflated to allow the ViewHolder to access the
        // ImageView and TextView as an extension property. Kotlin Android Extensions plugin adds
        // hidden caching functions and fields to prevent the constant querying of views.
//        private var view: View = v
        private var photo: Photo? = null

        fun bindPhoto(photo: Photo){
            this.photo = photo
            //It also adds the suggested Picasso import, which is a library that makes it simpler to
            // get images from a given URL.
            Picasso.with(itemRowBinding.root.context).load(photo.url).into(itemRowBinding.itemImage)
            itemRowBinding.itemDate.text = photo.humanDate
            itemRowBinding.itemDescription.text = photo.expalnation
        }

        //Initialize the View.OnClickListener.
        init {
            itemRowBinding.root.setOnClickListener(this)
        }

        //Implement the required method for View.OnClickListener since ViewHolders are responsible
        // for their own event handling.
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
            val context = itemView.context
            val showPhotoIntent = Intent(context, PhotoActivity::class.java)
            showPhotoIntent.putExtra(PHOTO_KEY, photo)
            context.startActivity(showPhotoIntent)
        }

        //Add a key for easy reference to the item launching the RecyclerView.
        companion object{
            private val PHOTO_KEY = "PHOTO"
        }
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = photos[position]
        holder.bindPhoto(itemPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
//        val inflateView = parent.inflate(R.layout.recyclerview_item_row, false)
//        return PhotoHolder(inflateView)
        itemRowBinding = RecyclerviewItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoHolder(itemRowBinding)
    }

    //kotlin extension function https://kotlinlang.org/docs/extensions.html#extension-functions
    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View{
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = photos.size
}