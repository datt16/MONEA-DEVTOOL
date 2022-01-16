package io.github.datt16.monea_devtool.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.Record
import io.github.datt16.monea_devtool.data.RecordRepositoryImpl
import io.github.datt16.monea_devtool.databinding.FragmentMainBinding
import io.github.datt16.monea_devtool.models.RecordViewModel
import kotlinx.coroutines.flow.asStateFlow
import java.lang.IllegalStateException

class MainFragment : Fragment() {
    private val database = Firebase.database

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    var fab: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
//        context?.let { setup(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recordListView: ComposeView = binding.recordListView

        recordListView.setContent {
            AppCompatTheme {
                Column(modifier = Modifier.padding(all = 4.dp)) {
                    Row() {
                        Text("日時")
                        Text("CO2")
                        Text("気温")
                        Text("湿度")
                        Text("気圧")
                    }
                    Spacer(modifier = Modifier.height(2.dp))

                    RecordListView()
                }
            }
        }


        fab = binding.mainFab
        fab?.setOnClickListener {
            findNavController().navigate(R.id.action_add_record)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.fab = null
    }


    // インスタンス化の際にViewModeもインスタンス化
    @Composable
    private fun RecordListView(
        recordViewModel: RecordViewModel = viewModel(
            factory = RecordViewModelFactory(
                RecordRepositoryImpl(database)
            )
        )
    ) {
        val records = recordViewModel.recordsStateFlow.asStateFlow().collectAsState()

        if (records.value == null) {
            Text(text = "Now Loading...")
        }
        else {
            records.value?.map {
                Log.d("VM", it.toString())
                RecordListViewItem(it)
            }
        }
    }
}

@Composable
private fun RecordListViewItem(item: Record) {
    Row {
        ListViewItemText(text = item.memo)
        ListViewItemText(text = item.co2.toString())
        ListViewItemText(text = item.temp.toString())
        ListViewItemText(text = item.humid.toString())
        ListViewItemText(text = item.pressure.toString())
    }
}

@Composable
private fun ListViewItemText(text: String) {
    Text(text = text, Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
}

class RecordViewModelFactory(private val recordRepository: RecordRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) return RecordViewModel(recordRepository) as T

        throw IllegalStateException()
    }
}
