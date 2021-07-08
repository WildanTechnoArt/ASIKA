package com.unpi.asika.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.fcm.NotificationOpenedHandler
import com.unpi.asika.utils.Constant.ONESIGNAL_APP_ID
import com.unpi.asika.utils.Constant.PLAY_SERVICES_RESOLUTION_REQUEST
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var mAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        prepare()
        checkPlayServices()
        checkUserLogin()
    }

    private fun prepare() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mAnalytics = FirebaseAnalytics.getInstance(this)
        mAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
        mAuth = FirebaseAuth.getInstance()

        GlideApp.with(this)
            .load(R.drawable.logo_asika_white)
            .into(img_asika)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(false)
        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandler(this))
    }

    private fun checkUserLogin() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                val thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(2500)
                        } catch (ex: InterruptedException) {
                            ex.printStackTrace()
                        } finally {
                            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
                thread.start()
            }
        }
    }

    private fun checkPlayServices() {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(
                    this, result,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)
    }
}