package ru.zatsoft.spinner

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.zatsoft.spinner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 200
    private var selectedId = -1L
    private lateinit var selectedProduct: Product
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
                selectedProduct = listAdapter.getItem(position)
                selectedId = selectedProduct.id
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnUpdate.visibility = View.VISIBLE
                listAdapter.notifyDataSetChanged()
            }
//  Заполняем список list из БД, обновляем адаптер
        readDB()

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val weight: Int
            val price: Int
            if (name.equals("")) {
                Toast.makeText(this, "Нет наименования продукта", Toast.LENGTH_LONG).show()
            } else {
                try {
                    weight = binding.etWeight.text.toString().toInt()
                    price = binding.etPrice.text.toString().toInt()
                    db.addName(name, weight, price)
                    Toast.makeText(
                        this,
                        "$name $weight $price  добавлены в базу данных",
                        Toast.LENGTH_LONG
                    ).show()
                    clearFields()
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        this,
                        "вес и цена  должны быть числами ",
                        Toast.LENGTH_LONG
                    ).show()
                }
//  Заполняем список list из БД, обновляем адаптер
                readDB()
                inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            }
        }

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("product", selectedProduct)
            startActivityForResult(intent, REQUEST_CODE)
//  Заполняем список list из БД, обновляем адаптер
            readDB()
            inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
//   Очистка полей
            clearFields()
        }

        binding.btnDelete.setOnClickListener {
            if (db.removeProduct(selectedId)) {
                readDB()
                listAdapter = ListAdapter(this, list)
                binding.listView.adapter = listAdapter
                binding.btnDelete.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
            }
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
                cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_WEIGHT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_PRICE))
            )
        )
    }

    private fun clearFields() {
        binding.etName.text.clear()
        binding.etWeight.text.clear()
        binding.etPrice.text.clear()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.extras?.getParcelable("product1") as Product?
                db.update(result)
                readDB()
                listAdapter = ListAdapter(this, list)
                binding.listView.adapter = listAdapter
                binding.btnDelete.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
            }
        }
    }
}