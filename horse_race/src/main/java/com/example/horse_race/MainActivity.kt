package com.example.horse_race

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var startRaceButton: Button
    private lateinit var horseSelector: Spinner
    private lateinit var resultMessageView: TextView
    private lateinit var raceHandler: Handler
    private var isRacing = false
    private val horsePositions = IntArray(6) { 0 }
    private val winCounts = IntArray(6) { 0 } // 각 말의 우승 횟수를 저장
    private val finishLine = 1000 // 골인 조건 (트랙 길이)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startRaceButton = findViewById(R.id.startRaceButton)
        horseSelector = findViewById(R.id.horseSelector)
        resultMessageView = findViewById(R.id.resultMessage)

        // 말 선택 Spinner 설정
        val horses = listOf("1번 말", "2번 말", "3번 말", "4번 말", "5번 말", "6번 말")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        horseSelector.adapter = adapter

        startRaceButton.setOnClickListener {
            if (!isRacing) {
                startRace()
            }
        }
    }

    private fun startRace() {
        isRacing = true
        horsePositions.fill(0) // 말 위치 초기화
        updateRaceTrack() // 초기화 시 UI도 업데이트
        resultMessageView.text = "경주가 시작되었습니다!" // 결과 메시지 초기화
        raceHandler = Handler(Looper.getMainLooper())

        raceHandler.post(object : Runnable {
            override fun run() {
                if (!isRacing) return

                // 말 이동 범위 설정
                for (i in horsePositions.indices) {
                    horsePositions[i] += Random.nextInt(10, 30) // 이동 거리 범위 조정
                }
                updateRaceTrack()

                // 우승 조건 확인
                val winnerIndex = horsePositions.indexOfFirst { it >= finishLine }
                if (winnerIndex >= 0) {
                    isRacing = false

                    // 우승 횟수 업데이트
                    winCounts[winnerIndex]++

                    // 등수 계산
                    val rankings = horsePositions
                        .mapIndexed { index, position -> index + 1 to position }
                        .sortedByDescending { it.second } // 위치를 기준으로 내림차순 정렬
                        .mapIndexed { rank, pair -> "${rank + 1}등: ${pair.first}번 말" }
                        .joinToString("\n") // 각 등수를 줄바꿈으로 연결

                    val selectedHorse = horseSelector.selectedItemPosition
                    val message = if (winnerIndex == selectedHorse) {
                        "축하합니다! ${winnerIndex + 1}번 말이 우승했습니다.\n\n등수:\n$rankings"
                    } else {
                        "아쉽습니다. ${winnerIndex + 1}번 말이 우승했습니다.\n\n등수:\n$rankings"
                    }

                    // 결과 메시지 업데이트
                    resultMessageView.text = message

                    // 우승 횟수 화면에 업데이트
                    updateWinCounts()
                } else {
                    raceHandler.postDelayed(this, 200) // 지연 시간 200ms로 설정
                }
            }
        })
    }

    private fun updateRaceTrack() {
        for (i in horsePositions.indices) {
            val horseId = resources.getIdentifier("horse${i + 1}", "id", packageName)
            val horseView = findViewById<TextView>(horseId)

            // 디버깅: 각 말 위치 로그 출력
            Log.d("HorseRace", "Horse ${i + 1} position: ${horsePositions[i]}")

            horseView.translationX = horsePositions[i].toFloat()
        }
    }

    private fun updateWinCounts() {
        val winTextView = findViewById<TextView>(R.id.winCountsView)
        val winText = winCounts.mapIndexed { index, count ->
            "${index + 1}번 말: $count" + "승"// 우승 횟수를 텍스트로 변환
        }.joinToString("\n") // 줄바꿈으로 연결
        winTextView.text = winText

        // 디버깅: 업데이트된 우승 횟수 확인
        Log.d("HorseRace", "Updated Win Counts: ${winCounts.joinToString(", ")}")
    }
}





