package com.example.a1202_movieseat.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.Contextcompat
import com.example.a1202_movieseat.R
import com.example.a1202_movieseat.databinding.ItemTimeBinding

class TimeAdapter(private val timeSlots:List<String>) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    private var selectedPosition=-1
    private var lastSelectedPosition=-1

    inner class TimeViewHolder(private val binding:ItemTimeBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(time:String){
            binding.TextViewTime.text=time
            if(selectedPosition==position){

                binding.TextViewTime.setBackgroundResource(R.drawable.white_bg)
                binding.TextViewTime.setTextColor(Contextcompat.getColor(itemView.context,R.color.black))

            }else{
                binding.TextViewTime.setBackgroundResource(R.drawable.white_bg)
                binding.TextViewTime.setTextColor(Contextcompat.getColor(itemView.context,R.color.white))
            }

            binding.root.setOnClickListener{
                val position=position
                if(position!=RecyclerView.NO_POSITION){
                    lastSelectedPosition=selectedPosition
                    selectedPosition=position
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(ItemTimeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(timeSlots[position])
    }

    override fun getItemCount(): Int=timeSlots.size


}