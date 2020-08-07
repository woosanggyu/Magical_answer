package com.example.magical_answer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
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
import kotlin.collections.ArrayList

class Question_Activity : AppCompatActivity() {

    lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    val locationRequestId = 100
    var areafather = ""
    var areaname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_)

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
            //위치 권한 체크
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //서비스 실행
                updateLocation() // 실시간 위치 업데이트

                println("주소 : " + areafather + areaname)

                apiconnect.requesteatData(areafather,areaname).enqueue(object : Callback<Eatgetclass> {
                    override fun onFailure(call: Call<Eatgetclass>, t: Throwable) {
                        t.message?.let { it1 -> Log.d("DEBUG", it1) } // 에러메시지 출력(오류나서 무슨 오류인지 확인하려고했었음)
                        // 웹 통신에 실패 했을 때 실행되는 코드
                        val dialog = AlertDialog.Builder(this@Question_Activity)
                        dialog.setTitle("실패")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<Eatgetclass>, response: Response<Eatgetclass>) {
                        //웹 통신에 성공 했을 때 실행되는 코드
                        val eatdata = response.body()
                        val dialog = AlertDialog.Builder(this@Question_Activity)
                        dialog.setTitle("뚱이의 답변")
                        dialog.setMessage(eatdata?.title + "에 가보는건 어때?\n 주소는 " + eatdata?.address + "여기야!")
                        dialog.show()
                    }
                })
            } else {

                //위치 권한 없으면 받아오기
                getLocation()
            }
        }

        //show_btn 클릭시 이벤트
        show_btn.setOnClickListener {
            //위치 권한 체크
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
                    }

                    override fun onResponse(call: Call<Doitclass>, response: Response<Doitclass>) {
                        //웹 통신에 성공 했을 때 실행되는 코드
                        val doitdata = response.body()
                        val dialog = AlertDialog.Builder(this@Question_Activity)
                        dialog.setTitle("뚱이의 답변")
                        dialog.setMessage(doitdata?.title + "에 가보는건 어때?\n 주소는 " + doitdata?.address + " 여기야!" )
                        dialog.show()
                    }
                })
            } else {

                //위치 권한 없으면 받아오기
                getLocation()
            }
        }

        //show_btn 클릭시 이벤트
        enjoy_btn.setOnClickListener {
            //위치 권한 체크
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
                    }

                    override fun onResponse(call: Call<Enjoyclass>, response: Response<Enjoyclass>) {
                        //웹 통신에 성공 했을 때 실행되는 코드
                        val doitdata = response.body()
                        val dialog = AlertDialog.Builder(this@Question_Activity)
                        dialog.setTitle("뚱이의 답변")
                        dialog.setMessage(doitdata?.title + "에 가보는건 어때?\n 주소는 " + doitdata?.address + " 여기야!" )
                        dialog.show()
                    }
                })
            } else {

                //위치 권한 없으면 받아오기
                getLocation()
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
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000

        mFusedLocationProviderClient = FusedLocationProviderClient(this)
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())
    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {

            val location : Location = p0!!.lastLocation

            updateAddressUI(location)
        }
    }

    fun updateAddressUI(location : Location){
        var geocoder : Geocoder
        var addressList = ArrayList<Address>()

        geocoder = Geocoder(this, Locale.getDefault())

        addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1) as ArrayList<Address>

        val userAdd = addressList.get(0).getAddressLine(0)
        val addr = userAdd.split(" ")

        areafather = addr[1]
        areaname = addr[2]

        return

//        println("주소 : " + addr[1] +addr[2])
    }
}