package com.example.eperpusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eperpusapp.Adapter.CollectionBookAdapter;
import com.example.eperpusapp.Adapter.WishlistBookAdapter;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.ActivityBookDetailBinding;
import com.example.eperpusapp.databinding.ActivityLoginBinding;
import com.example.eperpusapp.databinding.ActivityProfileBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    public static final String EXTRA_USER_DETAIL = "extra_user_detail";
    private WishlistBookAdapter adapter;
    private List<DataItemBuku> dataItemList;
    SessionManagement sessionManagement;
    public List<Integer> idbook = new ArrayList<>();
    int iduser;

    Toolbar toolbar;
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        btnExit = findViewById(R.id.btn_exit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManagement = new SessionManagement(ProfileActivity.this);

        showProfile();
        dataItemList = new ArrayList<>();
        iduser = sessionManagement.getSession();
        showReadingList(iduser);
        btnExit.setOnClickListener(v -> {
            logout();
        });

    }

    private void showReadingList(int id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getWishlist(id)
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, Response<ResponseBuku> response) {
                        if (response.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            idbook.clear();
                            List<DataItemBuku> dataItemss = response.body().getData();
                            if (dataItemss.isEmpty()) {
                                binding.emptyWishlist.setVisibility(View.VISIBLE);
                                binding.rvWishlist.setVisibility(View.GONE);
                            } else {
                                for (DataItemBuku dataItems : dataItemss) {
                                    dataItemList.add(dataItems);
                                    idbook.add(dataItems.getIdbuku());
                                    adapter = new WishlistBookAdapter(dataItemList);
//                                adapter.notifyItemInserted(dataItemList.lastIndexOf(dataItemList));

                                    binding.rvWishlist.setHasFixedSize(true);
                                    binding.rvWishlist.setLayoutManager(new LinearLayoutManager(ProfileActivity.this, RecyclerView.HORIZONTAL, false));
                                    binding.rvWishlist.setAdapter(adapter);
                                }
                                Log.d("LIST", String.valueOf(idbook));
                                saveTo(idbook);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBuku> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("PROFILE ACTIVITY", t.getMessage());
                    }
                });
    }

    private void saveTo(List<Integer> a) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(a);

        String TAG = "LIST_ID_"+sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }

    private void showProfile() {
        String photo = sessionManagement.getSessionPhoto();
        String nama = sessionManagement.getSessionName();
        String nim = sessionManagement.getSessionNim();
        String prodi = sessionManagement.getSessionProdi();
        String fakultas = sessionManagement.getSessionFakultas();
        int angkatan = sessionManagement.getSessionAngkatan();
        String status = sessionManagement.getSessionStatus();

        Glide.with(this)
                .load(photo)
                .into(binding.photoImage);
        binding.txtNama.setText(nama);
        binding.txtNim.setText(nim);
        binding.txtProdi.setText(prodi);
        binding.txtFakultas.setText(fakultas);
        binding.txtAngkatan.setText(String.valueOf(angkatan));
        binding.txtStatus.setText(status);
    }

    private void logout() {
        sessionManagement = new SessionManagement(ProfileActivity.this);
        sessionManagement.removeSession();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dataItemList.clear();
        showReadingList(iduser);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}