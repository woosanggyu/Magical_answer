package com.example.magical_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.magical_answer.`interface`.ApiGetclass
import com.example.magical_answer.`interface`.ApiService
import com.example.magical_answer.`interface`.MemoListAdaper
import com.example.magical_answer.`interface`.Mymemolist
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_memo_.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.file.Paths.get
import kotlin.concurrent.timer

class Memo_Activity : AppCompatActivity() {


    class memo (
        var no : String,
        var writer : String,
        var title : String,
        var dates : String
    )

    var memolist = arrayListOf<memo>(
    )
    var usertoken = ""
    var nickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_)

        usertoken = intent.getStringExtra("usertoken").toString()
        nickname = intent.getStringExtra("nickname").toString()

        apiconnect.requestmymemo(usertoken, nickname).enqueue(object : Callback<Mymemolist> {
            override fun onFailure(call: Call<Mymemolist>, t: Throwable) {
                // 웹 통신에 실패 했을 때 실행되는 코드
                val dialog = AlertDialog.Builder(this@Memo_Activity)
                dialog.setTitle("실패")
                dialog.setMessage("통신에 실패했습니다.")
                dialog.show()
            }

            override fun onResponse(call: Call<Mymemolist>, response: Response<Mymemolist>) {
                //웹 통신에 성공 했을 때 실행되는 코드
                val datalist = response.body()
                val memo = datalist?.answer

                for(i in 0..memo!!.size()) {
//                    var datess = JSONObject(memo?.get(3).toString())

//                    println(datess)

//                  memolist.add(memo(nos, writers, titles, datess));
                }
            }
        })

        val memoAdapter = MemoListAdaper(this, memolist)














//        var token = false
//
//        if(intent.hasExtra("usertoken")) {
//            token = true
//        } else {
//            token = false
//        }
//
//        take_data_btn.setOnClickListener {
//            if(token) {
//                var usertoken = ""
//                var nickname = ""
//
//                usertoken = intent.getStringExtra("usertoken").toString()
//                nickname = intent.getStringExtra("nickname").toString()
//
//                apiconnect.requestmymemo(usertoken, nickname).enqueue(object : Callback<Mymemolist> {
//                    override fun onFailure(call: Call<Mymemolist>, t: Throwable) {
//                        t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
//                        // 웹 통신에 실패 했을 때 실행되는 코드
//                        val dialog = AlertDialog.Builder(this@Memo_Activity)
//                        dialog.setTitle("실패")
//                        dialog.setMessage("통신에 실패했습니다.")
//                        dialog.show()
//                    }
//
//                    override fun onResponse(call: Call<Mymemolist>, response: Response<Mymemolist>) {
//                        //웹 통신에 성공 했을 때 실행되는 코드
//                        val datalist = response.body()
//
//                        println(datalist!!.answer[0])
//                        println(datalist!!.answer[1])
//                        println(datalist!!.answer[2])
//
//                    }
//                })
//            } else {
//                Toast.makeText(this, "로그인이 만료되었습니다.",Toast.LENGTH_SHORT).show()
//                val loginintent = Intent(this, Login_Activity::class.java)
//                startActivity(loginintent)
//            }
//        }


    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiconnect = retrofit.create(ApiService::class.java)
}
