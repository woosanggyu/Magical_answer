package com.example.magical_answer.`interface`


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