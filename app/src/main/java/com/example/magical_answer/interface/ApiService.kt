package com.example.magical_answer.`interface`
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("/api/requestdata/enjoydata")
    fun requestenjoyData(
        @Field("areafather") areafather:String,
        @Field("areaname") areaname : String
    ) : Call<Enjoyclass>

    @FormUrlEncoded
    @POST("/api/user/signin")
    fun requestLogin(
        @Field("id") id:String,
        @Field("password") password : String
    ) : Call<Loginclass>

    @FormUrlEncoded
    @POST("/api/user/checkid")
    fun requestcheckid(
        @Field("id") id:String
    ) : Call<checkidclass>

    @FormUrlEncoded
    @POST("/api/user/checknick")
    fun requestchecknick(
        @Field("nickname") nickname:String
    ) : Call<checknickclass>

    @FormUrlEncoded

    @POST("/api/user/signup")
    fun requestregist(
        @Field("id") id:String,
        @Field("password") password:String,
        @Field("nickname") nickname:String,
        @Field("gender") gender:String,
        @Field("age") age:String
    ) : Call<Registclass>

    @FormUrlEncoded
    @POST("/api/requestdata/mymemo")
    fun requestmymemo(
        @Header("token") token : String,
        @Field("nickname") nickname: String
    ) : Call<Mymemolist>

    @FormUrlEncoded
    @POST("/api/requestdata/addmemo")
    fun requestaddmemo(
        @Header("token") token : String,
        @Field("nickname") nickname: String,
        @Field("title") title: String,
        @Field("content") content: String
    ) : Call<addmemo>

    @FormUrlEncoded
    @POST("/api/requestdata/memoview")
    fun requestmemoview(
        @Header("token") token : String,
        @Field("nickname") nickname: String,
        @Field("no") no: String
    ) : Call<memoview>

    @FormUrlEncoded
    @PUT("/api/requestdata/updatememo")
    fun requestupdatememo(
        @Header("token") token : String,
        @Field("nickname") nickname: String,
        @Field("no") no: String,
        @Field("title") title: String,
        @Field("content") content: String
    ) : Call<updatememo>
}