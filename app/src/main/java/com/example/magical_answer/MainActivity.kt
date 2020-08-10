package com.example.magical_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ask_patrick_btn.setOnClickListener {
            val intent = Intent(this@MainActivity, Question_Activity::class.java)
            startActivity(intent)
        }

        memo_btn.setOnClickListener {
            if(intent.hasExtra("token")){
                var Token = intent.hasExtra("token")
                var ID = intent.hasExtra("ID")
                var Nickname = intent.hasExtra("Nickname")

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
            val intent = Intent(this@MainActivity, Login_Activity::class.java)
            startActivity(intent)
        }
    }
}