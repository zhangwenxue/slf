package android.boot.slf

import android.content.Context
import android.util.Log
import java.util.ServiceLoader

private inline fun <reified S> requireService() = ServiceLoader.load(S::class.java).firstOrNull()
private inline fun <reified S> requireServices() = ServiceLoader.load(S::class.java)?.toList()

interface LogService {

    var fixedPrefixProvider: () -> String

    fun init(context: Context)

    fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    )

    fun tag(tag: String? = null, append: Boolean = false, separator: String = "|"): LogService
    fun prefix(prefix: String? = null, append: Boolean = false, separator: String = "·"): LogService
    fun suffix(suffix: String? = null, append: Boolean = false, separator: String = "·"): LogService

    fun v(tag: String?, log: String?)
    fun d(tag: String?, log: String?)
    fun i(tag: String?, log: String?)
    fun w(tag: String?, log: String?)
    fun w(tag: String?, log: String?, throwable: Throwable?)
    fun e(tag: String?, log: String?)
    fun e(tag: String?, log: String? = null, throwable: Throwable?)
    fun v(log: String?)
    fun d(log: String?)
    fun i(log: String?)
    fun w(log: String?)
    fun w(log: String?, throwable: Throwable?)
    fun e(log: String?)
    fun e(log: String? = null, throwable: Throwable?)
}

private object SimpleLog : LogService {
    private var logTag: String = "_Log"
    private var logPrefix: String = ""
    private var logSuffix: String = ""
    private fun applyTag(tag: String, separator: String = "|") =
        if (logTag == "_Log") tag else "$logTag$separator$tag"

    private fun getPrefix() = if (logPrefix.trimIndent().isEmpty()) "" else "$logPrefix> "

    private fun getSuffix() = if (logSuffix.trimIndent().isEmpty()) "" else " >$logSuffix"

    override var fixedPrefixProvider: () -> String = { "" }

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

    override fun tag(tag: String?, append: Boolean, separator: String): LogService {
        logTag = tag?.let {
            if (!append) tag else applyTag(it, separator)
        } ?: "_Log"
        return this
    }

    override fun prefix(prefix: String?, append: Boolean, separator: String): LogService {
        logPrefix = prefix?.let {
            if (!append) prefix else "$logPrefix$separator$prefix"
        } ?: ""
        return this
    }

    override fun suffix(suffix: String?, append: Boolean, separator: String): LogService {
        logSuffix = suffix?.let {
            if (!append) suffix else "$logSuffix$separator$suffix"
        } ?: ""
        return this
    }

    override fun v(tag: String?, log: String?) {
        Log.v(tag, "$log")
    }

    override fun v(log: String?) {
        Log.v(logTag, "${getPrefix()}$log${getSuffix()}")
    }

    override fun d(tag: String?, log: String?) {
        Log.d(tag, "$log")
    }

    override fun d(log: String?) {
        Log.d(logTag, "${getPrefix()}$log${getSuffix()}")
    }

    override fun i(tag: String?, log: String?) {
        Log.i(tag, "$log")
    }

    override fun i(log: String?) {
        Log.i(logTag, "${getPrefix()}$log${getSuffix()}")
    }

    override fun w(tag: String?, log: String?) {
        Log.v(tag, "$log")
    }

    override fun w(tag: String?, log: String?, throwable: Throwable?) {
        Log.v(tag, "$log", throwable)
    }

    override fun w(log: String?) {
        Log.w(logTag, "${getPrefix()}$log${getSuffix()}")
    }

    override fun w(log: String?, throwable: Throwable?) {
        Log.w(logTag, "${getPrefix()}$log${getSuffix()}", throwable)
    }

    override fun e(tag: String?, log: String?) {
        Log.e(tag, "$log")
    }

    override fun e(tag: String?, log: String?, throwable: Throwable?) {
        Log.e(tag, "$log", throwable)
    }

    override fun e(log: String?) {
        Log.e(logTag, "${getPrefix()}$log${getSuffix()}")
    }

    override fun e(log: String?, throwable: Throwable?) {
        Log.e(logTag, "${getPrefix()}$log${getSuffix()}", throwable)
    }
}


object LogFacade : LogService {
    private val logService by lazy {
        requireService<LogService>() ?: SimpleLog
    }

    var pubCloudShipBookCfg: PubCloudShipBookCfg? = null

    var pubCloudMixPanelCfg: PubCloudMixpanelCfg? = null

    fun installPubCloudShipBook(appId: String, appKey: String) {
        pubCloudShipBookCfg = PubCloudShipBookCfg(appId, appKey)
    }

