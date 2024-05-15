package android.boot.slf.shipbook

import android.app.Application
import android.boot.slf.LogFacade
import android.boot.slf.LogService
import android.content.Context
import com.google.auto.service.AutoService
import io.shipbook.shipbooksdk.Log
import io.shipbook.shipbooksdk.ShipBook

@AutoService(LogService::class)
class ShipBookLog(override var fixedPrefixProvider: () -> String = { "" }) : LogService {
    private var logTag: String = "_ShipBook"
    private var logPrefix: String = ""
    private var logSuffix: String = ""
    private fun applyTag(tag: String, separator: String = "|") =
        if (logTag == "_Log") tag else "$logTag$separator$tag"

    private fun getPrefix() = if (logPrefix.trimIndent()
            .isEmpty()
    ) fixedPrefixProvider() else "$logPrefix-${fixedPrefixProvider()}> "

    private fun getSuffix() = if (logSuffix.trimIndent().isEmpty()) "" else " >$logSuffix"

    override fun init(context: Context) {
        if (LogFacade.pubCloudShipBookCfg == null || LogFacade.pubCloudShipBookCfg?.appId.isNullOrBlank() || LogFacade.pubCloudShipBookCfg?.appKey.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid cloud ship book configuration,Please call LogFacade.installPubCloudShipBook(PubCloudShipBookCfg) first with valid configurations!")
        }
        ShipBook.start(
            context.applicationContext as Application,
            LogFacade.pubCloudShipBookCfg?.appId ?: "",
            LogFacade.pubCloudShipBookCfg?.appKey ?: ""
        )
    }

    override fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    ) {
        ShipBook.registerUser(userId, userName, fullName, email, phone, additionalInfo)
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
        Log.w(tag, "$log")
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
        Log.v(tag, "$log", throwable)
    }

    override fun e(log: String?) {
        Log.e(logTag, "${getPrefix()}$log${getSuffix()}")
    }

    override fun e(log: String?, throwable: Throwable?) {
        Log.e(logTag, "${getPrefix()}$log${getSuffix()}", throwable)
    }
}