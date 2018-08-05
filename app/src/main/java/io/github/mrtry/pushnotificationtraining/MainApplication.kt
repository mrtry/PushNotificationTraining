package io.github.mrtry.pushnotificationtraining

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging


/**
 * Created by mrtry on 2018/08/04.
 */
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("mytopic");
    }
}