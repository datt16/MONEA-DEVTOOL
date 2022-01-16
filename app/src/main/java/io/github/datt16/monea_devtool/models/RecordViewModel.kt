package io.github.datt16.monea_devtool.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.datt16.monea_devtool.Record
import io.github.datt16.monea_devtool.data.RealtimeDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecordViewModel(private val _realtimeDatabaseRepository: RealtimeDatabaseRepository<Record>) :
    ViewModel() {

    // recordsStateFlow: 取得できたrecordsが順次に返される
    //   Mutable: 可変
    //   StateFlow: 状態を保持しておけるFlow, 監視可能
    //   Flow: コルーチンの一種, 複数の値を順次に出力ができる(ストリーム)
    //   https://qiita.com/tonionagauzzi/items/12aa1a4400256cece72c
    val recordsStateFlow = MutableStateFlow<List<Record>?>(null)

    init {
        fetchRecords()
    }

    // fetchを行うコルーチンを開始する
    private fun fetchRecords() {
        viewModelScope.launch {
            _realtimeDatabaseRepository.fetchData().collect {
                recordsStateFlow.value = it.getOrNull()
            }
        }
    }
}
