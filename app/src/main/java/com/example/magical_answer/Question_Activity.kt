package com.example.magical_answer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.magical_answer.`interface`.ApiService
import com.example.magical_answer.`interface`.nobase
import kotlinx.android.synthetic.main.activity_question_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Question_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_)

        //imageView에 gif 연결 및 Glide 모듈을 이용해서 사용하게 함(glide모듈없이는 gif 사용불가)
        val imageView : ImageView = findViewById(R.id.gif)
        Glide.with(this).load(R.drawable.answergif).into(imageView)

        //nobase_btn 클릭시 이벤트
        nobase_btn.setOnClickListener {
            val no = (1..6).random()

            //서비스 실행
            getnobase.requestData(no).enqueue(object : Callback<nobase> {
                override fun onFailure(call: Call<nobase>, t: Throwable) {
                    t.message?.let { it1 -> Log.d("DEBUG", it1) }
                    // 웹 통신에 실패 했을 때 실행되는 코드
                    val dialog = AlertDialog.Builder(this@Question_Activity)
                    dialog.setTitle("실패")
                    dialog.setMessage("통신에 실패했습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<nobase>, response: Response<nobase>) {
                    //웹 통신에 성공 했을 때 실행되는 코드
                    val nonobase = response.body()
                    val dialog = AlertDialog.Builder(this@Question_Activity)
                    dialog.setTitle("뚱이의 답변")
                    dialog.setMessage(nonobase?.msg)
                    dialog.show()
                }

            })
        }
    }

    fun nobase() {

    }

    //레트로핏 객체 선언
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.42:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //서비스 연결
    val getnobase = retrofit.create(ApiService::class.java)
}