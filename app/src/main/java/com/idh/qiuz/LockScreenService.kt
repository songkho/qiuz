package com.idh.qiuz

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

class LockScreenService : Service() {



    //화면이 꺼질때 브로드캐스트 메세지를 수신하는 리시버
    var receiver : ScreenOffReceiver? = null

    private val ANDROID_CHANNEL_ID = "com.akj.quizlocker"
    private val NOTIFICATION_ID = 9999


    //서비스가 최초 생성될때 컬백 함수

    override fun onCreate() {
        super.onCreate()


        if (receiver == null){
            receiver = ScreenOffReceiver()
            val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            registerReceiver(receiver, filter)

        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        if (intent != null){
            if (intent.action == null){


                if (receiver == null){
                    receiver = ScreenOffReceiver()
                    val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
                    registerReceiver(receiver, filter)
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val chan = NotificationChannel(ANDROID_CHANNEL_ID, "MyService", NotificationManager.IMPORTANCE_NONE)

            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE


            val manager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
            manager.createNotificationChannel(chan)


            val builder = Notification.Builder(this, ANDROID_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("SmartTracker Running")
            val notification = builder.build()


            startForeground(NOTIFICATION_ID, notification)

        }

        return Service.START_REDELIVER_INTENT








    }


    override fun onDestroy() {
        super.onDestroy()


        if (receiver != null){
            unregisterReceiver(receiver)
        }

    }





    override fun onBind(intent: Intent): IBinder? {

        return null



    }
}
