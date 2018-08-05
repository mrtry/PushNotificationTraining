package io.github.mrtry.pushnotificationtraining

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


/**
 * Created by mrtry on 2018/08/04.
 */
class PushBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(PushBroadcastReceiver::class.java.simpleName, "onReceived")

        NotificationBuilderJobService.registerToJobScheduler(context)
    }

}