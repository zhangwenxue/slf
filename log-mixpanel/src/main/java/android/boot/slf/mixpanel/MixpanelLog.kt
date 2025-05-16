package android.boot.slf.mixpanel

import android.boot.slf.L
import android.boot.slf.LogService
import android.content.Context
import com.google.auto.service.AutoService
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

@AutoService(LogService::class)
class MixpanelLog(override var fixedPrefixProvider: () -> String = { "" }) : LogService {
    companion object {
        private const val DEFAULT_TAG = "_tag"
    }

    private var mp: MixpanelAPI? = null
    private var logTag: String = "_Mixpanel"
    private var logPrefix: String = ""
    private var logSuffix: String = ""
    private fun applyTag(tag: String, separator: String = "|") =
        if (logTag == "_Log") tag else "$logTag$separator$tag"

    private fun getPrefix() = if (logPrefix.trimIndent()
            .isEmpty()
    ) fixedPrefixProvider() else "$logPrefix-${fixedPrefixProvider()}> "

    private fun getSuffix() = if (logSuffix.trimIndent().isEmpty()) "" else " >$logSuffix"

    override fun init(context: Context) {
        if (L.pubCloudMixPanelCfg == null || L.pubCloudMixPanelCfg?.token.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid cloud MixPanel configuration,Please call LogFacade.install() first with valid configurations!")
        }
        mp = MixpanelAPI.getInstance(context, L.pubCloudMixPanelCfg?.token, true)
    }

    override fun registerUser(
        userId: String,
        userName: String,
        fullName: String,
        email: String,
        phone: String,
        additionalInfo: Map<String, String>
    ) {
        mp?.run {
            identify(userId, true)
            people.set("\$userName", userName)
            people.set("\$fullName", fullName)
            people.set("\$email", email)
            people.set("\$phone", phone)
            additionalInfo.entries.forEach {
                people.set("$${it.key}", it.value)
            }
        }
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
        val props = JSONObject()
        props.put("_v", "${getPrefix()}$log${getSuffix()}")
        mp?.track(tag ?: DEFAULT_TAG, props)
    }

    override fun v(log: String?) {
        v(logTag, log)
    }

    override fun d(tag: String?, log: String?) {
        val props = JSONObject()
        props.put("_d", "${getPrefix()}$log${getSuffix()}")
        mp?.track(tag ?: DEFAULT_TAG, props)
    }

    override fun d(log: String?) {
        d(logTag, log)
    }

    override fun i(tag: String?, log: String?) {
        val props = JSONObject()
        props.put("_v", "${getPrefix()}$log${getSuffix()}")
        mp?.track(tag ?: DEFAULT_TAG, props)
    }

    override fun i(log: String?) {
        i(logTag, log)
    }


    override fun w(tag: String?, log: String?, throwable: Throwable?) {
        val props = JSONObject()
        props.put("_w", "${getPrefix()}$log${getSuffix()}")
        throwable?.message?.let {
            props.put("_ex", it)
        }
        mp?.track(tag ?: DEFAULT_TAG, props)
    }

    override fun w(tag: String?, log: String?) {
        w(tag, log, null)
    }

    override fun w(log: String?) {
        w(logTag, log, null)
    }

    override fun w(log: String?, throwable: Throwable?) {
        w(logTag, log, throwable)
    }


    override fun e(tag: String?, log: String?, throwable: Throwable?) {
        val props = JSONObject()
        props.put("_e", "${getPrefix()}$log${getSuffix()}")
        throwable?.message?.let {
            props.put("_ex", it)
        }
        mp?.track(tag ?: DEFAULT_TAG, props)
    }

    override fun e(tag: String?, log: String?) {
        e(tag, log, null)
    }


    override fun e(log: String?) {
        e(logTag, log, null)
    }

    override fun e(log: String?, throwable: Throwable?) {
        e(logTag, log, throwable)
    }
}