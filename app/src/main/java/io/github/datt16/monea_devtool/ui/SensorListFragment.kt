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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.data.SensorDataRepositoryImpl
import io.github.datt16.monea_devtool.models.SensorData
import io.github.datt16.monea_devtool.models.SensorViewModel
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme
import kotlinx.coroutines.flow.asStateFlow
import java.lang.IllegalStateException

class SensorListFragment : Fragment() {

    private val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                DevToolMaterialTheme {
                    SensorListView()
                }
            }
        }
    }

    @Composable
    fun SensorListView(
        sensorViewModel: SensorViewModel = viewModel(
            factory = SensorViewModeFactory(SensorDataRepositoryImpl(database))
        )
    ) {
        val sensors = sensorViewModel.sensorDataStateFlow.asStateFlow().collectAsState()

        Column(modifier = Modifier.padding(16.dp)) {
            if (sensors.value == null) {
                Text(text = "Now Loading")
            } else {
                sensors.value?.map {
                    SensorItemCard(sensor = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }

    @Composable
    fun SensorItemCard(
        sensor: SensorData,
        info1: String = "4J教室",
        info2: String = "",
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .clickable { findNavController().navigate(R.id.action_SensorList_to_Update) },
            elevation = 4.dp,
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        text = sensor.name,
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
}

class SensorViewModeFactory(private val repo: SensorDataRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) return SensorViewModel(repo) as T
        throw IllegalStateException()
    }
}