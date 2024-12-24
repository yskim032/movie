package com.example.a1209_budongsan

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

// 전역 변수 초기화
var text = ""

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)

//        textView.text = "" // 초기화

        // 키 값
        val key = "JYycGWgo0ICNCBchas0cAfWoUaZBgUvLGDczKp0kRghh%2FpOc69%2FiaPYPi4KWteT8XeHjwJewK%2FhdOzA4sE0haw%3D%3D"
        // 현재 페이지번호
        val pageNo = "&pageNo=1"
        // 한 페이지 결과 수
        val numOfRows = "&numOfRows=5"
        // AND(안드로이드)
        val MobileOS = "&MobileOS=AND"
        // 서비스명 = 어플명
        val MobileApp = "&MobileApp=AppTest"
        // API 정보를 가지고 있는 주소
        val url = "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/basedList?serviceKey=$key$pageNo$numOfRows$MobileOS$MobileApp"

        button.setOnClickListener {
            text = "" // 버튼 클릭 시 `text` 초기화

            // 쓰레드 생성
            val thread = Thread(NetworkThread(url))
            thread.start() // 쓰레드 시작
            thread.join() // 멀티 작업 방지

            // UI 업데이트는 반드시 메인 스레드에서 수행
            runOnUiThread {
                textView.text = text
            }
        }
    }
}

class NetworkThread(private val url: String) : Runnable {

    override fun run() {
        try {
            val xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()

            // 찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("item")

            // list.length만큼 얻고자 하는 태그의 정보를 가져온다
            for (i in 0 until list.length) {
                val n: Node = list.item(i)

                if (n.nodeType == Node.ELEMENT_NODE) {
                    val elem = n as Element

                    println("=========${i + 1}=========")
                    text += "${i + 1}번 캠핑장\n"

                    // 태그 존재 여부 확인 후 추가
                    text += "1. 주소 : ${
                        elem.getSafeElementValue("addr1") ?: "정보 없음"
                    } \n"

                    text += "2. 캠핑장 이름 : ${
                        elem.getSafeElementValue("facltNm") ?: "정보 없음"
                    } \n"

                    text += "3. 위도 : ${
                        elem.getSafeElementValue("mapX") ?: "정보 없음"
                    } \n"

                    text += "4. 경도 : ${
                        elem.getSafeElementValue("mapY") ?: "정보 없음"
                    } \n"

                    text += "5. 업종 : ${
                        elem.getSafeElementValue("induty") ?: "정보 없음"
                    } \n"
                }
            }
        } catch (e: Exception) {
            Log.d("TTT", "오픈API 에러: ${e.message}")
        }
    }

    // 안전하게 태그의 값을 가져오는 확장 함수
    private fun Element.getSafeElementValue(tag: String): String? {
        return try {
            val node = this.getElementsByTagName(tag).item(0)
            node?.textContent
        } catch (e: Exception) {
            null
        }
    }
}
