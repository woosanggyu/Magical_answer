package com.example.magical_answer.`interface`

import android.widget.ListView
import com.google.gson.JsonArray
import java.util.*
import kotlin.collections.ArrayList


data class ApiGetclass(
    // 받아올 데이터 선언
    var msg : String
)

data class Eatgetclass(
    var title : String,
    var address : String
)

data class Doitclass(
    var title : String,
    var address : String
)

data class Enjoyclass(
    var title : String,
    var address : String
)

data class Loginclass(
    var message : String,
    var ID : String,
    var Nickname : String,
    var token : String
)
data class checkidclass(
    var msg : String
)

data class checknickclass(
    var msg : String
)

data class Registclass(
    var msg : String
)

data class Mymemolist(
    val answer : JsonArray
)

data class mymemo(
    val no : String,
    val writer : String,
    val title : String,
    val content : String,
    val CreateTime : String
)

data class addmemo(
    val answer : String
)

data class memoview(
    val title : String,
    val con : String
)

data class updatememo(
    val answer: String
)

