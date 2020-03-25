package com.manegow.tesci

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.manegow.tesci.ui.splashscreen.DrawerLocker
import java.lang.Exception


class MainActivity() : AppCompatActivity(), DrawerLocker {

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_login,
                R.id.nav_home,
                R.id.nav_calendario,
                R.id.nav_ratings,
                R.id.nav_horary,
                R.id.nav_difusion,
                R.id.nav_about_us
            ), drawerLayout
        )

        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        val facebookItem = navigationView.menu.findItem(R.id.nav_facebook)
        facebookItem.setOnMenuItemClickListener {
            try {
                applicationContext.packageManager.getPackageInfo("com.facebook.katana", 0)
                val openFacebookApp = Intent(Intent.ACTION_VIEW)
                openFacebookApp.data = Uri.parse("fb://page/582310471809103")
                startActivity(openFacebookApp)
            } catch (e: Exception) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("http://www.facebook.com/Comunidad.Tesci")
                startActivity(openURL)
            }
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val twitterItem = navigationView.menu.findItem(R.id.nav_twitter)
        twitterItem.setOnMenuItemClickListener {
            try {
                applicationContext.packageManager.getPackageInfo("com.twitter.android", 0)
                val openTwitterApp = Intent(Intent.ACTION_VIEW)
                openTwitterApp.data = Uri.parse("twitter://user?user_id=1965092077")
                startActivity(openTwitterApp)
            } catch (e: Exception) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://twitter.com/ComunidadTESCI?s=20")
                startActivity(openURL)
            }
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val youtubeItem = navigationView.menu.findItem(R.id.nav_youtube)
        youtubeItem.setOnMenuItemClickListener {
            try {
                applicationContext.packageManager.getPackageInfo("com.google.android.youtube", 0)
                val openTwitterApp = Intent(Intent.ACTION_VIEW)
                openTwitterApp.data =
                    Uri.parse("https://www.youtube.com/channel/UCYa_gAb35yww-Soh4_Ar7Zw")
                startActivity(openTwitterApp)
            } catch (e: Exception) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.youtube.com/channel/UCYa_gAb35yww-Soh4_Ar7Zw")
                startActivity(openURL)
            }
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            true
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setDrawerLocked(enabled: Boolean) {
        if (enabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
}


