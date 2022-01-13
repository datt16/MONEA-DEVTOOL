package io.github.datt16.monea_devtool.data

import io.github.datt16.monea_devtool.Record
import kotlinx.coroutines.flow.Flow

interface RealtimeDatabaseRepository {

    // fetchRecords: RDB上のRecordsを購読する
    // この関数が実行されるとRDBにリスナーがアタッチされる
    fun fetchRecords(): Flow<Result<List<Record>>>

}