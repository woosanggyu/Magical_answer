package com.example.magical_answer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.magical_answer.`interface`.*
import kotlinx.android.synthetic.main.activity_login_.*
import kotlinx.android.synthetic.main.activity_question_.*
import kotlinx.android.synthetic.main.activity_signup__dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login_Activity : AppCompatActivity() {

    var Token = ""
    var ID = ""
    var Nickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_)

        signin_btn.setOnClickListener {
            //사용자가 입력한 아이디 및 비밀번호 받아오기
            var id = id_text.text.toString()
            var pw = pw_text.text.toString()

            if( id != "" && pw != ""){
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
                        ID = logindata!!.ID
                        Nickname = logindata!!.Nickname

                        if( Token != ""){
                            // 토큰이 존재할때 즉 로그인에 성공했을 때
                            Toast.makeText(this@Login_Activity,"로그인에 성공했습니다.",Toast.LENGTH_SHORT).show()
                            val loginintent = Intent(this@Login_Activity, MainActivity::class.java)
                            loginintent.putExtra("token", Token)
                            loginintent.putExtra("ID", ID)
                            loginintent.putExtra("Nickname", Nickname)
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

            else if( id != "" && pw == "" ) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if ( id == "" && pw != "" ) {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
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
        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_signup__dialog, null)

        var alertDialog = AlertDialog.Builder(this)
            .create()

        val register = view.findViewById<Button>(R.id.register_btn)
        val cancel = view.findViewById<Button>(R.id.cancel_btn)
        val checkid = view.findViewById<Button>(R.id.check_id_btn)

        checkid.setOnClickListener {

            var id = alertDialog.userid.text.toString()
            if( id != "" ) {

                apiconnect.requestcheckid(id).enqueue(object : Callback<checkidclass>{
                    override fun onFailure(call: Call<checkidclass>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<checkidclass>, response: Response<checkidclass>) {
                        val checkresult = response.body()
                        Toast.makeText(applicationContext, checkresult?.msg, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        register.setOnClickListener {
            var id = alertDialog.userid.text.toString()
            var pw = alertDialog.userpw_text?.text.toString()
            var nick = alertDialog.usernick_text?.text.toString()
            var age = alertDialog.userage_text?.text.toString()
            var gender = alertDialog.radiogroup.checkedRadioButtonId

            if( id != "" && pw != "" && nick != "" && age != "" && gender != -1) {
                var gender2 = resources.getResourceEntryName(gender)

                apiconnect.requestregist(id, pw, nick, gender2, age).enqueue(object : Callback<Registclass>{
                    override fun onFailure(call: Call<Registclass>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Registclass>, response: Response<Registclass>) {
                        val result = response.body()
                        println(result?.msg)

                        if (result?.msg == "회원가입에 실패했습니다.") {
                            Toast.makeText(applicationContext, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, result?.msg, Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        }
                    }
                })


            } else if ( id == "") {
                Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if ( pw == "") {
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if ( nick == "") {
                Toast.makeText(applicationContext, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if ( age == "") {
                Toast.makeText(applicationContext, "나이를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if ( gender == -1) {
                Toast.makeText(applicationContext, "성별을 체크해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.setView(view)
        alertDialog.show()
        alertDialog.window?.setLayout(700,850)
        }
    }