package com.makco.galacticon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makco.galacticon.databinding.ActivityMainBinding
import java.io.IOException


class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse/*, ProviderInstaller.ProviderInstallListener */{

    private var photosList: ArrayList<Photo> = ArrayList()
//    private var retryProviderInstall: Boolean = false
    private val lastVisibleItemPosition: Int
        get() = if (binding.recyclerView.layoutManager == linearLayoutManager){
            linearLayoutManager.findLastVisibleItemPosition()
        }else{
            gridLayoutManager.findLastVisibleItemPosition()
        }

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var imageRequester: ImageRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ProviderInstaller.installIfNeededAsync(this, this)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        linearLayoutManager = LinearLayoutManager(this)
        gridLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(photosList)
        binding.recyclerView.adapter = adapter

        imageRequester = ImageRequester(this)

        setRecyclerViewScrollListener()
        setRecyclerViewItemTouchListener()
    }

    override fun onStart() {
        super.onStart()

//        if(photosList.size == 0){
        if(photosList.isEmpty()){
            requestPhoto()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_recycler_manager){
            changeLayouManager()
            return true
        }
        return super.onOptionsItemSelected(item)
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

    private fun setRecyclerViewScrollListener(){
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if(!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1){
                    requestPhoto()
                }
            }
        })
    }

    private fun changeLayouManager(){
        if(binding.recyclerView.layoutManager == linearLayoutManager){
            // If it’s using the LinearLayoutManager, it swaps in the GridLayoutManager.
            binding.recyclerView.layoutManager = gridLayoutManager
            // It requests a new photo if your grid layout only has one photo to show.
            if(photosList.size == 1){
                requestPhoto()
            }
        }else{
            // If it’s using the GridLayoutManager, it swaps in the LinearLayoutManager.
            binding.recyclerView.layoutManager = linearLayoutManager
        }
    }

    private fun setRecyclerViewItemTouchListener(){
        //Create the callback and tell it what events to listen for. It takes two parameters:
        // One for drag directions and one for swipe directions. You’re only interested in swipe.
        // Pass 0 to inform the callback not to respond to drag events.
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //Return false in onMove. You don’t want to perform any special behavior here.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Call onSwiped when you swipe an item in the direction specified in the
                // ItemTouchHelper. Here, you request the viewHolder parameter passed for the
                // position of the item view, and then you remove that item from your list of
                // photos. Finally, you inform the RecyclerView adapter that an item has been
                // removed at a specific position.
                val position = viewHolder.adapterPosition
                photosList.removeAt(position)
                binding.recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        //Initialize ItemTouchHelper with the callback behavior you defined, and then attach it to
        // the RecyclerView.
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
//    /**
//     * This method is called if updating fails. The error code indicates
//     * whether the error is recoverable.
//     */
//    override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
//        GoogleApiAvailability.getInstance().apply {
//            if(isUserResolvableError(errorCode)){
//                Log.d("MainActivity", "onProviderInstallFailed->in if")
//                //Recoverable erro. Show a dialog promptin the user to
//                // install/update/enable Google Play services.
//                showErrorDialogFragment(this@MainActivity, errorCode, ERROR_DIALOG_REQUEST_CODE){
//                    //The user chose not to take the recovery action.
//                    onProviderInstallerNotAvailable()
//                }
//            }else{
//                Log.d("MainActivity", "onProviderInstallFailed->in else")
//                onProviderInstallerNotAvailable()
//            }
//        }
//    }
//
//    /**
//     * This method is only called if the provider is successfully updated
//     * (or is already up to date).
//     */
//    override fun onProviderInstalled() {
//        //Provider is up to date; app can make secure network calls.
//        if(photosList.isEmpty()){
//            Log.d("MainActivity", "onProviderInstalled->in if")
//            requestPhoto()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == ERROR_DIALOG_REQUEST_CODE){
//            //Adding a fragment via GoogleApiAvailability.showErrorDialogFragment
//            // before the instance state is restored throws an error. So instead,
//            // set a flag here, which causes the fragment to delay until
//            // onPostResume.
//            Log.d("MainActivity", "onActivityResult->in if")
//            retryProviderInstall = true
//        }
//    }
//
//    override fun onPostResume() {
//        super.onPostResume()
//        if(retryProviderInstall){
//            Log.d("MainActivity", "onPostResume->if")
//            //It's safe to retry installation
//            ProviderInstaller.installIfNeededAsync(this, this)
//        }
//        retryProviderInstall = false
//    }
//
//    private fun onProviderInstallerNotAvailable(){
//        //This is reached if the provider can't be updated for some reason.
//        // App should consider all HTTP communication to be vulnerable and take
//        // appropriate action.
//        Log.d("MainActivity", "onProviderInstallerNotAvailable->inside")
//    }
}
