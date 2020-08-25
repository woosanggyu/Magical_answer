package com.example.magical_answer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
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

    var Backwait : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_)

        // MainActivity로부터 아이디, 토큰, 닉네임 값 넘겨 받기
        usertoken = intent.getStringExtra("usertoken").toString()
        nickname = intent.getStringExtra("nickname").toString()

        val my_memo = ArrayList<mymemo>()

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

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                //글 번호 구하기
                val lenstring = viewHolder.adapterPosition.toString()
                val lenint = Integer.parseInt(lenstring)
                val deleteno = my_memo[lenint].no
//                println("Int : " + my_memo[lenint].no)




                // 삭제하기
                apiconnect.requestdeletememo(usertoken, nickname, deleteno).enqueue( object : Callback<memodelete>{
                    override fun onFailure(call: Call<memodelete>, t: Throwable) {
                        // 웹 통신에 실패 했을 때 실행되는 코드
                        val dialog = AlertDialog.Builder(this@Memo_Activity)
                        dialog.setTitle("실패")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(
                        call: Call<memodelete>,
                        response: Response<memodelete>
                    ) {
                        val result = response.body()
                        Toast.makeText(this@Memo_Activity,"삭제되었습니다.",Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Memo_Activity, Memo_Activity::class.java)
                        intent.putExtra("usertoken",usertoken)
                        intent.putExtra("nickname", nickname)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }

                })
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(memolist_view)
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

    override fun onBackPressed() {

        val intent = Intent(this@Memo_Activity, MainActivity::class.java)
        intent.putExtra("token",usertoken)
        intent.putExtra("nickname", nickname)
        startActivity(intent)

//        if(System.currentTimeMillis() - Backwait >= 2000) {
//            Backwait = System.currentTimeMillis()
//            Toast.makeText(this@Memo_Activity,"뒤로가기 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
//        } else {
//            //액티비티 종료
//            ActivityCompat.finishAffinity(this)
//            System.runFinalizersOnExit(true)
//            System.exit(0)
//        }
    }
}

