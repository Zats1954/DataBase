package ru.zatsoft.spinner

import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.zatsoft.spinner.databinding.ActivityExecBinding


class ExecActivity : AppCompatActivity(), Removable {

    private val db = DBHelper(this, null)

    private val role = mutableListOf(
        "Должность",
        "Бухгалтер",
        "Кладовщик",
        "Менеджер",
        "Администратор",
        "Сортировщик"
    )

    private lateinit var binding: ActivityExecBinding
    private var list = mutableListOf<Person>()
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExecBinding.inflate(layoutInflater)
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

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            role
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spinner.adapter = spinnerAdapter
        var selectedItem = " "
        val itemSelected: OnItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        selectedItem = parent?.getItemAtPosition(position) as String
                    else
                        selectedItem = " "
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedItem = " "
                }
            }
        binding.spinner.onItemSelectedListener = itemSelected

        binding.btnAdd.setOnClickListener {
            val name = binding.edName.text.toString()
            val lastName = binding.edLastName.text.toString()
            val age = binding.edAge.text.toString()
            val position = selectedItem
            if (name.equals("") || lastName.equals("") || age.toIntOrNull() == null || position.equals(
                    ""
                )
            ) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                db.addName(name, lastName, age, position)
                Toast.makeText(
                    this,
                    "$name $lastName $age $position добавлены в базу данных",
                    Toast.LENGTH_LONG
                ).show()
                clearFields()
            }
        }

        binding.btnInfo.setOnClickListener {
            val cursor = db.getInfo()
            list.clear()
            if (cursor != null && cursor.moveToFirst()) {
                addPerson(cursor)
            }
            while (cursor!!.moveToNext()) {
                addPerson(cursor)
            }
            cursor.close()
            listAdapter.notifyDataSetChanged()
            inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
//   Очистка полей
            clearFields()
            selectedItem = " "
        }

        binding.btnClear.setOnClickListener {
            db.removeAll()
            list.clear()
            listAdapter.notifyDataSetChanged()

        }
    }

    private fun addPerson(cursor: Cursor) {
        list.add(
            Person(
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_AGE)).toInt(),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_POSITION))
            )
        )
    }

    private fun clearFields() {
        binding.edName.text.clear()
        binding.edLastName.text.clear()
        binding.edAge.text.clear()
        binding.spinner.setSelection(0)
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

    override fun remove(person: Person) {
        db.removePerson(person)
        list.removeAt(list.indexOf(person))
        listAdapter.notifyDataSetChanged()
    }

}