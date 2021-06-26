package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ActivityWrite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val image = findViewById<ImageView>(R.id.imageView_acWrite_Image)
        //image.setImageBitmap()//TODO
        //image.setImageResource()//TODO or this

        val preference = sharedPreferences(this)
        var playerNr = preference.getPlayerNr()
        val maxPlayer = preference.getPlayerAmount()

        val playerX = findViewById<TextView>(R.id.textView_acWrite_PlayerX)
        playerX.text = "Player: $playerNr!"

        val msg = findViewById<EditText>(R.id.editText_acWrite_sentence)

        val btnDone = findViewById<Button>(R.id.button_acWrite_done)
        btnDone.setOnClickListener{
            if(msg.text.isEmpty()){
                Toast.makeText(this,"Please enter a Sentence!", Toast.LENGTH_LONG).show()
            }else {
                if(playerNr == maxPlayer){
                    val intentNextAc: Intent = Intent(this, ActivityResults::class.java)
                    startActivity(intentNextAc)
                }else{
                    Toast.makeText(this, "Please hand the Phone to the next Player!", Toast.LENGTH_LONG).show()
                    preference.setPlayerNr(++playerNr)
                    preference.setStrBuffer(msg.text.toString())
                    val intentNextAc: Intent = Intent(this, ActivitySketch::class.java)
                    startActivity(intentNextAc)
                }
                finish()
            }
        }
    }
}