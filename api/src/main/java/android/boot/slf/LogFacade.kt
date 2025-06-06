package android.boot.slf

import android.content.Context
import android.util.Log
import java.util.ServiceLoader

private inline fun <reified S> requireService() = ServiceLoader.load(S::class.java).firstOrNull()
private inline fun <reified S> requireServices() = ServiceLoader.load(S::class.java)?.toList()

interface LogService {

    fun init(context: Context)

    fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    )

    fun v(tag: String, log: String)
    fun d(tag: String, log: String)
    fun i(tag: String, log: String)
    fun w(tag: String, log: String)
    fun w(tag: String, log: String, throwable: Throwable?)
    fun e(tag: String, log: String)
    fun e(tag: String, log: String, throwable: Throwable?)
    fun trace(event: String, args: Map<String, String>)
}

private object Logcat : LogService {

    override fun init(context: Context) {}

    override fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    ) {

    }

    override fun v(tag: String, log: String) {
        Log.v(tag, log)
    }


    override fun d(tag: String, log: String) {
        Log.d(tag, log)
    }


    override fun i(tag: String, log: String) {
        Log.i(tag, log)
    }


    override fun w(tag: String, log: String) {
        Log.v(tag, log)
    }

    override fun w(tag: String, log: String, throwable: Throwable?) {
        Log.v(tag, log, throwable)
    }


    override fun e(tag: String, log: String) {
        Log.e(tag, log)
    }

    override fun e(tag: String, log: String, throwable: Throwable?) {
        Log.e(tag, log, throwable)
    }


    override fun trace(event: String, args: Map<String, String>) {
        Log.i(event, args.entries.joinToString())
    }


}

object Logger : LogService {
    private val services by lazy {
        requireServices<LogService>() ?: listOf(Logcat)
    }

    var pubCloudShipBookCfg: PubCloudShipBookCfg? = null

    var pubCloudMixPanelCfg: PubCloudMixpanelCfg? = null

    @JvmStatic
    fun installPubCloudShipBook(appId: String, appKey: String) {
        pubCloudShipBookCfg = PubCloudShipBookCfg(appId, appKey)
    }

    @JvmStatic
    fun installPubCloudMixPanel(token: String) {
        pubCloudMixPanelCfg = PubCloudMixpanelCfg(token)
    }


    override fun init(context: Context) {
        services.forEach { it.init(context) }
    }

    override fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    ) {
        services.forEach {
            it.registerUser(
                userId,
                userName,
                fullName,
                email,
                phone,
                additionalInfo
            )
        }
    }


    override fun v(tag: String, log: String) {
        services.forEach {
            it.v(tag, log)
        }
    }

    override fun d(tag: String, log: String) {
        services.forEach {
            it.d(tag, log)
        }
    }

    override fun i(tag: String, log: String) {
        services.forEach {
            it.i(tag, log)
        }
    }


    override fun w(tag: String, log: String) {
        services.forEach {
            it.w(tag, log)
        }
    }

    override fun w(tag: String, log: String, throwable: Throwable?) {
        services.forEach {
            it.w(tag, log, throwable)
        }
    }

    override fun e(tag: String, log: String) {
        services.forEach {
            it.e(tag, log)
        }
    }

    override fun e(tag: String, log: String, throwable: Throwable?) {
        services.forEach {
            it.e(tag, log, throwable)
        }
    }


    override fun trace(event: String, args: Map<String, String>) {
        services.forEach {
            it.trace(event, args)
        }
    }

}