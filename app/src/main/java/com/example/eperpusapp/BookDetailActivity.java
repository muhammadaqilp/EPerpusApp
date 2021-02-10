package com.example.eperpusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eperpusapp.Fragment.HomeFragment;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class BookDetailActivity extends AppCompatActivity {

    TextView back, bookmark, txtSummary;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        back = findViewById(R.id.btnBack);
        bookmark = findViewById(R.id.btnBookmark);
        txtSummary = findViewById(R.id.txt_summary);
        back.bringToFront();
        bookmark.bringToFront();

        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtSummary.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}