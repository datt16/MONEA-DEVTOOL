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
import io.github.datt16.monea_devtool.models.RoomData
import io.github.datt16.monea_devtool.models.SensorData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

// DBのインスタンスが必要なため、インスタンス化の時に要求
class SensorDataRepositoryImpl constructor(
    private val database: FirebaseDatabase
) : RealtimeDatabaseRepository<SensorData> {

    companion object {
        // RECORD_REFERENCE: RDB上のデータのパス
        // >>> v1/records/sensorId/[センサ名]/records/
        const val REFERENCE = "v1/sensors"
    }

    @ExperimentalCoroutinesApi
    override fun fetchData() = callbackFlow<Result<List<SensorData>>> {


        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("SensorData/Fetch", "data changed.")
                val value =
                    snapshot.getValue<Map<String, SensorData>>()
                Log.d("SensorData/Fetch", value.toString())

                val sensors: MutableList<SensorData> = mutableListOf()
                value?.map {
                    sensors += it.value
                }

                // 取得したデータを返す
                this@callbackFlow.sendBlocking(Result.success(sensors))
            }
        }

        // リスナーのアタッチ | 購読開始
        database.getReference(REFERENCE).limitToLast(20).addValueEventListener(postListener)

        // フローがキャンセルされた場合
        awaitClose {
            database.getReference(REFERENCE).removeEventListener(postListener)
        }
    }
}
