package com.example.a1202_movieseat.Activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.a1202_movieseat.Adapter.FilmListAdapter
import com.example.a1202_movieseat.Adapter.SliderAdapter
import com.example.a1202_movieseat.Models.Film
import com.example.a1202_movieseat.Models.SliderItems
import com.example.a1202_movieseat.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance() //파이어베이스 초기화

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        ) // 상태바 제거

        initBanner()  //핵심함수인 initBanner 호출
        initTopMoving()
        initUpcoming()


    }

    private fun initTopMoving() {
        val myRef: DatabaseReference = database.getReference("Items")
        binding.progressBarTopMovies.visibility=View.VISIBLE
        val items=ArrayList<Film>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        items.add(issue.getValue(Film::class.java)!!)
                    }
                    if (items.isNotEmpty()) {
                        binding.recyclerViewTopMovies.layoutManager =
                            LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )

                        binding.recyclerViewTopMovies.adapter = FilmListAdapter(items)
                    }
                    binding.progressBarTopMovies.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initUpcoming() {
        val myRef: DatabaseReference = database.getReference("Items")
        binding.progressBarupcomming.visibility=View.VISIBLE
        val items=ArrayList<Film>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        items.add(issue.getValue(Film::class.java)!!)
                    }
                    if (items.isNotEmpty()) {
                        binding.recyclerViewUpcoming.layoutManager =
                            LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )

                        binding.recyclerViewUpcoming.adapter = FilmListAdapter(items)
                    }
                    binding.progressBarupcomming.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }






    private fun initBanner() {
        val myRef: DatabaseReference = database.getReference("Banners")
        binding.progressBarSlider.visibility = View.VISIBLE

        //firebase가 변화될때마다 호출, 앱에 반영하는 역활
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderItems>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderItems::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                binding.progressBarSlider.visibility = View.GONE
                banners(lists)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun banners(lists: MutableList<SliderItems>) {
        binding.viewPager2.adapter = SliderAdapter(lists, binding.viewPager2)
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer(ViewPager2.PageTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            })
        }

        binding.viewPager2.setPageTransformer(compositePageTransformer)
        binding.viewPager2.currentItem = 1
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
            }
        })


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)

    }
}

