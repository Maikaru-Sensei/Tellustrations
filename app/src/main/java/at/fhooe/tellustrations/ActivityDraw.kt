package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class ActivityDraw : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        val preferencePlayerNr = sharedPreferences(this)
        var playerNr = preferencePlayerNr.getPlayerNr()
        val playerX = findViewById<TextView>(R.id.textView_acDraw_PlayerX)
        playerX.text = "Player: $playerNr!"

        val sentence = findViewById<TextView>(R.id.textView_acDraw_SentenceDisplay)
        sentence.text = "" //TODO! Buffer for msg

        val btnDone = findViewById<Button>(R.id.button_acFirstSentence_done)
        btnDone.setOnClickListener{
            Toast.makeText(this,"Please hand the Phone to the next Player!", Toast.LENGTH_LONG).show()
            preferencePlayerNr.setPlayerNr(++playerNr)
            val intentNextAc: Intent = Intent(this, ActivityWrite::class.java)
            startActivity(intentNextAc)
            finish()
        }


    }
}