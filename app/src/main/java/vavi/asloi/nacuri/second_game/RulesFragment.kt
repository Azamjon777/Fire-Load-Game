package vavi.asloi.nacuri.second_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import vavi.asloi.nacuri.R

class RulesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val okBtn = view.findViewById<TextView>(R.id.ok_btn)
        okBtn.setOnClickListener {
            findNavController().navigate(R.id.action_rulesFragment_to_secondGameFragment)
        }
    }
}