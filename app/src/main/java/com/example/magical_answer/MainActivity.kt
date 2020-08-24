package com.example.magical_answer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var token = false

    var Backwait : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 이전 액티비티로 부터 넘겨받은 토큰이 있는지 확인
        if(intent.hasExtra("token")) {
            token = true
        } else {
            token = false
        }

        if(token) {
            // 토큰이 있다면 로그아웃으로 텍스트 설정
            Login_btn.setText("Logout")
        } else {
            // 토큰이 없다면 로그인으로 텍스트 설정
            Login_btn.setText("Login")
        }

        ask_patrick_btn.setOnClickListener {
            val intent = Intent(this@MainActivity, Question_Activity::class.java)
            startActivity(intent)
        }

        memo_btn.setOnClickListener {
            //만약 토큰이 있다면 ( 로그인 했을때만 발급 됌)
            if(token){
                //토큰, 아이디, 닉네임
                val usertoken = intent.getStringExtra("token")
                val nickname = intent.getStringExtra("nickname")

//                println("유저 토큰이다 : " + usertoken)
//                println("유저 아이디다 : " + ID)
//                println("유저 닉넴이다 : " + Nickname)

                val intent = Intent(this@MainActivity, Memo_Activity::class.java)
                // 로그인한 유저의 토큰, 아이디, 닉네임 값 실어 보내기
                intent.putExtra("usertoken",usertoken)
                intent.putExtra("nickname", nickname)
                startActivity(intent)
            } else {
                Toast.makeText(this, "로그인되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        



        Login_btn.setOnClickListener {
            if(token == true) {
                //토큰이 있다면 로그아웃 시키기
                token = false
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@MainActivity, MainActivity::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                val intent = Intent(this@MainActivity, Login_Activity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - Backwait >= 2000) {
            Backwait = System.currentTimeMillis()
            Toast.makeText(this@MainActivity,"뒤로가기 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            //액티비티 종료
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}