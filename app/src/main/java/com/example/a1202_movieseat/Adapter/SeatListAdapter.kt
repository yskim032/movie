package com.example.a1202_movieseat.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a1202_movieseat.Models.Seat
import com.example.a1202_movieseat.R
import com.example.a1202_movieseat.databinding.SeatItemBinding
import java.io.File.separator

class SeatListAdapter(
    private val seats: List<Seat>, private val context: Context, private val selectedSeat:SelectedSeat
) : RecyclerView.Adapter<SeatListAdapter.SeatViewHolder>() {
    private val selectedSeatNames = ArrayList<String>()

    class SeatViewHolder(val binding:SeatItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatListAdapter.SeatViewHolder {
        return SeatViewHolder(SeatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SeatListAdapter.SeatViewHolder, position: Int){
        val seat=seats[position]
        holder.binding.seat.text=seat.name

        when (seat.status) {
            Seat.SeatStatus.AVAILABLE -> {
                holder.binding.seat.setBackgroundResource(R.drawable.ic_seat_available)
                holder.binding.seat.setTextColor(context.getColor(R.color.white))
            }

            Seat.SeatStatus.SELECTED -> {
                holder.binding.seat.setBackgroundResource(R.drawable.ic_seat_selected)
                holder.binding.seat.setTextColor(context.getColor(R.color.black))
            }

            Seat.SeatStatus.UNAVAILABLE -> {
                holder.binding.seat.setBackgroundResource(R.drawable.ic_seat_unavailable)
                holder.binding.seat.setTextColor(context.getColor(R.color.grey))
            }
        }

            holder.binding.seat.setOnClickListener{
                when (seat.status) {
                    seat.SeatStatus.AVAILABLE -> {
                        seat.status = Seat.SeatStatus.SELECTED
                        selectedSeatNames.add(seat.name)
                        notifyItemChanged(position)
                    }
                    seat.SeatStatus.SELECTED -> {
                        seat.status = Seat.SeatStatus.AVAILABLE
                        selectedSeatNames.remove(seat.name)
                        notifyItemChanged(position)
                    }
                    else -> {}
                    }
                    val selected=selectedSeatNames.joinToString (separator:",")
                    selectedSeat.Return(selected,selectedSeatNames.size)
                }
            }

        }

    override fun getItemCount(): Int = seatList.size

    interface SelectedSeat{
        fun Return(selectedName: String, num: Int)
    }
}