package com.doozez.doozez.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.FragmentHomeBinding
import com.doozez.doozez.ui.view.SummaryCardView
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var summaryViewWon: SummaryCardView = binding.summaryWon
        var summaryViewInvites: SummaryCardView = binding.summaryInvites
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            summaryViewWon.setText(it)
            summaryViewInvites.setText(it)
        })
        //getInvitationCount()
        binding.lineChart.lineColor= Color.parseColor("#96529C")
//        binding.lineChart.fillColor = Color.BLACK
        binding.lineChart.gradientFillColors =
            intArrayOf(
                Color.parseColor("#DC9ECB"),
                Color.WHITE
            )
        binding.lineChart.animation.duration = animationDuration
        binding.lineChart.onDataPointTouchListener = { index, _, _ ->
            binding.lineChartValue.text =
                lineSet.toList()[index]
                    .second
                    .toString()
        }
        binding.lineChart.animate(lineSet)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getInvitationCount() {
        val call = ApiClient.invitationService.getInvitations()
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    binding.summaryWon.setText(it.body().count.toString())
                }
            }
            onFailure = {
                Log.e("HomeFragment", it?.stackTrace.toString())
                Snackbar.make(binding.homeContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val lineSet = listOf(
            "label1" to 5f,
            "label2" to 4.5f,
            "label3" to 4.7f,
            "label4" to 3.5f,
            "label5" to 3.6f,
            "label6" to 7.5f,
            "label7" to 7.5f,
            "label8" to 10f,
            "label9" to 5f,
            "label10" to 6.5f,
            "label11" to 3f,
            "label12" to 4f
        )

        private const val animationDuration = 1000L
    }
}