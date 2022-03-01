package org.classapp.peppaplan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    var SPLASH_TIME = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            val mySuperIntent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(mySuperIntent)
            finish()
        }, SPLASH_TIME.toLong())
    }
}