package com.example.magical_answer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magical_answer.`interface`.*
import kotlinx.android.synthetic.main.activity_memo_.*
import kotlinx.android.synthetic.main.activity_signup__dialog.*
import kotlinx.android.synthetic.main.memo_create.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Memo_Activity : AppCompatActivity() {

    var usertoken = ""
    var nickname = ""
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_)

        // MainActivity로부터 아이디, 토큰, 닉네임 값 넘겨 받기
        id = intent.getStringExtra("id").toString()
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
                val adapter = MemoAdapter(my_memo)
                // RecyclerView에 매칭시키기
                recyclerView.adapter = adapter
            }
        })

        add_item.setOnClickListener {
            create_item()
        }
    }

    fun create_item() {
        // 서비스 연결 및 dialog 내부 연결
        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.memo_create, null)

        // Dialog 객체 생성
        var alertDialog = AlertDialog.Builder(this)
            .create()

        // 버튼 정의
        val create = view.findViewById<Button>(R.id.create_btn)
        val cancel = view.findViewById<Button>(R.id.create_cancel_btn)


        create.setOnClickListener {
            // 사용자가 입력한 제목, 내용 가져오기
            var title = alertDialog.item_titles?.text.toString()
            var content = alertDialog.item_content?.text.toString()

            if ( title != "" && content != "") {
                apiconnect.requestaddmemo(usertoken, nickname,title,content).enqueue(object : Callback<addmemo>{
                    override fun onFailure(call: Call<addmemo>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<addmemo>, response: Response<addmemo>) {
                        val result = response.body()

                        Toast.makeText(applicationContext, result?.answer, Toast.LENGTH_SHORT).show()

                        // 정상적으로 메모작성이 완료되면 유저 토큰, 아이디, 닉네임을 실어서 인텐트
                        val intent = Intent(this@Memo_Activity, Memo_Activity::class.java)
                        intent.putExtra("usertoken",usertoken)
                        intent.putExtra("id", id)
                        intent.putExtra("nickname", nickname)

                        //다이얼로그 닫기
                        alertDialog.dismiss()

                        startActivity(intent)
                    }
                })
            } else if (title == "") {
                Toast.makeText(applicationContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (content == "") {
                Toast.makeText(applicationContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }



        cancel.setOnClickListener {
            // 다이얼 로그 닫기
            alertDialog.dismiss()
        }


        alertDialog.setView(view)
        alertDialog.show()

    }


    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiconnect = retrofit.create(ApiService::class.java)
}
