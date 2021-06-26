package at.fhooe.tellustrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import at.fhooe.tellustrations.databinding.ActivityResultsBinding
import at.fhooe.tellustrations.models.Card
import at.fhooe.tellustrations.models.Game
import at.fhooe.tellustrations.models.GameState

class ActivityResults : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityResultsBinding
    private var currentPlayer = 0
    private lateinit var game: Game
    private lateinit var cards: ArrayList<Card>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        game = GameState.getGame()
        cards = game.cards!!

        binding.textViewAcPlayerNr.text = "Player: 1"
        binding.textViewAcPaction.text = "said..."
        binding.textViewAcPSentence.text = cards[0].text

        binding.imageButtonLeft.setOnClickListener(this)
        binding.imageButtonRight.setOnClickListener(this)

        val btnDone = findViewById<Button>(R.id.button_acResults_done)
        btnDone.setOnClickListener {
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButton_left -> {
                currentPlayer--
                loadPlayer(currentPlayer)
            }

            R.id.imageButton_right -> {
                currentPlayer++
                loadPlayer(currentPlayer)
            }
        }
    }

    /**
     * loads a specific player from the cards list
     * */
    private fun loadPlayer(idx: Int) {
        if (idx >= 0 && idx < cards.size) {
            val card = cards[idx]

            binding.textViewAcPlayerNr.text = "Player: ${idx+1}"

            if (card.actId == 1) {
                binding.textViewAcPaction.text = "said..."
                binding.textViewAcPSentence.text = card.text
            }
            else if (card.actId % 2 == 0) {
                binding.textViewAcPaction.text = "drawed..."
                binding.textViewAcPSentence.text = card.text
            }
            else {
                binding.textViewAcPaction.text = "guessed..."
                binding.textViewAcPSentence.text = card.text
            }


            if (card.bitmap != null) {
                binding.imageViewAcBitmap.setImageBitmap(card.bitmap)
            }
        }
    }
}