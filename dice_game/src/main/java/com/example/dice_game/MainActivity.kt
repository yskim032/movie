package com.example.dice_game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var diceImage: ImageView
    private lateinit var resultText: TextView
    private lateinit var speedControl: SeekBar
    private lateinit var speedText: TextView
    private lateinit var startStopButton: Button
    private lateinit var timeText: TextView

    private val diceImages = arrayOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )
    private val diceCounts = IntArray(6)
    private var isRolling = false
    private val handler = Handler(Looper.getMainLooper())
    private var rollSpeed = 500L // 500ms 기본 속도
    private var totalRolls = 0
    private var startTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceImage = findViewById(R.id.diceImage)
        resultText = findViewById(R.id.resultText)
        speedControl = findViewById(R.id.speedControl)
        speedText = findViewById(R.id.speedText)
        startStopButton = findViewById(R.id.startStopButton)
        timeText = findViewById(R.id.timeText)

        // 속도 슬라이더 리스너
        speedControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val rollsPerSecond = progress + 2 // 최소 1회/초
                rollSpeed = 1000L / rollsPerSecond
                speedText.text = "Speed: $rollsPerSecond rolls/sec"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 시작/정지 버튼 리스너
        startStopButton.setOnClickListener {
            if (isRolling) {
                stopRolling()
            } else {
                startRolling()
            }
        }
    }

    private fun startRolling() {
        isRolling = true
        startStopButton.text = "Stop"
        totalRolls = 0
        startTime = System.currentTimeMillis()
        diceCounts.fill(0) // 카운트 초기화
        rollDice()
    }

    private fun stopRolling() {
        isRolling = false
        startStopButton.text = "Start"
        handler.removeCallbacksAndMessages(null)
    }

    private fun rollDice() {
        if (!isRolling) return

        val randomNumber = Random.nextInt(6)
        diceCounts[randomNumber]++
        totalRolls++
        updateDiceImage(randomNumber)
        updateResultText()

        // 일정 속도로 반복 실행
        handler.postDelayed({ rollDice() }, rollSpeed)
    }

    private fun updateDiceImage(number: Int) {
        diceImage.setImageResource(diceImages[number])
    }

    private fun updateResultText() {
        val elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0 // 초 단위
        val percentages = diceCounts.map { count ->
            if (totalRolls > 0) (count.toDouble() / totalRolls * 100).let { "%.1f".format(it) } else "0.0"
        }

        // 각 숫자와 비율을 일정한 간격으로 정렬
        val result = (1..6).joinToString("\n") {
            String.format("%-2d: %-5d (%5s%%)", it, diceCounts[it - 1], percentages[it - 1])
        }

        resultText.text = result
        timeText.text = "Time: %.1f sec | Total Rolls: $totalRolls".format(elapsedTime)
    }

}

