package io.github.datt16.monea_devtool.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme

class SensorListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DevToolMaterialTheme {
                    SensorItemCard()
                }
            }
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
                .clickable { findNavController().navigate(R.id.action_SensorList_to_Update) },
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

    @Preview(showBackground = true, name = "Card Preview")
    @Composable
    fun PreviewFun() {
        SensorItemCard(
            sensorName = "センサーの管理",
            info1 = "4J教室",
        )
    }
}