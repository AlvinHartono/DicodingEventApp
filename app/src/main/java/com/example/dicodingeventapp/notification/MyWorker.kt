package com.example.dicodingeventapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.local.entity.Event
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MyWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    companion object {
        const val TAG = "MyWorker"
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
        const val NOTIFICATION_ID = 1
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getEvent()
    }

    private fun getEvent(): Result {
        Log.d(TAG, "doWork: Start")
        Looper.prepare()
        val client = SyncHttpClient()

        val url = "https://event-api.dicoding.dev/events?active=-1&limit=1"
        Log.d(TAG, "getEvent: $url")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                // Jika koneksi berhasil
                val result = responseBody?.let { String(it) }
                Log.d(TAG, "onSuccess: $result")

                //parse
                try {
                    val jsonObject = JSONObject(result!!)
                    val listEvents = jsonObject.getJSONArray("listEvents")

                    if (listEvents.length() > 0) {
                        val eventJson = listEvents.getJSONObject(0)
                        val event = Event(
                            id = eventJson.getInt("id"),
                            name = eventJson.getString("name"),
                            summary = eventJson.getString("summary"),
                            description = eventJson.getString("description"),
                            imageLogo = eventJson.getString("imageLogo"),
                            mediaCover = eventJson.getString("mediaCover"),
                            category = eventJson.getString("category"),
                            ownerName = eventJson.getString("ownerName"),
                            cityName = eventJson.getString("cityName"),
                            quota = eventJson.getInt("quota"),
                            registrants = eventJson.getInt("registrants"),
                            beginTime = eventJson.getString("beginTime"),
                            endTime = eventJson.getString("endTime"),
                            link = eventJson.getString("link")
                        )

                        // Show notification with the event data
                        showNotificationEvent(event)
                    }
                    resultStatus = Result.success()
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: Gagal.....")
                    resultStatus = Result.failure()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "onFailure: ${error?.message}")
                resultStatus = Result.failure()
            }

        })
        return resultStatus as Result
    }

    private fun showNotificationEvent(
        latestEvent: Event
    ) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).setSmallIcon(
                R.drawable.ic_launcher_foreground
            )
                .setContentTitle(latestEvent.name)
                .setContentText("Event dimulai pada ${latestEvent.beginTime}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}