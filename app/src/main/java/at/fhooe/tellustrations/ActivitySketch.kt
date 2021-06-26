package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import at.fhooe.tellustrations.models.Card
import at.fhooe.tellustrations.models.GameState
import at.fhooe.tellustrations.ui.DrawFragment

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

        val fragment = DrawFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.setReorderingAllowed(true)
        ft.replace(R.id.fragmentContainerView2, fragment)
        ft.commit()

        val btnDone = findViewById<Button>(R.id.button_acSketch_done)
        btnDone.setOnClickListener {
            val bmp = fragment.getBitmap()

            val game = GameState.getGame()
            val cards = game.cards
            val c = Card(msg.text.toString(), bmp, playerNr)
            cards?.set(playerNr-1, c)

            GameState.setGame(game)

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
