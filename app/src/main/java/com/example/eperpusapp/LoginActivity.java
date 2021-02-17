package com.example.eperpusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.eperpusapp.Fragment.HomeFragment;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemUser;
import com.example.eperpusapp.Model.ResponseUser;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.ActivityBookDetailBinding;
import com.example.eperpusapp.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressDialog dialog;
    DataItemUser user;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkBtn();
        binding.nimMhs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.nimMhs.getText().toString();
            String password = binding.password.getText().toString();

            if (username.isEmpty()) {
                binding.nimMhs.setError("NIM is Required");
                binding.nimMhs.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                binding.password.setError("Password is Required");
                binding.password.requestFocus();
                return;
            } else {
                login(username, password);
                binding.btnLogin.setEnabled(true);
            }
        });

    }

    private void checkBtn() {
        Editable result = binding.nimMhs.getText();
        Editable result2 = binding.password.getText();
        if (result != null && !result.toString().isEmpty() && result2 != null && !result2.toString().isEmpty()) {
            binding.btnLogin.setEnabled(true);
        } else {
            binding.btnLogin.setEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is logged id
        //if user is logged in, move to main
        checkSession();
    }

    private void checkSession() {
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        userID = sessionManagement.getSession();

        if (userID != -1) {
            moveToMain();
        } else {

        }
    }

    public void login(String username, String password) {
        //1.login to app and save session user
        //2. move to mainactivity

        dialog.setMessage("Please Wait...");
        dialog.show();
        ApiService.apiCall().getUserDetail(username, password)
                .enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        if (response.isSuccessful()) {
//                            user = response.body().getData();
//                            String nama = user.getNamaLengkap();
//                            Log.d("HASIL", nama);
                            ResponseUser resp = response.body();
                            user = resp.getData();
                            if (resp.getMessage().equals("Login Successful")) {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Welcome " + user.getNamaLengkap(), Toast.LENGTH_SHORT).show();
                                SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                sessionManagement.saveSession(user);
                                moveToMain();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "YA GAGAL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moveToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}