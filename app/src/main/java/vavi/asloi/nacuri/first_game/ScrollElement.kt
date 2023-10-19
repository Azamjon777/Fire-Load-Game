package vavi.asloi.nacuri.first_game

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import vavi.asloi.nacuri.R
import vavi.asloi.nacuri.first_game.Constants.listCountGambElement
import vavi.asloi.nacuri.first_game.Constants.max
import kotlin.random.Random

class ScrollElement : FrameLayout {
    private var count = 0
    private val dbshfvbsjfhv: Context


    lateinit var kbbff: ImageView
    lateinit var fxvxfb: ImageView

    private fun kmddcd() {
        LayoutInflater.from(context)
            .inflate(R.layout.gambling_element_layout, this)
        fxvxfb = rootView.findViewById(R.id.current_slot_element_animation)
        kbbff = rootView.findViewById(R.id.next_slot_element_animation)
        kbbff.translationY = height.toFloat()
    }

    fun stopRoll(
        liveDataPulseElements: MutableLiveData<MutableList<Int>>,
        listGambElements: MutableList<Int>
    ) {
        Log.d("TAGProject", "stopRoll: $listGambElements")
        val map = listGambElements.groupingBy { it }.eachCount()
        val maxInMap = map.values.maxOrNull()
        val mapFilter = map.filter { it.value == maxInMap }.entries.first()

        Log.d("TAGProject", "stopRoll: $mapFilter")
        Log.d("TAGProject", "stopRoll: $listCountGambElement")

        max = maxInMap ?: 0

        val listPulse = mutableListOf<Int>()

        listGambElements.forEachIndexed { index, element ->

            if (mapFilter.key == element) {
                listPulse.add(index)
            }
        }
        liveDataPulseElements.value = listPulse
    }


    fun rotateElement(
        countRool: Int, idElements: Int, countElementsInGame: Int,
        liveDataPulseElements: MutableLiveData<MutableList<Int>>,
        listGambElements: MutableList<Int>,
        resourcesElement: List<Int>
    ) {
        fxvxfb.animate().translationY(-fxvxfb.height.toFloat())
            .setDuration(50L).start()
        kbbff.translationY = kbbff.height.toFloat()
        kbbff.animate().translationY(0F).setDuration(50L)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    fxvxfb.visibility = View.GONE


                    val pair = elements(resourcesElement)
                    kbbff.setImageResource(pair.first)
                    if (count != countRool) {
                        rotateElement(
                            countRool,
                            idElements,
                            countElementsInGame,
                            liveDataPulseElements,
                            listGambElements,
                            resourcesElement
                        )
                        count++
                    } else {
                        count = 0
                        fxvxfb.translationY = 0F
                        listGambElements.add(pair.second)
                        if (idElements == countElementsInGame - 1) {
                            stopRoll(liveDataPulseElements, listGambElements)
                        }
                    }


                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationStart(animator: Animator) {
                    kbbff.visibility = View.VISIBLE
                }
            })
    }


    fun elements(resourcesElement: List<Int>): Pair<Int, Int> {
        val rand = Random.nextInt(0, 3)

        return Pair(resourcesElement[rand], rand)
    }


    constructor(contextElementsAdapter: Context) : super(contextElementsAdapter) {
        this.dbshfvbsjfhv = contextElementsAdapter
        kmddcd()
    }

    constructor(contextElementsAdapter: Context, attributeSet: AttributeSet) : super(
        contextElementsAdapter,
        attributeSet
    ) {
        this.dbshfvbsjfhv = contextElementsAdapter
        kmddcd()
    }

    constructor(contextElementsAdapter: Context, attributeSet: AttributeSet, styleDef: Int) : super(
        contextElementsAdapter,
        attributeSet,
        styleDef
    ) {
        this.dbshfvbsjfhv = contextElementsAdapter
        kmddcd()
    }
}