package android.boot.slf.mixpanel

import android.boot.slf.LogService
import android.boot.slf.Logger
import android.content.Context
import com.google.auto.service.AutoService
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

@AutoService(LogService::class)
class MixpanelLog : LogService {

    private var mp: MixpanelAPI? = null

    override fun init(context: Context) {
        if (Logger.pubCloudMixPanelCfg == null || Logger.pubCloudMixPanelCfg?.token.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid cloud MixPanel configuration,Please call LogFacade.install() first with valid configurations!")
        }
        if (mp == null) mp =
            MixpanelAPI.getInstance(context, Logger.pubCloudMixPanelCfg?.token, true)
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


    override fun v(tag: String, log: String) {
        val props = JSONObject()
        props.put("verbose", log)
        mp?.track(tag, props)
    }


    override fun d(tag: String, log: String) {
        val props = JSONObject()
        props.put("debug", log)
        mp?.track(tag, props)
    }


    override fun i(tag: String, log: String) {
        val props = JSONObject()
        props.put("info", log)
        mp?.track(tag, props)
    }


    override fun w(tag: String, log: String, throwable: Throwable?) {
        val props = JSONObject()
        props.put("warn", log)
        throwable?.message?.let {
            props.put("exception", it)
        }
        mp?.track(tag, props)
    }

    override fun w(tag: String, log: String) {
        w(tag, log, null)
    }


    override fun e(tag: String, log: String, throwable: Throwable?) {
        val props = JSONObject()
        props.put("error", log)
        throwable?.message?.let {
            props.put("exception", it)
        }
        mp?.track(tag, props)
    }

    override fun e(tag: String, log: String) {
        e(tag, log, null)
    }


    override fun trace(event: String, args: Map<String, String>) {
        val props = JSONObject()
        args.entries.forEach {
            props.put(it.key, it.value)
        }

        mp?.track(event, props)
    }


}