package io.github.datt16.monea_devtool.ui

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.Record
import io.github.datt16.monea_devtool.databinding.FragmentAddRecordBinding
import java.time.LocalDateTime
import java.time.ZoneId

class AddRecordFragment : Fragment() {

    var id = ""

    private var _binding: FragmentAddRecordBinding? = null
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecordBinding.inflate(inflater, container, false)

        val submitBtn = binding.submitBtn
        submitBtn.setOnClickListener {
            validate()
        }

        binding.idEt.addTextChangedListener {
            binding.addressTv.text = "HANDSON/records/" + binding.idEt.text
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun validate() {
        val co2 = binding.co2Et.text.toString().trim()
        val humid = binding.humidEt.text.toString().trim()
        val temp = binding.tempEt.text.toString().trim()
        val pressure = binding.pressureEt.text.toString().trim()
        id = binding.idEt.text.toString().trim()

        Log.d("ADD_RDB", "================= OK")
        if (TextUtils.isEmpty(id)) {
            binding.idTil.error = "idを入力してください"
        } else if (TextUtils.isEmpty(co2)) {
            binding.co2Til.error = "入力してください"
        } else if (TextUtils.isEmpty(humid)) {
            binding.humidTil.error = "入力してください"
        } else if (TextUtils.isEmpty(pressure)) {
            binding.pressureTil.error = "入力してください"
        } else if (TextUtils.isEmpty(temp)) {
            binding.tempTil.error = "入力してください"
        } else {
            var created: Long = 0

            val s = id.replace("_", "T")
            val S = LocalDateTime.parse(s)
            created = S.atZone(ZoneId.systemDefault()).toEpochSecond()

            val record = Record(
                co2.toInt(),
                created.toInt(),
                humid.toInt(),
                pressure.toInt(),
                temp.toInt()
            )
            addDataToRDB(id, record)
            findNavController().navigate(R.id.action_add_done)
        }
    }

    private fun addDataToRDB(id: String, item: Record) {
        Log.d("ADD_RDB", "OK")
        val db = Firebase.database
        val ref = db.getReference("v1/records/sensorId/HANDSON/records/")
        ref.child(id).setValue(item).addOnSuccessListener {
            val context: Activity? = activity
            if (context != null) {
                AlertDialog.Builder(context) // FragmentではActivityを取得して生成
                    .setTitle("送信完了")
                    .setMessage("")
                    .setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                    .show()
            }
        }
    }
}
