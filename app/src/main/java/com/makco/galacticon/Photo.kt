package com.makco.galacticon

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class Photo(photoJson: JSONObject) : Serializable {
    private lateinit var photoDate: String
    lateinit var humanDate: String
        private set
    lateinit var expalnation: String
        private set

    lateinit var url: String
        private set

    init{
        try{
            photoDate = photoJson.getString(PHOTO_DATE)
            humanDate = convertDateToHumanDate()
            expalnation = photoJson.getString(PHOTO_EXPLANATION)
            url = photoJson.getString(PHOTO_URL)
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }

    private fun convertDateToHumanDate(): String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val humanDateFormat = SimpleDateFormat("dd MMMM yyyy")
        try{
            val parsedDateFormat = dateFormat.parse(photoDate)
            val cal = Calendar.getInstance()
            cal.time = parsedDateFormat
            return humanDateFormat.format(cal.time)
        }catch (e: ParseException){
            e.printStackTrace()
            return ""
        }
    }

    companion object{
        private val PHOTO_DATE = "date"
        private val PHOTO_EXPLANATION = "explanation"
        private val PHOTO_URL = "url"
    }

}