package com.example.magical_answer.`interface`

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.magical_answer.R

class MemoAdapter( val mymemoList : ArrayList<mymemo>) : RecyclerView.Adapter<MemoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // 아이템 정의
        val no = itemView.findViewById<TextView>(R.id.item_no)
        val title = itemView.findViewById<TextView>(R.id.item_title)
        val CreateTime = itemView.findViewById<TextView>(R.id.item_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 메모 아이템 리스트 레이아웃 연결부분
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.memo_custom, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        // 가져올 갯수 정하는 부분
        return mymemoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //바인딩 하는부분 = 아이템 매칭
        val memos : mymemo = mymemoList[position]

        holder?.no?.text = memos.no
        holder?.title?.text = memos.title
        holder?.CreateTime?.text = memos.CreateTime

    }
}