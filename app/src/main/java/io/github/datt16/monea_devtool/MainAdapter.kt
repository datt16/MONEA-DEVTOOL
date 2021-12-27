package io.github.datt16.monea_devtool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val dataSet: List<Record>) :
    RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tv_item_model)
        val co2Tv: TextView = view.findViewById(R.id.tv_co2)
        val tempTv: TextView = view.findViewById(R.id.tv_temp)
        val humidTv: TextView = view.findViewById(R.id.tv_humid)
        val pressureTv: TextView = view.findViewById(R.id.tv_pressure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = dataSet[position].memo
        holder.co2Tv.text = dataSet[position].co2.toString()
        holder.tempTv.text = dataSet[position].temp.toString()
        holder.humidTv.text = dataSet[position].humid.toString()
        holder.pressureTv.text = dataSet[position].pressure.toString()

    }

    override fun getItemCount(): Int = dataSet.size
}
