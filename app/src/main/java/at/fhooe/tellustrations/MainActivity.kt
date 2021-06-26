package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var players = 5
        val textMessage = findViewById<TextView>(R.id.textView_acStart_message)
        val textPlayers = findViewById<TextView>(R.id.textView_acStart_players)
        val slider = findViewById<SeekBar>(R.id.seekBar_acStart_players)
        slider?.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                players = slider.progress + 3
                textPlayers.text = "Number of Players: " + players;
                if(players < 5) {
                    textMessage.text = "5 or more Players are recommended!"
                }else{
                    textMessage.text = ""
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val preferencePlayers = sharedPreferences(this)

        val btnStart = findViewById<Button>(R.id.button_acStart_start)
        btnStart.setOnClickListener{
            preferencePlayers.setPlayerAmount(players)
            preferencePlayers.setPlayerNr(2)
            val intentNextAc: Intent = Intent(this, ActivityFirstSentence::class.java)
            startActivity(intentNextAc)
        }
    }

}