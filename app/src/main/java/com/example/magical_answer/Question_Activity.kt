package com.example.magical_answer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.magical_answer.`interface`.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_question_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Question_Activity : AppCompatActivity() {

    lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    val locationRequestId = 100
    var areafather = ""
    var areaname = ""
    var eat_flag = true
    var show_flag = true
    var enjoy_flag = true
    var usertoken = ""
    var nickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_)

        if(intent.hasExtra("usertoken")) {
            usertoken = intent.getStringExtra("usertoken").toString()
            nickname = intent.getStringExtra("nickname").toString()
        }
//
//        println(usertoken)
//        println(nickname)

        //imageView에 gif 연결 및 Glide 모듈을 이용해서 사용하게 함(glide모듈없이는 gif 사용불가)
        val imageView : ImageView = findViewById(R.id.gif)
        Glide.with(this).load(R.drawable.answergif).into(imageView)

        //nobase_btn 클릭시 이벤트
        nobase_btn.setOnClickListener {
                //서비스 실행
                val no = (1..11).random()

                apiconnect.requestData(no).enqueue(object : Callback<ApiGetclass> {
                    override fun onFailure(call: Call<ApiGetclass>, t: Throwable) {
                        t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
                        // 웹 통신에 실패 했을 때 실행되는 코드
                        val dialog = AlertDialog.Builder(this@Question_Activity)
                        dialog.setTitle("실패")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<ApiGetclass>, response: Response<ApiGetclass>) {
                        //웹 통신에 성공 했을 때 실행되는 코드
                        val nonobase = response.body()
                        val dialog = AlertDialog.Builder(this@Question_Activity)
                        dialog.setTitle("뚱이의 답변")
                        dialog.setMessage(nonobase?.msg)
                        dialog.show()
                    }
                })
        }

        //eat_btn 클릭시 이벤트
        eat_btn.setOnClickListener {
            if(eat_flag) {
                //위치 권한 체크
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    eat_flag = false
                    //서비스 실행
                    updateLocation() // 실시간 위치 업데이트

                        println("주소 : " + areafather + areaname)

                        apiconnect.requesteatData(areafather,areaname).enqueue(object : Callback<Eatgetclass> {
                            override fun onFailure(call: Call<Eatgetclass>, t: Throwable) {
                                t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
                                // 웹 통신에 실패 했을 때 실행되는 코드
                                val dialog = AlertDialog.Builder(this@Question_Activity)
                                dialog.setTitle("실패")
                                dialog.setMessage("잠시후 다시 시도해주세요.")
                                dialog.show()
                                eat_flag = true
                            }

                            override fun onResponse(call: Call<Eatgetclass>, response: Response<Eatgetclass>) {
                                //웹 통신에 성공 했을 때 실행되는 코드
                                eat_flag = true
                                var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                val view = inflater.inflate(R.layout.answer_dialog, null)

                                // dialog 생성
                                var alertDialog = AlertDialog.Builder(this@Question_Activity)
                                    .create()

                                val add = view.findViewById<Button>(R.id.add_question_memo_btn)
                                val cancel = view.findViewById<Button>(R.id.add_nono)
                                var named = view.findViewById<TextView>(R.id.name)
                                var address = view.findViewById<TextView>(R.id.address)
                                val eatdata = response.body()
//
                                named.setText(eatdata?.title)
                                address.setText(eatdata?.address)

                                add.setOnClickListener {
                                    if ( named.text.toString() != "" && named.text.toString() != "" && usertoken != "" && nickname != "") {
                                        apiconnect.requestaddmemo(usertoken, nickname,"먹방 추천 장소","장소 : " + named.text.toString()+ "\n주소 : " + address.text.toString()).enqueue(object : Callback<addmemo> {
                                            override fun onFailure(call: Call<addmemo>, t: Throwable) {
                                                Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                                            }

                                            override fun onResponse(call: Call<addmemo>, response: Response<addmemo>) {
                                                val result = response.body()

                                                Toast.makeText(applicationContext, "메모에 기록되었습니다.", Toast.LENGTH_SHORT).show()

                                                // 메모가 저장되면 다이얼로그를 닫고 인텐트
                                                alertDialog.dismiss()

//                                                val intent = Intent(this@Question_Activity, Memo_Activity::class.java)
//                                                intent.putExtra("usertoken",usertoken)
//                                                intent.putExtra("nickname", nickname)
//
//                                                startActivity(intent)
                                            }
                                        })
                                    } else {
                                        Toast.makeText(this@Question_Activity,"로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                cancel.setOnClickListener {
                                    alertDialog.dismiss()
                                }

                                alertDialog.setView(view)
                                alertDialog.show()
//                                val dialog = AlertDialog.Builder(this@Question_Activity)
//                                dialog.setTitle("뚱이의 답변")
//                                dialog.setMessage(eatdata?.title + "에 가보는건 어때?\n 주소는 " + eatdata?.address + "여기야!")
//                                dialog.show()

                            }
                        })
                } else {

                    //위치 권한 없으면 받아오기
                    getLocation()
                }
            }

        }

        //show_btn 클릭시 이벤트
        show_btn.setOnClickListener {

            if(show_flag) {
                //위치 권한 체크
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    show_flag = false
                    //서비스 실행

                    updateLocation() // 실시간 위치 업데이트

                    println("주소 : " + areafather + areaname)
                    apiconnect.requestdoitData(areafather,areaname).enqueue(object : Callback<Doitclass> {
                        override fun onFailure(call: Call<Doitclass>, t: Throwable) {
                            t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
                            // 웹 통신에 실패 했을 때 실행되는 코드
                            val dialog = AlertDialog.Builder(this@Question_Activity)
                            dialog.setTitle("실패")
                            dialog.setMessage("통신에 실패했습니다.")
                            dialog.show()
                            show_flag = true
                        }

                        override fun onResponse(call: Call<Doitclass>, response: Response<Doitclass>) {
                            show_flag = true
                            var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val view = inflater.inflate(R.layout.answer_dialog, null)

                            // dialog 생성
                            var alertDialog = AlertDialog.Builder(this@Question_Activity)
                                .create()

                            val add = view.findViewById<Button>(R.id.add_question_memo_btn)
                            val cancel = view.findViewById<Button>(R.id.add_nono)
                            var named = view.findViewById<TextView>(R.id.name)
                            var address = view.findViewById<TextView>(R.id.address)
                            val showdata = response.body()
//
                            named.setText(showdata?.title)
                            address.setText(showdata?.address)

                            add.setOnClickListener {
                                if ( named.text.toString() != "" && named.text.toString() != "" && usertoken != "" && nickname != "") {
                                    apiconnect.requestaddmemo(usertoken, nickname,"구경 추천 장소","장소 : " + named.text.toString()+ "\n주소 : " + address.text.toString()).enqueue(object : Callback<addmemo> {
                                        override fun onFailure(call: Call<addmemo>, t: Throwable) {
                                            Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onResponse(call: Call<addmemo>, response: Response<addmemo>) {
                                            val result = response.body()

                                            Toast.makeText(applicationContext, "메모에 기록되었습니다.", Toast.LENGTH_SHORT).show()

                                            // 메모가 저장되면 다이얼로그를 닫고 인텐트
                                            alertDialog.dismiss()

//                                            val intent = Intent(this@Question_Activity, Memo_Activity::class.java)
//                                            intent.putExtra("usertoken",usertoken)
//                                            intent.putExtra("nickname", nickname)
//
//                                            startActivity(intent)
                                        }
                                    })
                                } else {
                                    Toast.makeText(this@Question_Activity,"로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            cancel.setOnClickListener {
                                alertDialog.dismiss()
                            }

                            alertDialog.setView(view)
                            alertDialog.show()
                        }
                    })
                } else {

                    //위치 권한 없으면 받아오기
                    getLocation()
                }
            }
        }

        //enjoy_btn 클릭시 이벤트
        enjoy_btn.setOnClickListener {

            if(enjoy_flag) {
                //위치 권한 체크
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enjoy_flag = false
                    //서비스 실행

                    updateLocation() // 실시간 위치 업데이트

                    println("주소 : " + areafather + areaname)
                    apiconnect.requestenjoyData(areafather,areaname).enqueue(object : Callback<Enjoyclass> {
                        override fun onFailure(call: Call<Enjoyclass>, t: Throwable) {
                            t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
                            // 웹 통신에 실패 했을 때 실행되는 코드
                            val dialog = AlertDialog.Builder(this@Question_Activity)
                            dialog.setTitle("실패")
                            dialog.setMessage("통신에 실패했습니다.")
                            dialog.show()
                            enjoy_flag = true
                        }

                        override fun onResponse(call: Call<Enjoyclass>, response: Response<Enjoyclass>) {
                            //웹 통신에 성공 했을 때 실행되는 코드
                            enjoy_flag = true
                            var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val view = inflater.inflate(R.layout.answer_dialog, null)

                            // dialog 생성
                            var alertDialog = AlertDialog.Builder(this@Question_Activity)
                                .create()

                            val add = view.findViewById<Button>(R.id.add_question_memo_btn)
                            val cancel = view.findViewById<Button>(R.id.add_nono)
                            var named = view.findViewById<TextView>(R.id.name)
                            var address = view.findViewById<TextView>(R.id.address)
                            val enjoy = response.body()
//
                            named.setText(enjoy?.title)
                            address.setText(enjoy?.address)

                            add.setOnClickListener {
                                if ( named.text.toString() != "" && named.text.toString() != "" && usertoken != "" && nickname != "") {
                                    apiconnect.requestaddmemo(usertoken, nickname,"체험 추천 장소","장소 : " + named.text.toString()+ "\n주소 : " + address.text.toString()).enqueue(object : Callback<addmemo> {
                                        override fun onFailure(call: Call<addmemo>, t: Throwable) {
                                            Toast.makeText(applicationContext, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onResponse(call: Call<addmemo>, response: Response<addmemo>) {
                                            val result = response.body()

                                            Toast.makeText(applicationContext, "메모에 기록되었습니다.", Toast.LENGTH_SHORT).show()

                                            // 메모가 저장되면 다이얼로그를 닫고 인텐트
                                            alertDialog.dismiss()

//                                            val intent = Intent(this@Question_Activity, Memo_Activity::class.java)
//                                            intent.putExtra("usertoken",usertoken)
//                                            intent.putExtra("nickname", nickname)
//
//                                            startActivity(intent)
                                        }
                                    })
                                } else {
                                    Toast.makeText(this@Question_Activity,"로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            cancel.setOnClickListener {
                                alertDialog.dismiss()
                            }

                            alertDialog.setView(view)
                            alertDialog.show()
                        }
                    })


                } else {

                    //위치 권한 없으면 받아오기
                    getLocation()
                }
            }
        }
    }

    //레트로핏 객체 선언
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-91-26.ap-northeast-2.compute.amazonaws.com:8080") //서버주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   //서비스 연결
    val apiconnect = retrofit.create(ApiService::class.java)

    fun checkForlocationPermission() : Boolean {
        // 위치 권한 받았는지 확인
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return true

        return false
    }

    fun askLocationPermission() {
        // 권한 받기
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), locationRequestId)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == locationRequestId){
            if(grantResults!=null && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //만약 권한이 있다면 주소 받아오기
                getLocation()
            }
        }
    }


    fun getLocation() {
        //주소받아오기 권한 체크 부분
        if(checkForlocationPermission()){
            updateLocation()
        } else {
            askLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 가장 정확한 위치를 요청
        locationRequest.interval = 10000 // 요청 간격
        locationRequest.fastestInterval = 5000 // 밀리초 단위 중 가장 빠른 위치 요청 ( 정확함 )

        mFusedLocationProviderClient = FusedLocationProviderClient(this) // 위치 값 획득 클래스
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())
    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {

            val location : Location = p0!!.lastLocation

            updateAddressUI(location)
        }
    }

    fun updateAddressUI(location : Location){
        // 받아온 위치 값을 이용해 주소 값으로 변환 하는 작업
        var geocoder : Geocoder
        var addressList: ArrayList<Address>

        geocoder = Geocoder(this, Locale.getDefault())

        addressList = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) as ArrayList<Address>

//        println("주소 :" + addressList)

        if(addressList.size != 0) {
            val userAdd = addressList.get(0).getAddressLine(0)
            val addr = userAdd.split(" ")

            areafather = addr[1]
            areaname = addr[2]

            println("주소 : " + addr[1] + " " +addr[2])
        } else {
            println("주소없졍 :" + addressList)
        }
    }
}