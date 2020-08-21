package com.example.magical_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.magical_answer.`interface`.ApiService
import com.example.magical_answer.`interface`.memoview
import kotlinx.android.synthetic.main.activity_memo_view_page.*
import kotlinx.android.synthetic.main.activity_memoinfo_.*
import kotlinx.android.synthetic.main.activity_memoinfo_.cancel_memo_btn
import kotlinx.android.synthetic.main.activity_memoinfo_.item_content_text
import kotlinx.android.synthetic.main.activity_memoinfo_.item_title_text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Memo_viewPage : AppCompatActivity() {

    var usertoken = ""
    var nickname = ""
    var no = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_view_page)


        cancel_memo_btn.setOnClickListener {
            val intent = Intent(this@Memo_viewPage, Memo_Activity::class.java)
            intent.putExtra("usertoken",usertoken)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }

        // MemoActivity로부터 값 넘겨 받기
        usertoken = intent.getStringExtra("usertoken").toString()
        nickname = intent.getStringExtra("nickname").toString()
        no = intent.getStringExtra("no").toString()

        apiconnect.requestmemoview(usertoken, nickname, no).enqueue(object : Callback<memoview> {
            override fun onFailure(call: Call<memoview>, t: Throwable) {
                Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<memoview>, response: Response<memoview>) {
                val memo = response.body()

                item_title_texts.setText(memo?.title)
                item_content_texts.setText(memo?.con)
            }
        })

        change_memo_btn.setOnClickListener {
            val intent = Intent(this@Memo_viewPage, Memo_Change_Activity::class.java)
            intent.putExtra("usertoken",usertoken)
            intent.putExtra("nickname", nickname)
            intent.putExtra("no", no)
            intent.putExtra("title", item_title_texts.text.toString())
            intent.putExtra("content", item_content_texts.text.toString())
            startActivity(intent)
        }
    }


    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiconnect = retrofit.create(ApiService::class.java)
}