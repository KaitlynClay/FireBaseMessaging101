package com.example.firebasemessaging101

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MainActivity : AppCompatActivity() {

    val THE_INTERNET = 100
    val POST_IT = 200
    val ACCESS_ME = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForPermission(android.Manifest.permission.INTERNET, "Internet", THE_INTERNET)
        checkForPermission(android.Manifest.permission.POST_NOTIFICATIONS, "POST", POST_IT)
        checkForPermission(android.Manifest.permission.ACCESS_NETWORK_STATE, "Net", ACCESS_ME)
    }

    class MyFireBaseMessagingService: FirebaseMessagingService() {
        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)

            // Handle data payload of FCM messages.
            if (remoteMessage.data.isNotEmpty()) {
                // Handle the data message here.
            }

            // Handle notification payload of FCM messages.
            remoteMessage.notification?.let {
                // Handle the notification message here.
            }
        }


    }

    fun checkForPermission(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext,permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> {
                    showAskMsg(permission, name, requestCode)
                }
                else -> {
                    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun weOK(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name is refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name is granted", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            THE_INTERNET -> weOK("Post")
        }
    }

    private fun showAskMsg(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("I need permission for $name to be used in this app")
            setTitle("I need permission")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }
    }
}