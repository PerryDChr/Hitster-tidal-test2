package com.perrydchr.hitstertidal

import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
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
                answerText.text =
                    "The Power of Love\nHuey Lewis & The News"
            }
        }

        answerText = TextView(this).apply {
            text = "Svar skjult"
            textSize = 22f
            setPadding(0, 50, 0, 0)
        }

        layout.addView(title)
        layout.addView(playButton)
        layout.addView(stopButton)
        layout.addView(answerButton)
        layout.addView(answerText)

        setContentView(layout)
    }

    private fun openTidal() {
        val tidalUrl =
            "https://tidal.com/browse/search?q=The%20Power%20of%20Love%20Huey%20Lewis"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tidalUrl))
        startActivity(intent)
    }

    private fun stopPlayback() {
        val audioManager =
            getSystemService(AUDIO_SERVICE) as AudioManager

        audioManager.dispatchMediaKeyEvent(
            KeyEvent(
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_MEDIA_PAUSE
            )
        )

        audioManager.dispatchMediaKeyEvent(
            KeyEvent(
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_MEDIA_PAUSE
            )
        )
    }
}
