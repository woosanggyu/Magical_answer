package com.example.magical_answer.`interface`
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded //인코딩 문제 해결
    @POST("/api/question/nobase") // GET,POST,UPDATE,DELETE 및 api주소 선언
    fun requestData(
        //input data
        @Field("no") no:Int
    ) : Call<ApiGetclass> // output data

    @FormUrlEncoded
    @POST("/api/requestdata/eatdata")
    fun requesteatData(
        @Field("areafather") areafather:String,
        @Field("areaname") areaname : String
    ) : Call<Eatgetclass>

    @FormUrlEncoded
    @POST("/api/requestdata/doitdata")
    fun requestdoitData(
        @Field("areafather") areafather:String,
        @Field("areaname") areaname : String
    ) : Call<Doitclass>
}