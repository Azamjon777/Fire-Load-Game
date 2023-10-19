package vavi.asloi.nacuri.second_game

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import vavi.asloi.nacuri.R

class SecondGameFragment : Fragment() {

    private lateinit var myImageView: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var toThirdGame: TextView
    private var score = 0
    private var timer: CountDownTimer? = null
    private val gameDurationMillis: Long = 30000 // 30 секунд
    private var isGameRunning = false
    private val fiveSecondsRemainingMillis: Long = 6000 // 5 секунд
    private var isTimerAnimated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_game, container, false)

        myImageView = view.findViewById(R.id.myImageView)
        scoreTextView = view.findViewById(R.id.scoreTextView)
        timerTextView = view.findViewById(R.id.timerTextView)
        toThirdGame = view.findViewById(R.id.to_third_game_btn)

        toThirdGame.setOnClickListener {
            findNavController().navigate(R.id.action_secondGameFragment_to_thirdGameFragment)
        }
        myImageView.setOnClickListener {
            handleImageViewClick()
        }

        startGame()

        return view
    }

    private fun startGame() {
        score = 0
        scoreTextView.text = "Score: $score"
        isGameRunning = true


        timer = object : CountDownTimer(gameDurationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                timerTextView.text = String.format("Time: %02d:%02d", seconds / 60, seconds % 60)
                generateRandomImageView()

                if (millisUntilFinished <= fiveSecondsRemainingMillis && !isTimerAnimated) {
                    animatedText()
                    isTimerAnimated = true
                }
            }

            override fun onFinish() {
                isGameRunning = false
                showGameOverDialog()
                isTimerAnimated = false // Сброс флага isTimerAnimated
            }
        }

        timer?.start()
    }

    private fun generateRandomImageView() {
        val screenWidth = requireActivity().window.decorView.width
        val screenHeight = requireActivity().window.decorView.height

        val imageViewWidth = myImageView.width
        val imageViewHeight = myImageView.height

        if (screenWidth > imageViewWidth + 20 && screenHeight > imageViewHeight + 20) {
            val density = resources.displayMetrics.density
            val marginTopInDp = 100 // Задайте желаемое значение отступа в dp
            val marginTopInPixels = (marginTopInDp * density).toInt()

            val randomX = (10..(screenWidth - imageViewWidth - 20)).random().toInt()
            val randomY =
                (marginTopInPixels..(screenHeight - imageViewHeight - 40)).random().toInt()

            val layoutParams = myImageView.layoutParams as RelativeLayout.LayoutParams
            layoutParams.leftMargin = randomX
            layoutParams.topMargin = randomY

            myImageView.layoutParams = layoutParams

            val randomValue = (0..99).random()

            if (randomValue < 40) {
                // Шарик будет появляться в 40% случаев
                myImageView.setImageResource(R.drawable.fruit1)
            } else if (randomValue < 70) {
                // Бомба будет появляться в 30% случаев
                myImageView.setImageResource(R.drawable.fruit2)
            } else {
                // Звезда будет появляться в оставшиеся 30% случаев
                myImageView.setImageResource(R.drawable.fruit3)
            }

            myImageView.visibility = View.VISIBLE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleImageViewClick() {
        if (isGameRunning) {
            val drawable = myImageView.drawable
            if (drawable != null) {
                val currentImage = drawable.constantState
                val fruit1Img =
                    resources.getDrawable(R.drawable.fruit1, null).constantState
                val fruit2Img = resources.getDrawable(R.drawable.fruit2, null).constantState
                val fruit3Img = resources.getDrawable(R.drawable.fruit3, null).constantState

                when (currentImage) {
                    fruit1Img -> increaseScore(10)
                    fruit2Img -> decreaseScore(15)
                    fruit3Img -> decreaseScore(5)
                }

                myImageView.visibility = View.INVISIBLE
            }
        }
    }

    private fun increaseScore(points: Int) {
        score += points
        scoreTextView.text = "Score: $score"
    }

    private fun decreaseScore(points: Int) {
        score -= points
        if (score < 0) score = 0
        scoreTextView.text = "Score: $score"
    }

    private fun showGameOverDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_game_over, null)
        dialogBuilder.setView(dialogView)

        val titleTextView = dialogView.findViewById<TextView>(R.id.titleTextView)
        val scoreTextView = dialogView.findViewById<TextView>(R.id.scoreTextView)
        val restartButton = dialogView.findViewById<Button>(R.id.restartButtonDialog)

        // Настройки элементов
        titleTextView.text = "Game Over"
        scoreTextView.text = "Your score: $score"

        val alertDialog = dialogBuilder.create()

        // Настройка фона AlertDialog
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertDialog.show()
        restartButton.setOnClickListener {
            startGame()
            alertDialog.dismiss() // Закрытие AlertDialog
        }
    }

    private fun animatedText() {
        timerTextView.animate().apply {
            duration = 1000
            scaleXBy(0.3f)
            scaleYBy(0.3f)
            withEndAction {
                timerTextView.animate().apply {
                    duration = 1000
                    scaleXBy(-0.3f)
                    scaleYBy(-0.3f)
                }.start()
            }
        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
