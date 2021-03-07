package com.example.eperpusapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eperpusapp.Adapter.HistoryBookAdapter;
import com.example.eperpusapp.Model.DataItemHistory;
import com.example.eperpusapp.Model.DataItemMyBook;
import com.example.eperpusapp.Model.ResponseHistory;
import com.example.eperpusapp.Model.ResponseMyBook;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.R;
import com.example.eperpusapp.Session.SessionManagement;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private HistoryBookAdapter adapter;
    List<DataItemHistory> dataItemHistory;
    private ProgressBar progressBar;
    private int idUser;
    SessionManagement sessionManagement;
    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManagement = new SessionManagement(getContext());
        idUser = sessionManagement.getSession();
        progressBar = view.findViewById(R.id.progressCircle);
        rvHistory = view.findViewById(R.id.rv_history);
        dataItemHistory = new ArrayList<>();
        showBookHistory(idUser);
        mybookList(idUser);
    }

    private void showBookHistory(int idUser) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getHistoryList(idUser)
                .enqueue(new Callback<ResponseHistory>() {
                    @Override
                    public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            List<DataItemHistory> history = response.body().getData();
                            for (DataItemHistory his : history) {
                                dataItemHistory.add(his);

                                adapter = new HistoryBookAdapter(dataItemHistory);
                                adapter.notifyItemInserted(dataItemHistory.lastIndexOf(dataItemHistory));

                                rvHistory.setHasFixedSize(true);
                                LinearLayoutManager lm = new LinearLayoutManager(getContext());
                                lm.setReverseLayout(true);
                                lm.setStackFromEnd(true);
                                rvHistory.setLayoutManager(lm);
                                rvHistory.setAdapter(adapter);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseHistory> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("HISTORY FRAGMENT", t.getMessage());
                    }
                });
    }

    private void mybookList(int session) {
        ApiService.apiCall().getMyBookList(session)
                .enqueue(new Callback<ResponseMyBook>() {
                    @Override
                    public void onResponse(Call<ResponseMyBook> call, Response<ResponseMyBook> response) {
                        if (response.isSuccessful()) {
                            List<DataItemMyBook> dataItemssz = response.body().getData();
                            if (dataItemssz.isEmpty()) {
                                saveToMy(null);
                            } else {
                                for (DataItemMyBook dataItems : dataItemssz) {
                                    hashMap.put(dataItems.getIdBuku(), dataItems.getIdPinjam());
                                }
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

        String TAG = "LIST_BORROW_" + sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }
}