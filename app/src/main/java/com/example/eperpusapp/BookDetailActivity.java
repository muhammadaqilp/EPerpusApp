package com.example.eperpusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eperpusapp.Fragment.HomeFragment;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);


        TextView back = findViewById(R.id.btnBack);
        TextView bookmark = findViewById(R.id.btnBookmark);
        back.bringToFront();
        bookmark.bringToFront();


        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}