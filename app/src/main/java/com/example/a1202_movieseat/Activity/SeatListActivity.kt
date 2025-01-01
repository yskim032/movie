package com.example.a1202_movieseat.Activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1202_movieseat.Adapter.DateAdapter
import com.example.a1202_movieseat.Adapter.SeatListAdapter
import com.example.a1202_movieseat.Adapter.TimeAdapter
import com.example.a1202_movieseat.Models.Film
import com.example.a1202_movieseat.Models.Seat
import com.example.a1202_movieseat.databinding.ActivitySeatListBinding
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class SeatListActivity(val name: String) : AppCompatActivity() {
    private lateinit var binding: ActivitySeatListBinding
    private lateinit var film: Film
    private var price: Double = 0.0
    private var number:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentExtra()
        setVariable()
        initSeatsList()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        }

    private fun initSeatsList() {
        val gridLayoutManager= GridLayoutManager(this,7)
        gridLayoutManager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (position % 7 == 3) 1 else 1
            }
        }

        binding.seatRecyclerview.layoutManager=gridLayoutManager
        val seatList=mutableListOf<Seat>()
        val numberSeats=81

        for(i in 0 until numberSeats){
            val SeatName=""
            val SeatStatus=if(i==2|| i==20|| i==33|| i==41|| i==50|| i==72|| i==83) Seat.SeatStatus.UNAVAILABLE else Seat.SeatStatus.AVAILABLE
            seatList.add(Seat(SeatStatus, SeatName))

        }

        val SeatAdapter=SeatListAdapter(seatList,this,object :SeatListAdapter.SelectedSeat{
            override fun Return(selectedName: String, num: Int) {
                binding.numberSelectedTxt.text="$num Seat Selected"
                val df= DecimalFormat("#.##")
                price = df.format(num * film.price).toDouble()
                number=num
                binding.priceTxt.text="$$price"
            }
        })
        binding.seatRecyclerview.adapter=SeatAdapter
        binding.seatRecyclerview.isNestedScrollingEnabled=false

        binding.timeRecycleView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.timeRecycleView.adapter= TimeAdapter(generateTimeSlots())

        binding.dateRecycleView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.dateRecycleView.adapter= DateAdapter(generateDate())

    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener{
            finish()
        }
    }

    private fun getIntentExtra() {
        film=intent.getParcelableExtra("film")!!

    }

    private fun generateTimeSlots():List<String>{
        val timeSlots= mutableListOf<String>()

        val formatter=DateTimeFormatter.ofPattern("hh:mm a")


        for(i in 0 until 24 step 2){
            val time= LocalTime.of(i,0)
            timeSlots.add(time.format(formatter))
        }

        return timeSlots

    }

    private fun generateDate():List<String>{
        val dates= mutableListOf<String>()
        val today= LocalDate.now()
        val formatter=DateTimeFormatter.ofPattern("EEE/dd/MMM")

        for(i in 0 until 7){
            dates.add(today.plusDays(i.toLong()).format(formatter))
        }
        return dates
    }
}