package com.example.magical_answer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class Question_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_)


        val imageView : ImageView = findViewById(R.id.gif)
        Glide.with(this).load(R.drawable.answergif).into(imageView)
    }
}