    fun installPubCloudMixPanel(token: String) {
        pubCloudMixPanelCfg = PubCloudMixpanelCfg(token)
    }

    override var fixedPrefixProvider: () -> String
        get() = logService.fixedPrefixProvider
        set(value) {
            logService.fixedPrefixProvider = value
        }

    override fun init(context: Context) {
        logService.init(context)
    }

    override fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    ) {
        logService.registerUser(userId, userName, fullName, email, phone, additionalInfo)
    }

    override fun tag(tag: String?, append: Boolean, separator: String) =
        logService.tag(tag, append, separator)

    override fun prefix(prefix: String?, append: Boolean, separator: String) =
        logService.prefix(prefix, append, separator)

    override fun suffix(suffix: String?, append: Boolean, separator: String) =
        logService.suffix(suffix, append, separator)

    override fun v(tag: String?, log: String?) {
        logService.v(tag, log)
    }

    override fun v(log: String?) {
        logService.v(log)
    }

    override fun d(tag: String?, log: String?) {
        logService.d(tag, log)
    }

    override fun d(log: String?) {
        logService.d(log)
    }

    override fun i(tag: String?, log: String?) {
        logService.i(tag, log)
    }

    override fun i(log: String?) {
        logService.i(log)
    }

    override fun w(tag: String?, log: String?) {
        logService.w(tag, log)
    }

    override fun w(tag: String?, log: String?, throwable: Throwable?) {
        logService.w(tag, log, throwable)
    }

    override fun w(log: String?) {
        logService.w(log)
    }

    override fun w(log: String?, throwable: Throwable?) {
        logService.w(log, throwable)
    }

    override fun e(tag: String?, log: String?) {
        logService.e(tag, log)
    }

    override fun e(tag: String?, log: String?, throwable: Throwable?) {
        logService.e(tag, log, throwable)
    }

    override fun e(log: String?) {
        logService.e(log)
    }

    override fun e(log: String?, throwable: Throwable?) {
        logService.e(log, throwable)
    }

}

object L : LogService {
    private val logService by lazy {
        requireService<LogService>() ?: SimpleLog
    }

    var pubCloudShipBookCfg: PubCloudShipBookCfg? = null

    var pubCloudMixPanelCfg: PubCloudMixpanelCfg? = null

    fun installPubCloudShipBook(appId: String, appKey: String) {
        pubCloudShipBookCfg = PubCloudShipBookCfg(appId, appKey)
    }

    fun installPubCloudMixPanel(token: String) {
        pubCloudMixPanelCfg = PubCloudMixpanelCfg(token)
    }

    override var fixedPrefixProvider: () -> String
        get() = logService.fixedPrefixProvider
        set(value) {
            logService.fixedPrefixProvider = value
        }

    override fun init(context: Context) {
        logService.init(context)
    }

    override fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    ) {
        logService.registerUser(userId, userName, fullName, email, phone, additionalInfo)
    }

    override fun tag(tag: String?, append: Boolean, separator: String) =
        logService.tag(tag, append, separator)

    override fun prefix(prefix: String?, append: Boolean, separator: String) =
        logService.prefix(prefix, append, separator)

    override fun suffix(suffix: String?, append: Boolean, separator: String) =
        logService.suffix(suffix, append, separator)

    override fun v(tag: String?, log: String?) {
        logService.v(tag, log)
    }

    override fun v(log: String?) {
        logService.v(log)
    }

    override fun d(tag: String?, log: String?) {
        logService.d(tag, log)
    }

    override fun d(log: String?) {
        logService.d(log)
    }

    override fun i(tag: String?, log: String?) {
        logService.i(tag, log)
    }

    override fun i(log: String?) {
        logService.i(log)
    }

    override fun w(tag: String?, log: String?) {
        logService.w(tag, log)
    }

    override fun w(tag: String?, log: String?, throwable: Throwable?) {
        logService.w(tag, log, throwable)
    }

    override fun w(log: String?) {
        logService.w(log)
    }

    override fun w(log: String?, throwable: Throwable?) {
        logService.w(log, throwable)
    }

    override fun e(tag: String?, log: String?) {
        logService.e(tag, log)
    }

    override fun e(tag: String?, log: String?, throwable: Throwable?) {
        logService.e(tag, log, throwable)
    }

    override fun e(log: String?) {
        logService.e(log)
    }

    override fun e(log: String?, throwable: Throwable?) {
        logService.e(log, throwable)
    }

}