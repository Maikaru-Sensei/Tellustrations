package at.fhooe.tellustrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ActivityCheckBuffer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_buffer)

        val preference = sharedPreferences(this)
        val msg = findViewById<TextView>(R.id.textView_acCheck_Buffer)
        msg.text = preference.getStrBuffer()
    }
}