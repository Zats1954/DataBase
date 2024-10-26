package ru.zatsoft.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(private val context: Context, private val dataList: MutableList<Person> )
    : ArrayAdapter<Person>(context, R.layout.list_item, dataList)
{

    override fun getCount(): Int {
      return dataList.size
    }

    override fun getItem(position: Int): Person{
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, v: View?, parent: ViewGroup): View{
        val view = v?: LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        val data = getItem(position)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvLastName = view.findViewById<TextView>(R.id.tvLastName)
        val tvAge = view.findViewById<TextView>(R.id.tvAge)
        val tvPosition = view.findViewById<TextView>(R.id.tvPosition)

        tvName.text = data.name
        tvLastName.text = data.lastName
        tvAge.text = data.age.toString()
        tvPosition.text = data.position
        return view
    }
}