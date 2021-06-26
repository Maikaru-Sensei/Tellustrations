package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class ActivitySketch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketch)

        val preference = sharedPreferences(this)
        var playerNr = preference.getPlayerNr()
        val maxPlayer = preference.getPlayerAmount()
        val playerX = findViewById<TextView>(R.id.textView_acSketch_PlayerX)
        playerX.text = "Player: $playerNr!"

        val msg = findViewById<TextView>(R.id.textView_acSketch_sentence)
        msg.text = preference.getStrBuffer()

        val btnDone = findViewById<Button>(R.id.button_acSketch_done)
        btnDone.setOnClickListener {
            if(playerNr == maxPlayer){
                val intentNextAc: Intent = Intent(this, ActivityResults::class.java)
                startActivity(intentNextAc)
            }else{
                Toast.makeText(this, "Please hand the Phone to the next Player!", Toast.LENGTH_LONG).show()
                preference.setPlayerNr(++playerNr)
                val intentNextAc: Intent = Intent(this, ActivityWrite::class.java)
                startActivity(intentNextAc)
            }
            finish()
        }
    }
}
