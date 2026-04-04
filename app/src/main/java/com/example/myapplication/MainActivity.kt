package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        if (!sharedPref.getBoolean("isRegistered", false)) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
            return
        }

        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvEmail = findViewById<TextView>(R.id.tv_email)
        val tvGender = findViewById<TextView>(R.id.tv_gender)
        val tvHobbies = findViewById<TextView>(R.id.tv_hobbies)
        val tvCity = findViewById<TextView>(R.id.tv_city)
        val copyEmailBtn = findViewById<Button>(R.id.copy_email_btn)
        val logoutBtn = findViewById<Button>(R.id.logout_btn)

        tvName.text = sharedPref.getString("name", "")
        tvEmail.text = sharedPref.getString("email", "")
        tvGender.text = sharedPref.getString("gender", "")
        tvHobbies.text = sharedPref.getString("hobbies", "")
        tvCity.text = sharedPref.getString("city", "")

        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        copyEmailBtn.setOnClickListener {
            clipboard.setPrimaryClip(ClipData.newPlainText("email", tvEmail.text))
            Toast.makeText(this, "Email copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        copyEmailBtn.setOnLongClickListener {
            val info =
                "Name: ${tvName.text}\nEmail: ${tvEmail.text}\nGender: ${tvGender.text}\nHobbies: ${tvHobbies.text}\nCity: ${tvCity.text}"
            clipboard.setPrimaryClip(ClipData.newPlainText("profile", info))
            Toast.makeText(this, "Profile info copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }

        logoutBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    sharedPref.edit { clear() }
                    startActivity(Intent(this, RegisterActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
