package io.github.datt16.monea_devtool.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import io.github.datt16.monea_devtool.Record
import io.github.datt16.monea_devtool.Records
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

// DBのインスタンスが必要なため、インスタンス化の時に要求
class RecordRepositoryImpl constructor(
    private val database: FirebaseDatabase
) : RealtimeDatabaseRepository {

    companion object {
        // RECORD_REFERENCE: RDB上のデータのパス
        // >>> v1/records/sensorId/[センサ名]/records/
        const val RECORD_REFERENCE = "v1/records/sensorId/HANDSON/records/"
    }

    @ExperimentalCoroutinesApi
    override fun fetchRecords() = callbackFlow<Result<List<Record>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val value: List<Pair<String, Record>>? =
                    snapshot.getValue<Map<String, Record>>()?.toList()

                // 入れ物の定義
                val records = Records()
                if (value != null) {
                    records.setRecords(value.map { it.second.addMemo(it.first) })
                    records.setSensorId("HANDSON")
                }

                // 取得したデータを返す
                this@callbackFlow.sendBlocking(Result.success(records.getAllRecords()))
            }
        }

        // リスナーのアタッチ | 購読開始
        database.getReference(RECORD_REFERENCE).limitToLast(20).addValueEventListener(postListener)

        // フローがキャンセルされた場合
        awaitClose {
            database.getReference(RECORD_REFERENCE).removeEventListener(postListener)
        }
    }
}
