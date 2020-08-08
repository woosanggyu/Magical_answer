package com.example.magical_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.magical_answer.`interface`.ApiGetclass
import com.example.magical_answer.`interface`.ApiService
import com.example.magical_answer.`interface`.Loginclass
import kotlinx.android.synthetic.main.activity_login_.*
import kotlinx.android.synthetic.main.activity_question_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login_Activity : AppCompatActivity() {

    var Token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_)

        signin_btn.setOnClickListener {
            //사용자가 입력한 아이디 및 비밀번호 받아오기
            var id = id_text.text.toString()
            var pw = pw_text.text.toString()

            apiconnect.requestLogin(id, pw).enqueue(object : Callback<Loginclass> {
                override fun onFailure(call: Call<Loginclass>, t: Throwable) {
                    t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
                    // 웹 통신에 실패 했을 때 실행되는 코드
                    val dialog = AlertDialog.Builder(this@Login_Activity)
                    dialog.setTitle("서버와 연결 실패")
                    dialog.setMessage("서버와의 연결에 실패했습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<Loginclass>, response: Response<Loginclass>) {
                    //웹 통신에 성공 했을 때 실행되는 코드
                    val logindata = response.body()
                    Token = logindata!!.token

                    if( Token != ""){
                        // 토큰이 존재할때 즉 로그인에 성공했을 때
                        Toast.makeText(this@Login_Activity,"로그인에 성공했습니다.",Toast.LENGTH_SHORT).show()
                        val loginintent = Intent(this@Login_Activity, MainActivity::class.java)
                        loginintent.putExtra("token", Token)
                        startActivity(loginintent)

                    } else if ( Token == "") {
                        // 토큰이 없다면 로그인에 실패함.
                        val dialog = AlertDialog.Builder(this@Login_Activity)
                        dialog.setTitle("로그인에 실패했습니다.")
                        dialog.setMessage("아이디 혹은 비밀번호가 틀립니다.")
                        dialog.show()
                    }
                }
            })
        }

        signup_btn.setOnClickListener {
            signup()
        }
    }
    //레트로핏 객체 선언
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //서비스 연결
    val apiconnect = retrofit.create(ApiService::class.java)

    private fun signup() {

    }
}