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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private var scannedSpotifyUrl: String? = null
    private var scannedTitle: String? = null
    private var scannedArtist: String? = null
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
    if (result.contents != null) {
    scannedSpotifyUrl = result.contents

    answerText.text = "Finder sang..."

    lookupSpotifyTrack(result.contents)
    }
    }

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

        val scanButton = Button(this).apply {
            text = "📷 SCAN QR-KODE"
            setOnClickListener {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan Hitster QR-koden")
            setBeepEnabled(true)
            setOrientationLocked(false)
        }

        barcodeLauncher.launch(options)
    }
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
        layout.addView(scanButton)
        layout.addView(playButton)
        layout.addView(stopButton)
        layout.addView(answerButton)
        layout.addView(answerText)

        setContentView(layout)
    }

    
    private fun openTidal() {
     if (scannedSpotifyUrl == null) {
    answerText.text = "Scan et Hitster-kort først"
    return
     }   
    val tidalIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://tidal.com/track/326022876")
        setPackage("com.aspiro.tidal")
    }

    try {
        startActivity(tidalIntent)
    } catch (e: Exception) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://tidal.com/track/326022876")
            )
        )
    }

    window.decorView.postDelayed({
        val audioManager =
            getSystemService(AUDIO_SERVICE) as AudioManager

        audioManager.dispatchMediaKeyEvent(
            KeyEvent(
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_MEDIA_PLAY
            )
        )

        audioManager.dispatchMediaKeyEvent(
            KeyEvent(
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_MEDIA_PLAY
            )
        )

        window.decorView.postDelayed({
            val returnIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(returnIntent)
        }, 1000)

    }, 1500)
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
 private fun lookupSpotifyTrack(spotifyUrl: String) {
    Thread {
        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(
                    "https://open.spotify.com/oembed?url=" +
                        java.net.URLEncoder.encode(
                            spotifyUrl,
                            "UTF-8"
                        )
                )
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (!response.isSuccessful || body == null) {
                throw Exception("Spotify lookup failed")
            }

            val json = JSONObject(body)
            val title = json.optString("title")
            val artist = json.optString("author_name")

            scannedTitle = title
            scannedArtist = artist
            val tidalSearchUrl =
    "https://tidal.com/browse/search?q=" +
        java.net.URLEncoder.encode(
            "$title $artist",
            "UTF-8"
        )

            Handler(Looper.getMainLooper()).post {
                answerText.text =
                    "Spotify fundet:\n$title\n$artist"
            }

        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Kunne ikke hente Spotify-data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }.start()
 }   
}
