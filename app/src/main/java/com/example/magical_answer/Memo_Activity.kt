package com.example.magical_answer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

class Memo_Activity : AppCompatActivity(), ItemClickListener {

    var usertoken = ""
    var nickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_)

        // MainActivity로부터 아이디, 토큰, 닉네임 값 넘겨 받기
        usertoken = intent.getStringExtra("usertoken").toString()
        nickname = intent.getStringExtra("nickname").toString()

        // 메모 리스트 불러오기
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
                }

                // 어댑터랑 my_memo 리스트랑 연결
                val adapter = MemoAdapter(my_memo, this@Memo_Activity)
                // RecyclerView에 매칭시키기
                recyclerView.adapter = adapter

//                adapter.setItemClickListener( object: MemoAdapter.ItemClickListener{
//                    override fun onClick(view: mymemo, position: Int) {
//                        println("제목 :" + view.item_title)
//                        Toast.makeText(this@Memo_Activity, "${position}번 클릭", Toast.LENGTH_SHORT).show()
//                    }
//                })
            }
        })

        add_item.setOnClickListener {
            val intent = Intent(this@Memo_Activity, Memoinfo_Activity::class.java)
            intent.putExtra("usertoken",usertoken)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }

//        memo_delete_btn.setOnClickListener {
//            println()
//        }
    }

    override fun onClick(view: mymemo, position: Int) {
        println("글 번호 :" + view.no)
        val intent = Intent(this@Memo_Activity, Memo_viewPage::class.java)
        intent.putExtra("usertoken",usertoken)
        intent.putExtra("nickname", nickname)
        intent.putExtra("no", view.no)
        startActivity(intent)
    }


    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiconnect = retrofit.create(ApiService::class.java)
}

