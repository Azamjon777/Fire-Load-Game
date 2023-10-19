package vavi.asloi.nacuri.start

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import vavi.asloi.nacuri.R

class StartFragment : Fragment() {
    private val delayMillis: Long = 3000 // Время задержки перед переходом (3 секунды)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        // Запускаем прогресс-бар и переходим на vavi.asloi.nacuri.first_game.CasinoGameFragment через delayMillis
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_startFragment_to_casino_game)
        }, delayMillis)

        return view
    }
}
