import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

//https://stackoverflow.com/questions/60735480/okhttp3-returns-sslhandshakeexception-handshake-failed
/*
to avoid javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
with api.nasa.gov
and OkHttp / Picasso
 */

object Http {
    private const val SSL = "SSL"
    private var InsecureHttpClient: OkHttpClient? = null
    fun client(): OkHttpClient? {
        if (InsecureHttpClient == null) {
            try {
                InsecureHttpClient = insecureOkHttpClient()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return InsecureHttpClient
    }

    @Throws(Exception::class)
    private fun insecureOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf(trustManager())
        val sslContext = SSLContext.getInstance(SSL)
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier(hostnameVerifier())
        return builder.build()
    }

    private fun trustManager(): TrustManager {
        return object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    private fun hostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, session -> true }
    }
}