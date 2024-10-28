package ru.zatsoft.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(private val context: Context, private val dataList: MutableList<Product> )
    : ArrayAdapter<Product>(context, R.layout.list_item, dataList)
{

    override fun getCount(): Int {
      return dataList.size
    }

    override fun getItem(position: Int): Product{
         return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, v: View?, parent: ViewGroup): View{
        val view = v?: LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        val data = getItem(position)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvWeight = view.findViewById<TextView>(R.id.tvWeight)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

        tvName.text = data.name
        tvWeight.text = data.weight.toString()
        tvPrice.text = data.price.toString()

        return view
    }
}