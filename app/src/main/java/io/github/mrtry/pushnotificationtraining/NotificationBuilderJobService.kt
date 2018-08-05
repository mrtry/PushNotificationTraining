package io.github.mrtry.pushnotificationtraining

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


/**
 * Created by mrtry on 2018/08/04.
 */
class NotificationBuilderJobService : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob")
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob")

        launch { displayNotification() }

        return true
    }

    private suspend fun displayNotification() {
        Log.d(TAG, "displayNotification() started")

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

            Log.d(TAG, "displayNotification() display")
        }

        Log.d(TAG, "displayNotification() finished")
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
        val TAG = NotificationBuilderJobService::class.java.simpleName

        fun registerToJobScheduler(context: Context) {
            val jobInfo = JobInfo.Builder(2989, ComponentName(context, NotificationBuilderJobService::class.java))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setOverrideDeadline(0)
                    .build()

            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            scheduler.schedule(jobInfo)
        }
    }
}