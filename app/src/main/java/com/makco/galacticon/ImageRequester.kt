package com.makco.galacticon

import android.app.Activity
import android.content.Context
import android.net.Uri.Builder
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.tls.Certificates
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

try this
https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/CustomTrust.java

private const val CERT = Certificates.decodeCertificatePem("-----BEGIN CERTIFICATE-----\n" +
        "MIIDMTCCAhmgAwIBAgIJQgAAIjVl8PKMMA0GCSqGSIb3DQEBCwUAMFQxGTAXBgNV\n" +
        "BAoMEEFPIEthc3BlcnNreSBMYWIxNzA1BgNVBAMMLkthc3BlcnNreSBBbnRpLVZp\n" +
        "cnVzIFBlcnNvbmFsIFJvb3QgQ2VydGlmaWNhdGUwHhcNMjMwOTEzMDAyNTQ4WhcN\n" +
        "MjQwOTExMDAyNTQ4WjAXMRUwEwYDVQQDEwxhcGkubmFzYS5nb3YwggEiMA0GCSqG\n" +
        "SIb3DQEBAQUAA4IBDwAwggEKAoIBAQDBP72f5gikwViXs+dcoJfO9TVJodQFv1mt\n" +
        "tOpR3KWMW7nAk0wYpNcibmeTPpgevg2NjA9t/P/ZTXIIwbNQeKOKpBKWwo/QtluR\n" +
        "plFfKNM+9WH2R8pKGfK0o+nyAXVKlX0oWDmeRNcPwVbJMu749MNlE/nQ5r0bbMbR\n" +
        "2ReazER8CycN+9zrRAKDadNPakb8NLCnHThEfmDUKUAcKiyqWt78mn77ZgXVFS0b\n" +
        "I1Qz9T2qhsG4CzufVmgibECcLUlsJmQGMRPKuXLS0H621FdQ7s0AncJMY2D6S1aX\n" +
        "8JmKz2ihhAeB2d4ulau0BFponbjaDVdoKSMSeQxR6HihwLihTL6lAgMBAAGjQzBB\n" +
        "MBMGA1UdJQQMMAoGCCsGAQUFBwMBMAsGA1UdDwQEAwIFoDAdBgNVHREEFjAUhwQP\n" +
        "yEP1ggxhcGkubmFzYS5nb3YwDQYJKoZIhvcNAQELBQADggEBAJoagQhvi6luZD8t\n" +
        "662wVYz7h9+UUdoM3OxMN2G+RF1gvUlPQ2G5ABRwWXAvZn6I4BGBJRwxIuvGorCA\n" +
        "msY7ALkYOll3RqkUoWdXXib1fJ7iG8uIeb/f79LyBX6Wc4meuFvrcHRNDWKCYVjw\n" +
        "f2ECZfRkzEMlP7r+nJP8e0Vw0XcdoWMnQE/z4qLzGXlujKbzFn8Al7jLWtqRmk8V\n" +
        "30iriSw4i/owwsXTCDqiXq8l4qhX7E+qmMGedp2p4ByUC0wDYUMJhWtg0JaTi7hI\n" +
        "iom+vTMh49u5HQJ/EvWZO4JXkUtLdzIQH9UsWGIEc4ZpfuFVsXVFJf8QISQz02M4\n" +
        "E+6Sggs=\n" +
        "-----END CERTIFICATE-----\n")
class ImageRequester(listeningActivity: Activity) {

    interface ImageRequesterResponse{
        fun receivedNewPhoto(newPhoto: Photo)
    }

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val responseListener: ImageRequesterResponse
    private val context: Context
    private val client: OkHttpClient
    var isLoadingData: Boolean = false
        private set

    init {
        responseListener = listeningActivity as ImageRequesterResponse
        context = listeningActivity.applicationContext
        client = OkHttpClient()
//        SSLContext sslContext = sslutils
//        client.setsslSocketFactory()
    }

    fun getPhoto(){
        val date = dateFormat.format(calendar.time)
        Log.d("getPhoto", context.getString(R.string.nasa_api_key))

        val urlRequest = Builder()
            .scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_1)
            .appendPath(URL_PATH_2)
            .appendQueryParameter(URL_QUERY_PARAM_DATE_KEY, date)
            .appendQueryParameter(URL_QUERY_PARAM_API_KEY, context.getString(R.string.nasa_api_key))
            .build().toString()
        Log.d("getPhoto", urlRequest)
//        val url = URL(urlRequest)
//        val urlConnection: URLConnection = url.openConnection()
//        val inputStream: InputStream = urlConnection.getInputStream()
//        var text = ""
//        IOUtils.toString(inputStream, text)
//        Log.d("getPhoto", text)

//        val caFileInputStream = context.resources.openRawResource(R.raw.api_nasa_gov_crt)
//        val keyStore = KeyStore.getInstance("PKCS12")
//        keyStore.load(caFileInputStream, "my file password".toCharArray())
//        val keyManagerFactory = KeyManagerFactory.getInstance("X509")
//        keyManagerFactory.init(keyStore, "my file password".toCharArray())
//        val sslContext = SSLContext.getInstance("TLS")
//        sslContext.init(keyManagerFactory.keyManagers, null, SecureRandom())

        val request = Request.Builder().url(urlRequest).build()
        isLoadingData = true

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                isLoadingData = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try{
//                    javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
                    val photoJson = JSONObject(response.body()!!.string())

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