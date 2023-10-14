package com.example.sbcalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView

class SecondLayout : AppCompatActivity() {
    private lateinit var sbText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_layout)
        sbText = findViewById(R.id.sbText)
    }

    fun animationPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun animateSplit(view: View) {
        sbText.visibility = View.VISIBLE
        view.animate().apply {
            duration = 1000
            rotationBy(360f)
            alpha(0f)
        }.withEndAction {
            view.animate().apply {
                duration = 1000
                rotationBy(-360f)
                alpha(1f)
            }
            Handler().postDelayed({
                sbText.visibility = View.INVISIBLE
            }, 1000)
        }
    }


}