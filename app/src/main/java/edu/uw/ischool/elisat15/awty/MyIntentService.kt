package edu.uw.ischool.elisat15.awty

import android.Manifest
import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.widget.Toast
import java.util.*
import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager


class MyIntentService: IntentService("awtyService") {

    companion object {
        val REQUEST_SMS_SEND_PERMISSION = 1234
        val REQUEST_CALL_PHONE_PERMISSION = 5678
    }

    private val TAG = "IntentService"
    private lateinit var mHandler: Handler
    private var duration: Int = 0
    private lateinit var message: String
    private var run: Boolean = true

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "Create intent service!")
        mHandler = Handler()

        duration = intent!!.getIntExtra("duration", 0)
        Log.v("service", "" + duration)
        val phone = "(" + intent!!.getStringExtra("phone").substring(0, 3) + ") " +
                intent!!.getStringExtra("phone").substring(3, 6) + "-" +
                intent!!.getStringExtra("phone").substring(6)
        message = phone + ": " + intent!!.getStringExtra("message")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.v(TAG, "Handling Intent")
        while(run) {

            mHandler.post {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                val smsManager = SmsManager.getDefault()
                val number = intent!!.getStringExtra("phone")
                smsManager.sendTextMessage(
                    number,
                    null,
                    intent!!.getStringExtra("message"),
                    null,
                    null
                )
                Log.v("Hello", "justSent!")

            }

            try {
                // Currently set for user to input duration in seconds will
                // change to minutes after demo (60,000 ms)
                Thread.sleep((duration * 60000).toLong())
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun onDestroy() {
        run = false
        super.onDestroy()
    }

}