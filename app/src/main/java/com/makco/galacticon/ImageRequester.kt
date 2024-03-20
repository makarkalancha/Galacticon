package com.makco.galacticon

import Http
import android.app.Activity
import android.content.Context
import android.net.Uri.Builder
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class ImageRequester(listeningActivity: Activity) {

    interface ImageRequesterResponse{
        fun receivedNewPhoto(newPhoto: Photo)
    }

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val responseListener: ImageRequesterResponse
    private val context: Context
    private val client: OkHttpClient?
    var isLoadingData: Boolean = false
        private set

    init {
        responseListener = listeningActivity as ImageRequesterResponse
        context = listeningActivity.applicationContext
//        client = OkHttpClient()
        client = Http.client()
    }

    fun getPhoto(){
        val date = dateFormat.format(calendar.time)

        val urlRequest = Builder()
            .scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_1)
            .appendPath(URL_PATH_2)
            .appendQueryParameter(URL_QUERY_PARAM_DATE_KEY, date)
//            .appendQueryParameter(URL_QUERY_PARAM_DATE_KEY, "2024-03-15")
            .appendQueryParameter(URL_QUERY_PARAM_API_KEY, context.getString(R.string.nasa_api_key))
            .build().toString()

        Log.d("getPhoto->url", urlRequest)

        val request = Request.Builder().url(urlRequest).build()
        isLoadingData = true

        client?.newCall(request)?.enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                isLoadingData = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try{
//                    val photoJson = JSONObject(response.body()!!.string())
                    val photoJson = JSONObject(response.body!!.string())
//                    Log.d("onResponse", photoJson!!.toString())

                    calendar.add(Calendar.DAY_OF_YEAR, -1)

                    if(photoJson.getString(MEDIA_TYPE_KEY) != MEDIA_TYPE_VIDEO_VALUE){
                        val receivedPhoto = Photo(photoJson)
                        responseListener.receivedNewPhoto(receivedPhoto)
                        isLoadingData = false
                    }else{
                        getPhoto()
                    }
                }catch (e: JSONException){
                    isLoadingData = false
                    e.printStackTrace()
                }
            }
        })
    }

    companion object{
        private val MEDIA_TYPE_KEY = "media_type"
        private val MEDIA_TYPE_VIDEO_VALUE = "video"
        private val URL_SCHEME = "https"
        private val URL_AUTHORITY = "api.nasa.gov"
        private val URL_PATH_1 = "planetary"
        private val URL_PATH_2 = "apod"
        private val URL_QUERY_PARAM_DATE_KEY = "date"
        private val URL_QUERY_PARAM_API_KEY = "api_key"
    }
}