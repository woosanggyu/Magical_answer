package com.example.magical_answer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.magical_answer.`interface`.ApiService
import com.example.magical_answer.`interface`.updatememo
import kotlinx.android.synthetic.main.activity_memo__change_.*
import kotlinx.android.synthetic.main.activity_memoinfo_.cancel_memo_btn
import kotlinx.android.synthetic.main.activity_memoinfo_.item_content_text
import kotlinx.android.synthetic.main.activity_memoinfo_.item_title_text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Memo_Change_Activity : AppCompatActivity() {

    var Backwait : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo__change_)

        // MemoActivity로부터 값 넘겨 받기
        var usertoken = intent.getStringExtra("usertoken").toString()
        var nickname = intent.getStringExtra("nickname").toString()
        var no = intent.getStringExtra("no").toString()
        var title = intent.getStringExtra("title").toString()
        var content = intent.getStringExtra("content").toString()

        item_title_text.setText(title)
        item_content_text.setText(content)

        cancel_memo_btn.setOnClickListener {
            val intent = Intent(this@Memo_Change_Activity, Memo_Activity::class.java)
            intent.putExtra("usertoken",usertoken)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }

        change_memo_btn.setOnClickListener {
            val title = item_title_text.text.toString()
            val content = item_content_text.text.toString()

            if ( title != "" && content != "") {
                apiconnect.requestupdatememo(usertoken, nickname, no, title, content).enqueue(object :
                    Callback<updatememo> {
                    override fun onFailure(call: Call<updatememo>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<updatememo>, response: Response<updatememo>) {
                        val result = response.body()

                        Toast.makeText(applicationContext, result?.answer, Toast.LENGTH_SHORT).show()

                        // 정상적으로 메모가 수정되면 유저 토큰, 닉네임을 실어서 인텐트
                        val intent = Intent(this@Memo_Change_Activity, Memo_Activity::class.java)
                        intent.putExtra("usertoken",usertoken)
                        intent.putExtra("nickname", nickname)

                        startActivity(intent)
                    }
                })
            } else if (title == "") {
                Toast.makeText(applicationContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (content == "") {
                Toast.makeText(applicationContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-3-35-40-128.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiconnect = retrofit.create(ApiService::class.java)

    override fun onBackPressed() {
        if(System.currentTimeMillis() - Backwait >= 2000) {
            Backwait = System.currentTimeMillis()
            Toast.makeText(this@Memo_Change_Activity,"뒤로가기 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            //액티비티 종료
            ActivityCompat.finishAffinity(this)
            System.runFinalizersOnExit(true)
            System.exit(0)
        }
    }
}