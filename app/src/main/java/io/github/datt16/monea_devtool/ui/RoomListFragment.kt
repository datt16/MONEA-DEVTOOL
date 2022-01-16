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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.data.RoomDataRepositoryImpl
import io.github.datt16.monea_devtool.models.RoomData
import io.github.datt16.monea_devtool.models.RoomViewModel
import io.github.datt16.monea_devtool.models.SensorViewModel
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme
import kotlinx.coroutines.flow.asStateFlow
import java.lang.IllegalStateException

class RoomListFragment : Fragment() {

    private val database = Firebase.database
    private val model: RoomViewModel by activityViewModels{RoomListViewModelFactory(
        RoomDataRepositoryImpl(database)
    )}

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
        sensorViewModel: RoomViewModel = model,
    ) {
        val sensors = sensorViewModel.roomDataStateFlow.asStateFlow().collectAsState()

        Column(modifier = Modifier.padding(16.dp)) {
            if (sensors.value == null) {
                Text(text = "Now Loading...")
            } else {
                sensors.value?.map {
                    SensorItemCard(room = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @Composable
    fun SensorItemCard(
        room: RoomData,
    ) {
        val action = SensorListFragmentDirections.actionSensorListToUpdate(room.id)

        Card(
            modifier = Modifier
                .wrapContentSize()
                .clickable { findNavController().navigate(action) },
            elevation = 4.dp,
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        text = room.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text = room.id,
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

class RoomListViewModelFactory(private val repo: RoomDataRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) return RoomViewModel(repo) as T
        throw IllegalStateException()
    }
}
