package ir.yamin.digitstest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ir.yamin.digits.Digits

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val editText = findViewById<EditText>(R.id.numberEditTextJava)
        val textView = findViewById<TextView>(R.id.textViewJava)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val number = s.toString()
                textView.text = Digits(this@MainActivity2).spellToIranMoney(number, true)
            }
        })
    }
}