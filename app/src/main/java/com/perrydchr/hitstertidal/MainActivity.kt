package com.perrydchr.hitstertidal

import android.content.Intent
import android.media.AudioManager
import android.media.KeyEvent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var answerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 80, 40, 40)
        }

        val title = TextView(this).apply {
            text = "HITSTER TIDAL TEST"
            textSize = 28f
        }

        val playButton = Button(this).apply {
            text = "▶ AFSPIL I TIDAL"
            setOnClickListener {
                openTidal()
            }
        }

        val stopButton = Button(this).apply {
            text = "■ STOP"
            setOnClickListener {
                stopPlayback()
            }
        }

        val answerButton = Button(this).apply {
            text = "VIS SVAR"
            setOnClickListener {
