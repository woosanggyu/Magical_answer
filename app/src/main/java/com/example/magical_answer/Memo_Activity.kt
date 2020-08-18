package com.example.magical_answer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magical_answer.`interface`.*
import kotlinx.android.synthetic.main.activity_memo_.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Memo_Activity : AppCompatActivity() {

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

            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<Mymemolist>, response: Response<Mymemolist>) {
                //웹 통신에 성공 했을 때 실행되는 코드
                val datalist = response.body()
                val memo = datalist?.answer
                val recyclerView = findViewById<RecyclerView>(R.id.memolist_view)

                recyclerView.layoutManager = LinearLayoutManager(this@Memo_Activity, LinearLayout.VERTICAL, false)

                val my_memo = ArrayList<mymemo>()

                for(i in 0..memo!!.size()-1) {
                    var datess = JSONObject(memo?.get(i).toString())

                    var no = datess.getString("no")
                    var wr = datess.getString("writer")
                    var ti = datess.getString("title")
                    var con = datess.getString("content")
                    var dt = datess.getString("CreateTime")

                    my_memo.add(mymemo(no,wr,ti,con,dt))

                    val adapter = MemoAdapter(my_memo)

                    recyclerView.adapter = adapter

                }
            }
        })
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiconnect = retrofit.create(ApiService::class.java)
}
