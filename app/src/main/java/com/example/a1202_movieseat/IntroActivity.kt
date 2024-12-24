package com.example.a1202_movieseat

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.a1202_movieseat.databinding.ActivityIntroBinding



class IntroActivity : AppCompatActivity() {
//    앱에서 "IntroActivity"라는 이름의 **화면(페이지)**을 정의함.

    private lateinit var binding: ActivityIntroBinding
//    binding은 XML 파일(activity_intro.xml)과 코드를 연결해주는 도구임.
//    나중에 초기화하겠다는 의미로 lateinit을 사용함.

    override fun onCreate(savedInstanceState: Bundle?) {
//        앱이 이 화면을 처음 실행할 때 하는 일을 정의함.
//        예를 들어, 화면을 표시하거나 버튼을 초기화함.
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
//        activity_intro.xml이라는 설계도를 읽어서 화면을 그릴 준비를 함.
//        설계도를 실제로 조립하는 과정이라고 보면 됨.
        setContentView(binding.root)
//        조립한 화면을 사용자에게 보여줌.
//        화면의 가장 큰 틀(root)을 이 액티비티에 설정하는 역할임.


//        "이 코드는 XML 레이아웃을 뷰 바인딩으로 연결하여 IntroActivity 화면에 표시하는 역할을 함."



        // 올바른 메서드 이름으로 수정: setOnClickListener
        binding.startBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            // 버튼 클릭 시 동작

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        }
    }
}