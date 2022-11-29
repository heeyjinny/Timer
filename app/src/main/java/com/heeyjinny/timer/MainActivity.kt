package com.heeyjinny.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.heeyjinny.timer.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //1
    //전체시간 저장변수 total, 시작되었음을 체크하는 변수 started 선언
    var total = 0
    var started = false

    //2
    //화면에 시간 값을 출력하는 Handler구현하고 변수(handler)에 저장
    val handler = object : Handler(Looper.getMainLooper()){
        //2-1
        //핸들러의 handleMessage() 메서드 오버라이드
        override fun handleMessage(msg: Message) {
            //2-2
            //핸들러로 메시지가 전달되면
            //전체시간(초) total에 입력되어있는 시간을 60으로 나눈 값(/60)은 분단위로
            //60으로 나눈 나머지 값(%60)을 초단위로 사용하고
            //00 : 00 형식으로 보이도록
            //이 double형식의 값을 앞 2글자만 나오게 하여 변수에 저장("%02d")
            val minute = String.format("%02d",total/60)
            val second = String.format("%02d", total%60)

            //2-3
            //분,초의 값을 가지고 있는 값을 textTimer에 입력
            binding.textTimer.text="$minute : $second"
        }
    }//var handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //3
        //시작버튼을 누르면 실행하는 코드 작성
        binding.buttonStart.setOnClickListener {

            //3-1
            //시작되었음을 체크하는 변수 started의 값 true로 변경
            started = true

            //3-2
            //새로운 스레드 실행(코틀린에서 제공하는 thread() 구현)
            thread(start = true){
                //3-3
                //started가 true일 때 반복하는 while문 작성
                while (started){
                    //3-4
                    //스레드가 1초에 한 번씩 동작 하면서 아래 if문 반복
                    Thread.sleep(1000)
                    //3-5
                    //if문을 사용하여 started가 true일 때
                    if (started){
                        //total의 값을 1씩 증가하여 핸들러에 메시지 전송
                        //handler를 호출하는 곳이 한 곳이므로 메시지에 0을 담아 호출
                        total += 1
                        handler.sendEmptyMessage(0)
                    }
                }

            }

        }//buttonStart

        //4
        //종료버튼을 누르면 종료되어 타이머 멈추는 코드 작성
        binding.buttonStop.setOnClickListener {

            //4-1
            //if문을 사용하여 만약 started가 true라면 실행하는 코드 작성
            if (started){
                started = false
            }

        }//buttonStop

        //5
        //초기와 버튼을 누르면 타이머가 리셋되는 코드 작성
        binding.buttonReset.setOnClickListener {

            //5-1
            //started가 false라면 초기화 실행
            if (!started){
                total = 0
                binding.textTimer.text = "00 : 00"
            }

        }//buttonReset

    }//onCreate

}//MainActivity