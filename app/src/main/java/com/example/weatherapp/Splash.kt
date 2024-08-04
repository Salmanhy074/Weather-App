package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        /*Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent  = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },3000);*/

        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottieAnimationViewSplash)

        // Set the duration for the splash screen
        val splashScreenDuration = 5000L // 3 seconds

        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity after the splash screen duration
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashScreenDuration)

    }
}