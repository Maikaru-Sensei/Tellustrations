package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.fhooe.tellustrations.models.Card
import at.fhooe.tellustrations.models.GameState

class ActivityFirstSentence : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var hide = false;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_sentance)

        val exampleText = findViewById<TextView>(R.id.textView_acFirstSentence_examples)
        val btnExamples = findViewById<Button>(R.id.Button_acFirstSentence_SentenceHints)
        btnExamples.setOnClickListener{
            if(!hide){
                hide = true;
                exampleText.text = "We recommend funny Insiders! Maybe something like: \n" +
                        "~MC20 has found its eternal Archnemesis! Mario~ \n \n" +
                        "Or try something random like: \n" +
                        "~Pineapple Pizzas will burn in Hell! >:-) ~"
                btnExamples.text = "Hide Examples"
            }else{
                hide = false;
                exampleText.text = ""
                btnExamples.text = "Examples Please?"
            }
        }

        val preference = sharedPreferences(this)
        val msg = findViewById<EditText>(R.id.editText_acFirstSentence_firstSentence)
        val btnDone = findViewById<Button>(R.id.button_acFirstSentence_done)
        btnDone.setOnClickListener{

            if(msg.text.isEmpty()){
                Toast.makeText(this,"Please enter a Sentence!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Please hand the Phone to the next Player!", Toast.LENGTH_LONG).show()
                preference.setStrBuffer(msg.text.toString())

                val game = GameState.getGame()
                val cards = game.cards
                val c = Card(msg.text.toString(), null, 1)
                cards?.set(0, c)

                GameState.setGame(game)

                val intentNextAc: Intent = Intent(this, ActivitySketch::class.java)
                startActivity(intentNextAc)
                finish()
            }
        }
    }
}