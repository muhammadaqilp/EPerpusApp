package com.example.eperpusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.eperpusapp.Layout.DialogConfirmation;
import com.example.eperpusapp.Layout.DialogNoInternet;
import com.example.eperpusapp.Model.DataItemUser;
import com.example.eperpusapp.Model.Login;
import com.example.eperpusapp.Model.ResponseUser;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.ActivityLoginBinding;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressDialog dialog;
    DataItemUser user;
    int userID;
    private String AES = "AES";
    Login login = new Login();

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
                if (checkConnection() == false) {
                    Log.d("SHOW ALERT", String.valueOf(checkConnection()));
                    showAlert();
                } else {
                    Log.d("SHOW ALERT", String.valueOf(checkConnection()));

                    String a = "AQIL";
                    String out;
                    try {
//                        out = encrypt(a, password);
//                        Log.d("ENCRYPT", out);
                        login.setUsername(username);
                        login.setPassword(password);
                        login(login);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.btnLogin.setEnabled(true);
                }
            }
        });

    }

    private String encrypt(String data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);

        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
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

    public void login(Login login) {
        //1.login to app and save session user
        //2. move to mainactivity

        dialog.setMessage("Logging in...");
        dialog.show();
        Log.d("SUBMIT", login.getUsername());
        Log.d("SUBMIT", login.getPassword());
        ApiService.apiCall().getUserDetail(login)
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
                                Toast.makeText(LoginActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
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

    private boolean checkConnection() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }

//    private void showAlert() {
//        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("No Internet Connection!");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss",
//                (dialog, which) -> dialog.dismiss());
//        alertDialog.show();
//    }

    private void showAlert() {
        DialogNoInternet dialog = new DialogNoInternet(LoginActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}