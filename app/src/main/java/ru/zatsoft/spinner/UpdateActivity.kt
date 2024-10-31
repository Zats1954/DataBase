package ru.zatsoft.spinner

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.zatsoft.spinner.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var product: Product
    private lateinit var binding: ActivityUpdateBinding
    private var id = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        title = " "
//        val inputKeyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManagers
        product = intent.getParcelableExtra("product")!!
        fillFields()

        binding.btnCancel.setOnClickListener {
            fillFields()
        }

        binding.btnSave.setOnClickListener {
            val newProduct = getFields()
            newProduct?.let{
            val intent1 = Intent()
            intent1.putExtra("product1", it)
            setResult(RESULT_OK, intent1)
            finish()}
        }
    }

    private fun getFields(): Product? {
        var name = " "
        var weight = 0
        var price = 0
        try{
            name = binding.etName.text.toString()
            weight = binding.etWeight.text.toString().toInt()
            price = binding.etPrice.text.toString().toInt()
        } catch(e: NumberFormatException) {
            Toast.makeText(
                this,
                "вес и цена  должны быть числами ",
                Toast.LENGTH_LONG
            ).show()}
        if(name.trim() != ""){
           return Product(id,name, weight, price)
        }else {
            Toast.makeText(
            this,
            "Нет наименования продукта",
            Toast.LENGTH_LONG
        ).show()
            return null}
    }

    private fun fillFields() {
        id = product.id
        binding.etName.setText(product.name)
        binding.etWeight.setText(product.weight.toString())
        binding.etPrice.setText(product.price.toString())
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
}