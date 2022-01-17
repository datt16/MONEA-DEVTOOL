package io.github.datt16.monea_devtool.ui

import android.app.Activity
import android.content.DialogInterface
import android.hardware.Sensor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.data.SensorDataRepositoryImpl
import io.github.datt16.monea_devtool.models.*
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme
import kotlinx.coroutines.flow.asStateFlow

class UpdateRoomFragment : Fragment() {

    val database = Firebase.database

    private val args: UpdateRoomFragmentArgs by navArgs()
    private val model: RoomViewModel by activityViewModels()
    private val sensorModel: SensorViewModel by activityViewModels {
        SensorViewModeFactory(
            SensorDataRepositoryImpl(database)
        )
    }

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
        val roomData = model.roomDataStateFlow.asStateFlow().collectAsState().value
        val sensorData = sensorModel.sensorDataStateFlow.asStateFlow().collectAsState().value

        val target: RoomData = roomData?.filter { it.id == args.id }?.get(0) ?: RoomData()

        var roomName by remember {
            mutableStateOf(target.name)
        }

        var sensorIds by remember {
            mutableStateOf(target.sensors)
        }

        Column() {

            Text(text = "id:${args.id}")

            Row(modifier = Modifier.padding(16.dp)) {
                SimpleTextInput("センサー名", value = roomName, OnValueChange = { roomName = it })
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "センサー",
                color = Color.Gray,
                style = MaterialTheme.typography.caption
            )

            Spacer(modifier = Modifier.height(12.dp))
            if (sensorData != null) {
                sensorData.map { sensor ->
                    var selected by remember {
                        mutableStateOf(sensorIds.contains(sensor.id))
                    }
                    Row {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = {
                                if (selected) {
                                    sensorIds = sensorIds.filter { it != sensor.id }
                                    selected = false
                                } else {
                                    sensorIds += sensor.id
                                    selected = true
                                }
                            })
                        Text(text = sensor.id)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Text(text = "Loading...")
            }

            Text(text = "selected")
            sensorIds.map {
                Row {
                    Text(text = it)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(onClick = {
                addDataToRDB(
                    args.id,
                    RoomData(
                        name = roomName,
                        id = args.id,
                        sensors = sensorIds
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

    private fun addDataToRDB(id: String, item: RoomData) {
        val db = Firebase.database
        val ref = db.getReference("v1/rooms/roomId/")
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
        findNavController().navigate(R.id.action_updateRoomFragment_to_roomListFragment)
    }
}
