package io.github.datt16.monea_devtool.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme

class SensorUpdateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DevToolMaterialTheme {
                    MainWindow()
                }
            }
        }
    }

    @Composable
    fun MainWindow() {
        var sensorName by remember {
            mutableStateOf("aaa")
        }
        var sensorX by remember {
            mutableStateOf("0")
        }
        var sensorY by remember {
            mutableStateOf("0")
        }
        var sensorColor by remember {
            mutableStateOf("orange")
        }

        Column() {

            Row(modifier = Modifier.padding(16.dp)) {
                SimpleTextInput("センサー名", value = sensorName, OnValueChange = { sensorName = it })
            }

            Spacer(modifier = Modifier.height(12.dp))
            Column(modifier = Modifier.padding(horizontal =  16.dp)) {
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

            Spacer(modifier = Modifier.height(24.dp))
            Column {
                Text(text = "sensorName: $sensorName")
                Text(text = "(X,Y): ($sensorX, $sensorY)")
                Text(text = "sensorColor: $sensorColor")
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

    @Composable
    fun SensorItemCard(
        sensorName: String = "センサーの管理",
        info1: String = "4J教室",
        info2: String = "",
        sensorId: String = "HANDSON"
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
                .clickable { TODO("sensorIdでセンサーの更新ページに遷移させる") },
            elevation = 4.dp,
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        text = sensorName,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text = info1,
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = info2,
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }

                }
            }
        }
    }

    @Preview(showBackground = true, name = "SUF Preview")
    @Composable
    fun PreviewFun() {
        DevToolMaterialTheme {
            MainWindow()
        }
    }
}