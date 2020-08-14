package com.example.magical_answer.`interface`

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.magical_answer.Memo_Activity
import com.example.magical_answer.R

class MemoListAdaper (val context : Context, val memolist : ArrayList<Memo_Activity.memo>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다.
        val view : View = LayoutInflater.from(context).inflate(R.layout.memo_custom, null)

        // 위에서 생성한 view 와 xml파일의 view를 연결
        val memono = view.findViewById<TextView>(R.id.item_no)
        val memotitle = view.findViewById<TextView>(R.id.item_title)
        val memodate = view.findViewById<TextView>(R.id.item_date)

        // ArrayList<memo>에 내용 담기
        val memo = memolist[position]
        memono.text = memo.no
        memotitle.text = memo.title
        memodate.text = memo.dates

        return view
    }

    override fun getItem(p0: Int): Any {
        return memolist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return memolist.size
    }


}