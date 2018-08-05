package io.github.mrtry.pushnotificationtraining

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


/**
 * Created by mrtry on 2018/08/04.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived")

        // NotificationBuilderJobService.registerToJobScheduler(this)

        launch { displayNotification() }
    }

    private suspend fun displayNotification() {
        Log.d(NotificationBuilderJobService.TAG, "displayNotification() started")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        val image = getImage()

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Test")
                .setContentText("いえーい、みてるー？")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        launch(UI) {
            notificationManager!!.notify(0, notificationBuilder.build())

            Log.d(NotificationBuilderJobService.TAG, "displayNotification() display")
        }

        Log.d(NotificationBuilderJobService.TAG, "displayNotification() finished")
    }

    private suspend fun getImage(): Bitmap {
        val url = "http://phoenix-wind.com/common/img/OGP_60/character/ononoki_yotsugi.jpg"

        return GlideApp.with(this).load(url).submit().get().toBitmap()
    }

    // see: https://stackoverflow.com/a/48028456
    private fun Drawable.toBitmap(): Bitmap {
        if (this is BitmapDrawable) {
            return bitmap
        }

        val width = if (bounds.isEmpty) intrinsicWidth else bounds.width()
        val height = if (bounds.isEmpty) intrinsicHeight else bounds.height()

        return Bitmap.createBitmap(width.nonZero(), height.nonZero(), Bitmap.Config.ARGB_8888).also {
            val canvas = Canvas(it)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
        }
    }

    private fun Int.nonZero() = if (this <= 0) 1 else this

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }
}