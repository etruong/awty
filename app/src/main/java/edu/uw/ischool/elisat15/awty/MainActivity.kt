package edu.uw.ischool.elisat15.awty

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.uw.ischool.elisat15.awty.MyIntentService.Companion.REQUEST_SMS_SEND_PERMISSION
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),
                REQUEST_SMS_SEND_PERMISSION)
        }

        val startBtn = findViewById<Button>(R.id.start_btn)
        startBtn.setOnClickListener {

            val messageInput = findViewById<EditText>(R.id.message)
            val phoneInput = findViewById<EditText>(R.id.phone)
            val timeInput = findViewById<EditText>(R.id.timer)
            Log.v("main", messageInput.text.toString())
            if (checkInputs(messageInput, phoneInput, timeInput)) {
                if (startBtn.text == "STOP") {
                    startBtn.text = "START"
                    stopService(Intent(this, MyIntentService::class.java))
                    // stop service
                } else {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                        PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),
                            REQUEST_SMS_SEND_PERMISSION)
                    } else {
                        Log.v("main", "enter")
                        startBtn.text = "STOP"

                        val intent = Intent(this, MyIntentService::class.java)
                        intent.putExtra("message", messageInput.text.toString())
                        intent.putExtra("phone", phoneInput.text.toString())
                        intent.putExtra("duration", timeInput.text.toString().toInt())
                        startService(intent)
                    }
                }


            }
        }
    }

    private fun checkInputs(messageInput: EditText, phoneInput: EditText, timeInput: EditText): Boolean {
        return messageInput.text.toString() != ""
                && phoneInput.text.toString().length >= 10
                && phoneInput.text.toString() != ""
                && android.util.Patterns.PHONE.matcher(phoneInput.text.toString()).matches()
                && timeInput.text.toString() != "" && timeInput.text.toString().toInt() > 0
    }

}
