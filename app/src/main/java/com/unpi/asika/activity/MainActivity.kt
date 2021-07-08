package com.unpi.asika.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.onesignal.OneSignal
import com.unpi.asika.R
import com.unpi.asika.fcm.NotificationOpenedHandler
import com.unpi.asika.fragment.HomeFragment
import com.unpi.asika.fragment.NewsFragment
import com.unpi.asika.fragment.ProfileFragment
import com.unpi.asika.fragment.TodoListFragment
import com.unpi.asika.utils.Constant
import com.unpi.asika.utils.Constant.KEY_FRAGMENT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var pageContent: Fragment? = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init(savedInstanceState)
        firstMenuSelected()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        pageContent?.let { supportFragmentManager.putFragment(outState, KEY_FRAGMENT, it) }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.home -> {
                pageContent = HomeFragment()
            }
            R.id.todo_list -> {
                pageContent = TodoListFragment()
            }
            R.id.news -> {
                pageContent = NewsFragment()
            }
            R.id.profile -> {
                pageContent = ProfileFragment()
            }
        }
        pageContent?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_pager, it)
                .commit()
        }
        return true
    }

    private fun firstMenuSelected() {
        pageContent?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_pager, it)
                .commit()
        }
    }

    private fun init(savedInstanceState: Bundle?) {
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(Constant.ONESIGNAL_APP_ID)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(false)
        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandler(this))

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            pageContent?.let {
                supportFragmentManager.beginTransaction().replace(R.id.view_pager, it).commit()
            }
        } else {
            pageContent = supportFragmentManager.getFragment(savedInstanceState, KEY_FRAGMENT)
            pageContent?.let {
                supportFragmentManager.beginTransaction().replace(R.id.view_pager, it).commit()
            }
        }
    }
}