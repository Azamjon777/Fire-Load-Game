package molqu.alpani.cxipan.second_game

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import vavi.asloi.nacuri.R
import java.util.Random

class ThirdGameFragment : Fragment() {
    private lateinit var playerScoreTextView: TextView
    private lateinit var computerScoreTextView: TextView
    private lateinit var userImageView: ImageView
    private lateinit var computerImageView: ImageView
    private lateinit var resultTextView: TextView

    private var playerScore: Int = 0
    private var computerScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_third_game, container, false)

        playerScoreTextView = rootView.findViewById(R.id.playerScoreTextView)
        computerScoreTextView = rootView.findViewById(R.id.computerScoreTextView)
        userImageView = rootView.findViewById(R.id.userImageView)
        computerImageView = rootView.findViewById(R.id.computerImageView)
        resultTextView = rootView.findViewById(R.id.resultTextView)

        rootView.findViewById<TextView>(R.id.rockButton).setOnClickListener { play("Rock") }
        rootView.findViewById<TextView>(R.id.paperButton).setOnClickListener { play("Paper") }
        rootView.findViewById<TextView>(R.id.scissorsButton).setOnClickListener { play("Scissors") }

        return rootView
    }

    private fun play(userChoice: String) {
        val options = arrayOf("Rock", "Paper", "Scissors")
        val computerChoice = options[Random().nextInt(options.size)]

        val userImage = when (userChoice) {
            "Rock" -> R.drawable.rock
            "Paper" -> R.drawable.paper
            "Scissors" -> R.drawable.scissors
            else -> null
        }
        val computerImage = when (computerChoice) {
            "Rock" -> R.drawable.rock
            "Paper" -> R.drawable.paper
            "Scissors" -> R.drawable.scissors
            else -> null
        }

        userImageView.setImageResource(userImage!!)
        computerImageView.setImageResource(computerImage!!)

        when {
            userChoice == computerChoice -> {
                resultTextView.text = "It's a tie!"
                updateScoreText()
            }

            userChoice == "Rock" && computerChoice == "Scissors" -> {
                resultTextView.text = "You win!"
                playerScore++
                updateScoreText()
            }

            userChoice == "Paper" && computerChoice == "Rock" -> {
                resultTextView.text = "You win!"
                playerScore++
                updateScoreText()
            }

            userChoice == "Scissors" && computerChoice == "Paper" -> {
                resultTextView.text = "You win!"
                playerScore++
                updateScoreText()
            }

            else -> {
                resultTextView.text = "You lose!"
                computerScore++
                updateScoreText()
            }
        }

        if (playerScore == 10 || computerScore == 10) {
            showAlertDialog()
        }
    }

    private fun updateScoreText() {
        playerScoreTextView.text = "Player: $playerScore"
        computerScoreTextView.text = "Computer: $computerScore"
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_game_over, null)
        dialogBuilder.setView(dialogView)

        val titleTextView = dialogView.findViewById<TextView>(R.id.titleTextView)
        val yourScoreTextView = dialogView.findViewById<TextView>(R.id.your_score_text)
        val compScoreTextView = dialogView.findViewById<TextView>(R.id.comp_score_text)
        val restartButton = dialogView.findViewById<Button>(R.id.restartButtonDialog)

        // Настройка элементов
        titleTextView.text = "Game Over"
        yourScoreTextView.text = "Your score: $playerScore"
        compScoreTextView.text = "Computer score: $computerScore"

        val alertDialog = dialogBuilder.create()

        // Настройка фона AlertDialog
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_frame)

        alertDialog.show()
        restartButton.setOnClickListener {
            restartGame()
            alertDialog.dismiss() // Закрытие AlertDialog
        }
    }

    private fun restartGame() {
        playerScore = 0
        computerScore = 0
        resultTextView.text = getString(R.string.choose_which_one)
        userImageView.setImageResource(0)
        computerImageView.setImageResource(0)
        updateScoreText()
    }
}
