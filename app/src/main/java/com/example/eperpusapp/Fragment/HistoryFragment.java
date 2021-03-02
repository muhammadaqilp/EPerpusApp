package com.example.eperpusapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eperpusapp.Adapter.HistoryBookAdapter;
import com.example.eperpusapp.Model.DataItemHistory;
import com.example.eperpusapp.Model.ResponseHistory;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.R;
import com.example.eperpusapp.Session.SessionManagement;

import java.util.ArrayList;
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

    }

    private void showBookHistory(int idUser) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getHistoryList(idUser)
                .enqueue(new Callback<ResponseHistory>() {
                    @Override
                    public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                        if (response.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            List<DataItemHistory> history = response.body().getData();
                            for (DataItemHistory his : history){
                                dataItemHistory.add(his);

                                adapter = new HistoryBookAdapter(dataItemHistory);
                                adapter.notifyItemInserted(dataItemHistory.lastIndexOf(dataItemHistory));

                                rvHistory.setHasFixedSize(true);
                                rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
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
}