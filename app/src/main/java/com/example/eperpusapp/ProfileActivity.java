package com.example.eperpusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.ActivityBookDetailBinding;
import com.example.eperpusapp.databinding.ActivityLoginBinding;
import com.example.eperpusapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    public static final String EXTRA_USER_DETAIL = "extra_user_detail";
    SessionManagement sessionManagement;

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

        btnExit.setOnClickListener(v ->{
            logout();
        });

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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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