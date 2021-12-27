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
import com.google.firebase.database.core.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.github.datt16.monea_devtool.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    var recyclerview: RecyclerView? = null
    var fab: FloatingActionButton? = null
    var records: Records = Records()
    var store = DataSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        context?.let { setup(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = binding.mainFab
        fab?.setOnClickListener {
            findNavController().navigate(R.id.action_add_record)
        }

        recyclerview = binding.containerRecyclerView
        this.recyclerview?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(context, records.getAllRecords())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.fab = null
        this.binding.containerRecyclerView.adapter = null
    }

    private fun setup(context: android.content.Context) {
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

                        binding.containerRecyclerView.adapter =
                            MainAdapter(context, records.getAllRecords())
                    }
                    store.debug()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
