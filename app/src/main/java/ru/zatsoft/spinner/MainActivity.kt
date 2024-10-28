package ru.zatsoft.spinner

import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.zatsoft.spinner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Removable {

    private val db = DBHelper(this, null)
    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<Product>()
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        title = " "
        val inputKeyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        listAdapter = ListAdapter(this, list)
        binding.listView.adapter = listAdapter
        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedPerson = listAdapter.getItem(position)
                remove(selectedPerson)
                listAdapter.notifyDataSetChanged()
            }

        binding.btnAdd.setOnClickListener {
            val name = binding.edName.text.toString()
            var weight: Int
            var price: Int
            if (name.equals("") )
             {
                Toast.makeText(this, "Нет наименования продукта", Toast.LENGTH_LONG).show()
            } else {
                try{
                weight = binding.edWeight.text.toString().toInt()
                price = binding.edPrice.text.toString().toInt()
                db.addName(name, weight, price)
                Toast.makeText(
                    this,
                    "$name $weight $price  добавлены в базу данных",
                    Toast.LENGTH_LONG
                ).show()
                clearFields()}
                catch(e: NumberFormatException){
                    Toast.makeText(
                        this,
                        "вес и цена  должны быть числами ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                readDB()

            }
        }

        binding.btnInfo.setOnClickListener {
            readDB()
            inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
//   Очистка полей
            clearFields()
        }

        binding.btnClear.setOnClickListener {
            db.removeAll()
            list.clear()
            listAdapter.notifyDataSetChanged()

        }
    }

    private fun readDB() {
        val cursor = db.getInfo()
        list.clear()
        if (cursor != null && cursor.moveToFirst()) {
            addProduct(cursor)
        }
        while (cursor!!.moveToNext()) {
            addProduct(cursor)
        }
        cursor.close()
        listAdapter.notifyDataSetChanged()
    }

    private fun addProduct(cursor: Cursor) {
        list.add(
            Product(
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_WEIGHT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_PRICE))
            )
        )
    }

    private fun clearFields() {
        binding.edName.text.clear()
        binding.edWeight.text.clear()
        binding.edPrice.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun remove(product: Product) {
        db.removePerson(product)
        list.removeAt(list.indexOf(product))
        listAdapter.notifyDataSetChanged()
    }

}