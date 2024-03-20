package com.makco.galacticon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makco.galacticon.databinding.ActivityMainBinding
import java.io.IOException

/*
to solve OkHttpClient Intermittent SSLHandshakeException calling a GET
https://developer.android.com/privacy-and-security/security-gms-provider#kotlin
https://stackoverflow.com/questions/68639187/trust-anchor-for-certification-path-not-found-on-android-project
 */
private const val ERROR_DIALOG_REQUEST_CODE = 1

class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse/*, ProviderInstaller.ProviderInstallListener */{

    private var photosList: ArrayList<Photo> = ArrayList()
//    private var retryProviderInstall: Boolean = false
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

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
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(photosList)
        binding.recyclerView.adapter = adapter

        imageRequester = ImageRequester(this)

        setRecyclerViewScrollListener()
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
