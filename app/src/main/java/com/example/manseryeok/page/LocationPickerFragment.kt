package com.example.manseryeok.page

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.manseryeok.utils.TimeDiffConstants
import com.example.manseryeok.adapter.LocationAdapter
import com.example.manseryeok.databinding.FragmentLocationPickerBinding


class LocationPickerFragment : DialogFragment() {
    private val binding by lazy { FragmentLocationPickerBinding.inflate(layoutInflater) }
    private val locations = TimeDiffConstants.timeZones
    val adapter by lazy { LocationAdapter(requireActivity(), locations) }
    lateinit var onLocationClickListener: LocationAdapter.OnLocationClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvLocation.adapter = adapter
        adapter.onLocationClickListener = onLocationClickListener

    }

    override fun onStart() {
        super.onStart()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val window: Window? = dialog!!.window
        window?.attributes = lp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.run {
            adapter.notifyDataSetChanged()
        }


        return binding.root
    }
}