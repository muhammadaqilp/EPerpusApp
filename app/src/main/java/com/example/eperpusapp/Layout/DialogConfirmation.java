package com.example.eperpusapp.Layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.eperpusapp.Fragment.HomeFragment;
import com.example.eperpusapp.Fragment.MyBookFragment;
import com.example.eperpusapp.Model.DataItemMyBook;
import com.example.eperpusapp.Model.ResponseMyBook;
import com.example.eperpusapp.Model.ResponseReturn;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.R;
import com.example.eperpusapp.ReadBookActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogConfirmation extends Dialog implements View.OnClickListener{

    private Context context;
    private Button btnYes, btnNo;
    private int idBuku, idUser, idPinjam;
    HashMap<Integer, Integer> hashMap = new HashMap<>();
    private String file;
    private ProgressDialog dialog;

    public DialogConfirmation(@NonNull Context context, int idBuku, int idUser, int idPinjam, String file) {
        super(context);
        this.context = context;
        this.idBuku = idBuku;
        this.idUser = idUser;
        this.idPinjam = idPinjam;
        this.file = file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog1);

        btnNo = findViewById(R.id.btnNo);
        btnYes = findViewById(R.id.btnYes);

        dialog = new ProgressDialog(getContext());

        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnYes:
                try {
                    getTimestamp();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnNo:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

    private void getTimestamp() throws ParseException {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar calendar = null;
        for (int i = 0; i < 7; i++) {
            calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
        }
        String day = sdf.format(calendar.getTime());

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = formatter.parse(day);
        Long a = Long.valueOf(date.getTime())/1000;
        String timestamp = a.toString();

        dialog.setMessage("Borrowing Book...");
        dialog.show();

        ApiService.apiCall().getResponseBorrow(idUser, idBuku, ts, timestamp)
                .enqueue(new Callback<ResponseReturn>() {
                    @Override
                    public void onResponse(Call<ResponseReturn> call, Response<ResponseReturn> response) {
                        if (response.isSuccessful()){
                            ResponseReturn resp = response.body();
                            String msg = resp.getMessage();
                            if(msg.equals("1")){
                                dialog.dismiss();
                                Toast.makeText(context, "Success to Borrow Book", Toast.LENGTH_SHORT).show();
                                mybookList(idUser);
                                Intent i = new Intent(getContext(), ReadBookActivity.class);
                                i.putExtra(ReadBookActivity.EXTRA_BOOK_FILE, file);
                                i.putExtra(ReadBookActivity.EXTRA_BOOK_IDPINJAM, idPinjam);
                                getContext().startActivity(i);
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(context, "Failed to Borrow Book", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReturn> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mybookList(int session) {
        ApiService.apiCall().getMyBookList(session)
                .enqueue(new Callback<ResponseMyBook>() {
                    @Override
                    public void onResponse(Call<ResponseMyBook> call, Response<ResponseMyBook> response) {
                        if (response.isSuccessful()){
                            List<DataItemMyBook> dataItemssz = response.body().getData();
                            if (dataItemssz.isEmpty()) {
                                saveToMy(null);
                            } else {
                                for (DataItemMyBook dataItems : dataItemssz) {
//                                    idmybook.add(dataItems.getIdBuku());
                                    hashMap.put(dataItems.getIdBuku(), dataItems.getIdPinjam());
                                }
//                                Log.d("LIST", String.valueOf(idmybook));
                                saveToMy(hashMap);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMyBook> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("MY BOOK FRAGMENT", t.getMessage());
                    }
                });
    }

    private void saveToMy(HashMap<Integer, Integer> hashMap) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMap);

        String TAG = "LIST_BORROW_"+idUser;

        edit.putString(TAG, json);
        edit.commit();
    }
}