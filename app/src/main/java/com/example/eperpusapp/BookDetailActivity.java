package com.example.eperpusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.databinding.ActivityBookDetailBinding;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class BookDetailActivity extends AppCompatActivity {

    private ActivityBookDetailBinding binding;
    public static final String EXTRA_BOOK_DETAIL = "extra_book_detail";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.bringToFront();
        binding.btnBookmark.bringToFront();

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.txtSummary.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        DataItemBuku dataItem = getIntent().getParcelableExtra(EXTRA_BOOK_DETAIL);

        binding.bookTitleDetail.setText(dataItem.getJudulBuku());
        binding.bookAuthorsDetail.setText(dataItem.getPengarang());
        binding.isbnDetail.setText(dataItem.getIsbn());
        binding.publisherDetail.setText(dataItem.getPenerbit());
        binding.publishedYear.setText(String.valueOf(dataItem.getTahunTerbit()));
        binding.totalPages.setText(String.valueOf(dataItem.getJumlahHalaman()));
        binding.bookCategory.setText(dataItem.getKategori());
        binding.txtSummary.setText(dataItem.getSinopsis());

        int a = dataItem.getJumlahCopy();
        int b = dataItem.getTotalDipinjam();
        int c = a-b;
        if (c == 0){
            binding.btnBorrow.setEnabled(false);
            binding.jumlahAvail.setText("Not Available");
        }
        else {
            binding.jumlahAvail.setText("Available " + c + " / " + a);
        }
    }
}