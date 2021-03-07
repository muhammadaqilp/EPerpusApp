package com.example.eperpusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eperpusapp.Layout.DialogConfirmation;
import com.example.eperpusapp.Layout.DialogNoInternet;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.ResponseReturn;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.ActivityBookDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class BookDetailActivity extends AppCompatActivity {

    private ActivityBookDetailBinding binding;
    public static final String EXTRA_BOOK_DETAIL = "extra_book_detail";
    SessionManagement sessionManagement;
    private List<DataItemBuku> dataItems;
    private DataItemBuku dataItem;
    private List<Integer> arraylist;
    private boolean var, van, varmy;
    private int iduser, idpinjam, a, b, c;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManagement = new SessionManagement(this);

        binding.btnBack.bringToFront();
        binding.btnBookmark.bringToFront();

        binding.btnBack.setOnClickListener(v -> finish());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.txtSummary.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        dataItem = getIntent().getParcelableExtra(EXTRA_BOOK_DETAIL);

        Glide.with(this)
                .load(dataItem.getFotoBuku())
                .into(binding.img);
        binding.bookTitleDetail.setText(dataItem.getJudulBuku());
        binding.bookAuthorsDetail.setText(dataItem.getPengarang());
        binding.isbnDetail.setText(dataItem.getIsbn());
        binding.publisherDetail.setText(dataItem.getPenerbit());
        binding.publishedYear.setText(String.valueOf(dataItem.getTahunTerbit()));
        binding.totalPages.setText(String.valueOf(dataItem.getJumlahHalaman()));
        binding.bookCategory.setText(dataItem.getKategori());
        binding.txtSummary.setText(dataItem.getSinopsis());

        a = dataItem.getJumlahCopy();
        b = dataItem.getTotalDipinjam();
        c = a - b;

        iduser = sessionManagement.getSession();

        varmy = checkMyBook(dataItem.getIdbuku());
        checkBtnRead(varmy);

        checkIcon();

        binding.btnBookmark.setOnClickListener(v -> {
            van = readList(dataItem.getIdbuku(), sessionManagement.getSession());
            checkBtnBookmark(van);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        varmy = checkMyBook(dataItem.getIdbuku());
        checkBtnRead(varmy);
    }

    private void checkBtnBookmark(boolean van) {
        if (van == true){
            removeWishlist(sessionManagement.getSession(), dataItem.getIdbuku());
            binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            int idnya = 0;
            for (int z = 0; z < arraylist.size(); z++){
                if (dataItem.getIdbuku() == arraylist.get(z)){
                    idnya = z;
                }
            }
            arraylist.remove(idnya);
            saveTo(arraylist, sessionManagement.getSession());
            Log.d("VARARRAY", String.valueOf(arraylist));
        }
        else if (van == false){
            addWishlist(sessionManagement.getSession(), dataItem.getIdbuku());
            binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
            arraylist.add(dataItem.getIdbuku());
            saveTo(arraylist, sessionManagement.getSession());
            update();
            Log.d("VARARRAY", String.valueOf(arraylist));
        }
    }

    private void checkBtnRead(boolean varmy) {
        if (varmy != true) {
            if (c == 0) {
                binding.btnBorrow.setEnabled(false);
                binding.jumlahAvail.setText("Not Available");
            } else {
                binding.jumlahAvail.setText("Available " + c + " / " + a);
                binding.btnBorrow.setOnClickListener(v -> {
                    showDialog(dataItem.getIdbuku(), sessionManagement.getSession(), dataItem.getFileBuku());
                });
            }
        }

        else {
            binding.btnBorrow.setVisibility(View.GONE);
            binding.btnRead.setVisibility(View.VISIBLE);
            if (c == 0) {
                binding.jumlahAvail.setText("Not Available");
            }
            else {
                binding.jumlahAvail.setText("Available " + c + " / " + a);
            }
        }
    }

    private void checkIcon() {
        var = readList(dataItem.getIdbuku(), sessionManagement.getSession());

        if (var == true){
            binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
        else {
            binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
        }
    }

    private void update() {
        var = false;
        van = false;
    }

    private boolean readList(int idbuku, int iduser) {
        boolean val = false;

        String TAG = "LIST_ID_"+iduser;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sp.getString(TAG, "");
        Type type = new TypeToken<List<Integer>>() {}.getType();
        arraylist = gson.fromJson(json, type);

        Log.d("IDLISTBUKU", String.valueOf(arraylist));

        if (arraylist == null){
            val = false;
        }

        else {
            for (int a = 0; a < arraylist.size(); a++) {
                if (arraylist.get(a) == idbuku) {
                    val = true;
                }
            }
        }
        return val;
    }

    private void saveTo(List<Integer> a, int iduser) {
        String TAG = "LIST_ID_"+iduser;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(a);

        edit.putString(TAG, json);
        edit.commit();
    }

    private void addWishlist(int session, int idbuku) {
        ApiService.apiCall().getResponseAddWishlist(session, idbuku)
                .enqueue(new Callback<ResponseReturn>() {
                    @Override
                    public void onResponse(Call<ResponseReturn> call, Response<ResponseReturn> response) {
                        if (response.isSuccessful()){
                            ResponseReturn resp = response.body();
                            String msg = resp.getMessage();
                            if(msg.equals("1")){
                                Toast.makeText(BookDetailActivity.this, "Book Added to Wishlist", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(BookDetailActivity.this, "Failed to Added Book to Wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReturn> call, Throwable t) {
                        Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeWishlist(int session, int idbuku) {
        ApiService.apiCall().getResponseRemoveWishlist(session, idbuku)
                .enqueue(new Callback<ResponseReturn>() {
                    @Override
                    public void onResponse(Call<ResponseReturn> call, Response<ResponseReturn> response) {
                        if (response.isSuccessful()){
                            ResponseReturn resp = response.body();
                            String msg = resp.getMessage();
                            if(msg.equals("1")){
                                Toast.makeText(BookDetailActivity.this, "Book Removed From Wishlist", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(BookDetailActivity.this, "Failed to Remove Book From Wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReturn> call, Throwable t) {
                        Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkMyBook(int idbuku){
        boolean val = false;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String zz = "LIST_BORROW_"+sessionManagement.getSession();
        String json = sp.getString(zz, "");
        Type type = new TypeToken<HashMap<Integer, Integer>>() {}.getType();
        HashMap<Integer, Integer> yy = gson.fromJson(json, type);

        Log.d("IDLISTBUKUMYBORROW", String.valueOf(yy));

        if (yy == null){
            val = false;
        }
        else {
            for (int key : yy.keySet()) {
                if (key == idbuku) {
                    val = true;
                    idpinjam = yy.get(key);
                }
            }
        }
        return val;
    }

    private void showDialog(int idBuku, int idUser, String file) {
        DialogConfirmation dialog = new DialogConfirmation(this, idBuku, idUser, file);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

//    private boolean readListMy(int idbuku, int iduser) {
//        boolean val = false;
//
//        String TAG = "LIST_MYBOOK_"+iduser;
//
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        Gson gson = new Gson();
//        String json = sp.getString(TAG, "");
//        Type type = new TypeToken<List<Integer>>() {}.getType();
//        arraylistmy = gson.fromJson(json, type);
//
//        Log.d("IDLISTBUKUMY", String.valueOf(arraylistmy));
//
//        if (arraylistmy == null){
//            val = false;
//        }
//        else {
//            for (int a = 0; a < arraylistmy.size(); a++) {
//                if (arraylistmy.get(a) == idbuku) {
//                    val = true;
//                }
//            }
//        }
//        return val;
//    }

}