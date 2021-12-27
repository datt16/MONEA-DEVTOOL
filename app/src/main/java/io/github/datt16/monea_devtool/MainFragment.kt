package io.github.datt16.monea_devtool

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment() {
    var recyclerview: RecyclerView? = null
    var fab: FloatingActionButton? = null
    var records: Records = Records()
    var store = DataSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setup()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = view.findViewById(R.id.mainFab)
        fab?.setOnClickListener {
            findNavController().navigate(R.id.action_add_record)
        }

        recyclerview = view.findViewById(R.id.container_recycler_view)
        this.recyclerview?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(records.getAllRecords())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.fab = null
        this.recyclerview?.adapter = null
        this.recyclerview = null
    }

    private fun setup() {
        records.resetData()
        val database = Firebase.database
        val myRef = database.getReference("v1/records/sensorId/HANDSON/records/")
        myRef.limitToLast(20)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value: List<Pair<String, Record>>? =
                        snapshot.getValue<Map<String, Record>>()?.toList()
                    value?.map { it -> Log.d("MSG", it.first) }
                    if (value != null) {
                        records.setRecords(value.map { it.second.addMemo(it.first) })
                        records.setSensorId("HANDSON")

                        store.setNewSensor(records.getSensorId(), records.getAllRecords())
                        Log.d("Fire", records.getAllRecords().toString())

                        recyclerview?.adapter = MainAdapter(records.getAllRecords())
                    }
                    store.debug()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
