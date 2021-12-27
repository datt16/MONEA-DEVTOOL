package io.github.datt16.monea_devtool

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.datt16.monea_devtool.databinding.TextRowItemBinding

class MainAdapter(context: Context, private val dataSet: List<Record>) :
    RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    class MyViewHolder(binding: TextRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.tvItemModel
        val co2Tv: TextView = binding.tvCo2
        val tempTv: TextView = binding.tvTemp
        val humidTv: TextView = binding.tvHumid
        val pressureTv: TextView = binding.tvPressure
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: TextRowItemBinding =
            TextRowItemBinding.inflate(layoutInflater, parent, false)

        return MyViewHolder(binding)
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
