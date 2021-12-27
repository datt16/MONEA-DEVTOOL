package io.github.datt16.monea_devtool

class Record(
    val co2: Int = -255,
    val created: Int = 0,
    val humid: Int = 0,
    val pressure: Int = 0,
    val temp: Int = -255,
    var memo: String = "",
) {
    fun addMemo(memo: String): Record {
        this.memo = memo
        return this
    }
}

class DataSet {
    var dataSets: Map<String, List<Record>> = mutableMapOf()

    fun resetDataSets() {
        dataSets = mutableMapOf()
    }

    fun setNewSensor(sensorId: String, Records: List<Record>) {
        dataSets += sensorId to Records
    }

    fun debug() {
//        Log.d("DataSet",
//            dataSets.map { entry -> "${entry.key} ----- ${entry.value.map { it.memo }}\n" }
//                .toString()
//        )
    }
}

class Records {

    private var records = listOf<Record>()
    private var sensorId: String = ""

    init {
        resetData()
    }

    fun resetData() {
        records = listOf<Record>()
        sensorId = ""
    }

    fun setSensorId(id: String) {
        sensorId = id
    }

    fun setRecords(item: List<Record>) {
        records = item.sortedBy { it.created }
    }

    fun addRecord(item: Record) {
        records.plus(item)
    }

    fun getSensorId(): String {
        return sensorId
    }

    fun getCurrentRecord(): Record {
        return records.last()
    }

    fun getAllRecords(): List<Record> {
        return records
    }
}
