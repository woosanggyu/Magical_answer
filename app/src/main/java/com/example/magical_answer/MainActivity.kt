package com.example.magical_answer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var token = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.hasExtra("token")) {
            token = true
        } else {
            token = false
        }

        if(token) {
            Login_btn.setText("Logout")
        } else {
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
                val Token = intent.hasExtra("token")
                val ID = intent.hasExtra("ID")
                val Nickname = intent.hasExtra("Nickname")

                val intent = Intent(this@MainActivity, Memo_Activity::class.java)
                intent.putExtra("token",Token)
                intent.putExtra("ID", ID)
                intent.putExtra("Nickname", Nickname)
                startActivity(intent)
            } else {
                Toast.makeText(this, "로그인되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }



        Login_btn.setOnClickListener {
            if(token == true) {
                //토큰이 있으므로 로그인 상태이므로 로그아웃 시키기
                token = false
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                val i = Intent(this@MainActivity, MainActivity::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(i)
                overridePendingTransition(0, 0)
            } else {
                val intent = Intent(this@MainActivity, Login_Activity::class.java)
                startActivity(intent)
            }
        }
    }
}