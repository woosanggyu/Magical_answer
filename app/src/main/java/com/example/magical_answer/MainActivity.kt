package com.example.magical_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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
            val intent = Intent(this@MainActivity, Memo_Activity::class.java)
            startActivity(intent)
        }
    }
}