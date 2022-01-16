package io.github.datt16.monea_devtool.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.datt16.monea_devtool.data.RealtimeDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoomViewModel(private val _realtimeDatabaseRepository: RealtimeDatabaseRepository<RoomData>) :
    ViewModel() {

    // recordsStateFlow: 取得できたrecordsが順次に返される
    //   Mutable: 可変
    //   StateFlow: 状態を保持しておけるFlow, 監視可能
    //   Flow: コルーチンの一種, 複数の値を順次に出力ができる(ストリーム)
    //   https://qiita.com/tonionagauzzi/items/12aa1a4400256cece72c
    val recordsStateFlow = MutableStateFlow<List<RoomData>?>(null)

    init {
        fetchData()
    }

    // fetchを行うコルーチンを開始する
    private fun fetchData() {
        viewModelScope.launch {
            _realtimeDatabaseRepository.fetchData().collect {
                recordsStateFlow.value = it.getOrNull()
            }
        }
    }
}
