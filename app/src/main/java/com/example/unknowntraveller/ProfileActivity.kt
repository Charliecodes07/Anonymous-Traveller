package com.example.unknowntraveller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val editbtn = findViewById<Button>(R.id.edit_profile_button)
        editbtn.setOnClickListener{
            val Intent = Intent(this, EditProfileActivity::class.java)
            startActivity(Intent)
        }

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<ItemsViewModel>()

        data.add(ItemsViewModel(R.drawable.bgimg, "Location 1", "User 1"))
        data.add(ItemsViewModel(R.drawable.bgimg, "Location 2", "User 2"))
        data.add(ItemsViewModel(R.drawable.bgimg, "Location 3", "User 3"))

        val adapter = CustomAdapter(data)

        recyclerview.adapter = adapter
    }
}