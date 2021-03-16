package com.example.eperpusapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eperpusapp.Adapter.CollectionBookAdapter;
import com.example.eperpusapp.Adapter.MyBookAdapter;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemMyBook;
import com.example.eperpusapp.Model.ResponseMyBook;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.R;
import com.example.eperpusapp.ReadBookActivity;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.FragmentHomeBinding;
import com.example.eperpusapp.databinding.FragmentMyBookBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MyBookFragment extends Fragment {

    private int id;

    private MyBookAdapter adapter;
    private RecyclerView rvMyBook;
    private ImageView imageEmpty;
    List<DataItemMyBook> dataItemMyBooks;
    private ProgressBar progressCircle;
    private HashMap<Integer, Integer> hashMapRead = new HashMap<>();
    SessionManagement sessionManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManagement = new SessionManagement(getContext());
        id = sessionManagement.getSession();
        progressCircle = view.findViewById(R.id.progressCircle);
        rvMyBook = view.findViewById(R.id.rv_mybook);
        dataItemMyBooks = new ArrayList<>();
        imageEmpty = view.findViewById(R.id.empty_wishlist);
    }

    @Override
    public void onStart() {
        super.onStart();

        dataItemMyBooks.clear();
        showMyBook(id);
    }

    private void showMyBook(int id) {
        progressCircle.setVisibility(View.VISIBLE);
        ApiService.apiCall().getMyBookList(id)
                .enqueue(new Callback<ResponseMyBook>() {
                    @Override
                    public void onResponse(Call<ResponseMyBook> call, Response<ResponseMyBook> response) {
                        if (response.isSuccessful()) {
                            progressCircle.setVisibility(View.GONE);
                            List<DataItemMyBook> a = response.body().getData();
                            if (a.isEmpty()) {
                                saveToMyRead(null);
                                rvMyBook.setVisibility(View.GONE);
                                imageEmpty.setVisibility(View.VISIBLE);
                            } else {
                                for (DataItemMyBook data : a) {
                                    dataItemMyBooks.add(data);
                                    hashMapRead.put(data.getIdPinjam(), data.getProgressBaca());
                                    saveToMyRead(hashMapRead);
                                    Log.d("PROGR_MB", String.valueOf(data.getProgressBaca()));
                                    adapter = new MyBookAdapter(dataItemMyBooks);
                                    adapter.notifyItemInserted(dataItemMyBooks.lastIndexOf(dataItemMyBooks));

                                    rvMyBook.setHasFixedSize(true);
                                    rvMyBook.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rvMyBook.setAdapter(adapter);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMyBook> call, Throwable t) {
                        progressCircle.setVisibility(View.GONE);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("MY BOOK FRAGMENT", t.getMessage());
                    }
                });
    }

    private void saveToMyRead(HashMap<Integer, Integer> hashMapRead) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMapRead);

        String TAG = "LIST_PROGRESS_" + sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }
}