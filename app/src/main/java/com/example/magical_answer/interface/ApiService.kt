package com.example.magical_answer.`interface`
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded //인코딩 문제 해결
    @POST("/api/question/nobase") // GET,POST,UPDATE,DELETE 및 api주소 선언
    fun requestData(
        //input data
        @Field("no") no:Int
    ) : Call<nobase> // output data
}