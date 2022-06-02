package com.example.teampel_1.ui.workout

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teampel_1.R
import com.example.teampel_1.databinding.FragmentWorkoutBinding
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        return root
    }

    private fun init() {
//        val date = getCurrentDateTime()
//        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
//        val textView = binding.root.findViewById<TextView>(R.id.timerDisplay)

        val dlgView = LayoutInflater.from(binding.root.context).inflate(R.layout.custom_dialog, null)
        val dlgWindow = AlertDialog.Builder(binding.root.context)
            .setView(dlgView)
            .setCancelable(false)

//      Set Custom Title
        val title = TextView(binding.root.context)
        title.setText("운동은 어떠셨나요?")
        title.setBackgroundColor(getColor(binding.root.context, R.color.colorCustom2))
        title.setPadding(0, 30, 0, 30)
        title.setGravity(Gravity.CENTER)
        title.setTextColor(getColor(binding.root.context, R.color.colorCustom))
        title.setTextSize(20F)

        binding.dlgView.setOnClickListener {
//          show dialog when TextView "VIEW DIALOG" is clicked
            dlgWindow.setCustomTitle(title)
            dlgWindow.show()
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}