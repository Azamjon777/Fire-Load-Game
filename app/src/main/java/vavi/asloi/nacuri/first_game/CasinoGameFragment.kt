package vavi.asloi.nacuri.first_game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vavi.asloi.nacuri.R
import vavi.asloi.nacuri.databinding.FragmentCasinoGameBinding
import vavi.asloi.nacuri.first_game.Constants.listCountGambElement
import vavi.asloi.nacuri.first_game.Constants.max
import kotlin.random.Random
import kotlin.random.nextInt

class CasinoGameFragment : Fragment() {

    private lateinit var elemAll: List<ScrollElement>
    private lateinit var elemBut: Array<TextView>
    private var repeat = 15
    private var bal = 0
    private var durationPulse = 200L
    private lateinit var binding: FragmentCasinoGameBinding
    private lateinit var txtBal: TextView
    private lateinit var btnToSecondGame: TextView
    private lateinit var btnToThirdGame: TextView

    private val listResources = listOf(
        R.drawable.symbol1,
        R.drawable.symbol2,
        R.drawable.symbol3,
        R.drawable.symbol4,
        R.drawable.symbol5
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCasinoGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
        binding.nextGameBtn.setOnClickListener {
            findNavController().navigate(R.id.action_casino_game_to_rulesFragment)
        }

        binding.toThirdFromCasino.setOnClickListener {
            findNavController().navigate(R.id.action_casino_game_to_thirdGameFragment)
        }

        click(elemBut[0], elemAll, elemAll.size, 10) // Замените значение count на нужное вам

        elemAll.forEach { el ->
            el.setOnClickListener {
                lifecycleScope.launch {
                    disableButtons(false)
                    animateElement(el, 5, 500)
                    animatedText(btnToSecondGame, btnToThirdGame)
                    recordWinToBalance(150)
                    disableButtons(true)
                }
            }
        }
    }

    private fun animatedText(vararg textViews: TextView) {
        val animatorSet = AnimatorSet()

        val animators = textViews.map { textView ->
            val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
                textView,
                PropertyValuesHolder.ofFloat("scaleX", textView.scaleX * 1.3f),
                PropertyValuesHolder.ofFloat("scaleY", textView.scaleY * 1.3f)
            )
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                textView,
                PropertyValuesHolder.ofFloat("scaleX", textView.scaleX),
                PropertyValuesHolder.ofFloat("scaleY", textView.scaleY)
            )
            scaleUp.duration = 1000
            scaleDown.startDelay = scaleUp.duration
            scaleDown.duration = 500
            scaleUp.repeatCount = 0
            scaleDown.repeatCount = 0
            scaleUp.repeatMode = ObjectAnimator.REVERSE
            scaleDown.repeatMode = ObjectAnimator.REVERSE
            AnimatorSet().apply {
                playSequentially(scaleUp, scaleDown)
            }
        }

        animatorSet.playTogether(animators)
        animatorSet.start()
    }

    private fun initialize() {
        elemAll = List(5 * 3) {
            val element = ScrollElement(requireContext())
            val height = 59 * resources.displayMetrics.density
            val width = 59 * resources.displayMetrics.density
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.height = height.toInt()
            layoutParams.width = width.toInt()
            element.layoutParams = layoutParams
            element.setPadding(10, 10, 10, 10)
            element
        }
        txtBal = binding.txtBal
        btnToSecondGame = binding.nextGameBtn
        btnToThirdGame = binding.toThirdFromCasino
        elemAll.forEach {
            binding.gridElements.addView(it)
        }

        elemBut = arrayOf(binding.burRoll)

        bal = Random.nextInt(0..3400)
        txtBal.text = bal.toString()

    }

    private fun disableButtons(isClickable: Boolean) {
        elemBut.forEach {
            it.isClickable = isClickable
        }
        elemAll.forEach {
            it.isClickable = isClickable
        }
    }

    private fun clearLiveData() {
        listCountGambElement.clear()
    }

    private fun click(
        button: TextView,
        elements: List<ScrollElement>,
        countElem: Int,
        count: Int
    ) {
        button.setOnClickListener {
            recordWinToBalance(-250)
            repeat = 5
            durationPulse = Random.nextInt(200, 1200).toLong()
            clearLiveData()
            val liveDataPulseElements = MutableLiveData<MutableList<Int>>()
            val listGambElements = mutableListOf<Int>()

            disableButtons(false)

            var idElements = 0
            val totalCount = elements.size

            elements.forEach {
                it.rotateElement(
                    count,
                    idElements,
                    totalCount,
                    liveDataPulseElements,
                    listGambElements,
                    listResources
                )
                idElements++
            }

            liveDataPulseElements.observe(viewLifecycleOwner, Observer { indices ->
                val win = getScoreWin(max)
                recordWinToBalance(win)

                indices.forEach { index ->
                    lifecycleScope.launch {
                        animateElement(elements[index], repeat, durationPulse)
                    }
                }

                lifecycleScope.launch {
                    delay(repeat * durationPulse)
                    disableButtons(true)
                    animateButton(binding.nextGameBtn, 5, 1000)
                }
            })
        }
    }

    private fun getScoreWin(countElem: Int): Int {
        return countElem * 100
    }

    private fun recordWinToBalance(win: Int) {
        bal += win
        txtBal.text = bal.toString()
    }

    private fun animateElement(element: View, repeat: Int, duration: Long) {
        val pulseAnimation: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            element,
            PropertyValuesHolder.ofFloat("scaleX", 1.3f),
            PropertyValuesHolder.ofFloat("scaleY", 1.3f),
        )

        pulseAnimation.repeatCount = repeat
        pulseAnimation.duration = duration
        pulseAnimation.repeatMode = ObjectAnimator.REVERSE

        pulseAnimation.start()
    }

    private fun animateButton(button: View, repeat: Int, duration: Long) {
        val pulseAnimation: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            button,
            PropertyValuesHolder.ofFloat("scaleX", 1.3f),
            PropertyValuesHolder.ofFloat("scaleY", 1.3f),
        )

        pulseAnimation.repeatCount = repeat
        pulseAnimation.duration = duration
        pulseAnimation.repeatMode = ObjectAnimator.REVERSE

        pulseAnimation.start()
    }
}
