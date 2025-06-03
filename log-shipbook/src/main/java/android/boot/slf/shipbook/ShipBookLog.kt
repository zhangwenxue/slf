package android.boot.slf.shipbook

import android.app.Application
import android.boot.slf.LogService
import android.boot.slf.Logger
import android.content.Context
import com.google.auto.service.AutoService
import io.shipbook.shipbooksdk.Log
import io.shipbook.shipbooksdk.ShipBook

@AutoService(LogService::class)
class ShipBookLog : LogService {

    override fun init(context: Context) {
        if (Logger.pubCloudShipBookCfg == null || Logger.pubCloudShipBookCfg?.appId.isNullOrBlank() || Logger.pubCloudShipBookCfg?.appKey.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid cloud ship book configuration,Please call LogFacade.installPubCloudShipBook(PubCloudShipBookCfg) first with valid configurations!")
        }
        ShipBook.start(
            context.applicationContext as Application,
            Logger.pubCloudShipBookCfg?.appId ?: "",
            Logger.pubCloudShipBookCfg?.appKey ?: ""
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
        Log.w(tag, log)
    }

    override fun w(tag: String, log: String, throwable: Throwable?) {
        Log.v(tag, log, throwable)
    }

    override fun e(tag: String, log: String) {
        Log.e(tag, log)
    }

    override fun e(tag: String, log: String, throwable: Throwable?) {
        Log.v(tag, log, throwable)
    }

    override fun trace(event: String, args: Map<String, String>) {
        Log.i(event, args.entries.joinToString())
    }

}