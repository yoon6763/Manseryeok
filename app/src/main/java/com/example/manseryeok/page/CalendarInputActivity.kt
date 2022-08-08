package com.example.manseryeok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.manseryeok.databinding.ActivityCalendarInputBinding

class CalendarInputActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            btnCalenderInputFinish.setOnClickListener {

                val intent = Intent(this@CalendarInputActivity, CalendarActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}