package io.github.datt16.monea_devtool.ui

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.MainActivity
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.Record
import io.github.datt16.monea_devtool.data.SensorDataRepositoryImpl
import io.github.datt16.monea_devtool.models.Position
import io.github.datt16.monea_devtool.models.SensorData
import io.github.datt16.monea_devtool.models.SensorViewModel
import io.github.datt16.monea_devtool.models.Status
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme
import kotlinx.coroutines.flow.asStateFlow

class UpdateSensorFragment : Fragment() {

    private val args: UpdateSensorFragmentArgs by navArgs()
    private val model: SensorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DevToolMaterialTheme {
                    MainView()
                }
            }
        }
    }

    @Composable
    fun MainView() {
        val sensorData = model.sensorDataStateFlow.asStateFlow().collectAsState().value
        val target: SensorData = sensorData?.filter { it.id == args.id }?.get(0) ?: SensorData()

        var sensorName by remember {
            mutableStateOf(target.name)
        }
        var sensorX by remember {
            mutableStateOf(target.position.x.toString())
        }
        var sensorY by remember {
            mutableStateOf(target.position.y.toString())
        }
        var sensorColor by remember {
            mutableStateOf(target.color)
        }
        var description by remember {
            mutableStateOf(target.description)
        }

        Column() {

            Text(text = "id:${args.id}")

            Row(modifier = Modifier.padding(16.dp)) {
                SimpleTextInput("センサー名", value = sensorName, OnValueChange = { sensorName = it })
            }

            Spacer(modifier = Modifier.height(12.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SimpleTextInput("X位置", value = sensorX, OnValueChange = { sensorX = it })
                Spacer(modifier = Modifier.height(8.dp))
                SimpleTextInput("Y位置", value = sensorY, OnValueChange = { sensorY = it })
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "表示色",
                color = Color.Gray,
                style = MaterialTheme.typography.caption
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SimpleRadioButton(label = "オレンジ", selected = sensorColor == "orange") {
                    sensorColor = "orange"
                }
                SimpleRadioButton(label = "シアン", selected = sensorColor == "cyan") {
                    sensorColor = "cyan"
                }
                SimpleRadioButton(label = "ブルー", selected = sensorColor == "blue") {
                    sensorColor = "blue"
                }
            }

            Row(modifier = Modifier.padding(16.dp)) {
                SimpleTextInput(
                    "センサーの説明",
                    value = description,
                    OnValueChange = { description = it })
            }
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(onClick = {
                addDataToRDB(
                    args.id,
                    SensorData(
                        name = sensorName,
                        id = args.id,
                        color = sensorColor,
                        description = description,
                        position = Position(sensorX.toInt(), sensorY.toInt()),
                        status = Status(200, "OK")
                    )
                )
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "保存")
            }
        }

    }

    @Composable
    fun SimpleTextInput(label: String, value: String, OnValueChange: (String) -> Unit) {
        TextField(
            value = value,
            onValueChange = OnValueChange,
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun SimpleRadioButton(label: String, selected: Boolean, action: () -> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected, onClick = action)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }
    }

    private fun addDataToRDB(id: String, item: SensorData) {
        val db = Firebase.database
        val ref = db.getReference("v1/sensors/")
        ref.child(id).setValue(item).addOnSuccessListener {
            val context: Activity? = activity
            if (context != null) {
                androidx.appcompat.app.AlertDialog.Builder(context) // FragmentではActivityを取得して生成
                    .setTitle("送信完了")
                    .setMessage("${item.name} / id:${item.id}")
                    .setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                    .show()
            }
        }
        findNavController().navigate(R.id.action_sensor_update_done)
    }
}
