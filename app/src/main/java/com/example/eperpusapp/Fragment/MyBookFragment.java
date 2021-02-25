package com.example.eperpusapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MyBookFragment extends Fragment {

    private LinearLayout a;
    private int progress, current, total, id;
    private FragmentMyBookBinding binding;

    private MyBookAdapter adapter;
    private RecyclerView rvMyBook;
    List<DataItemMyBook> dataItemMyBooks;
    private ProgressBar progressCircle;
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

//        a = view.findViewById(R.id.linear);
//        percentage = view.findViewById(R.id.progressPercentage);
//        progressBar = view.findViewById(R.id.progressBar);

//        a.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), ReadBookActivity.class);
//            startActivity(intent);
//        });

//        refresh();
        sessionManagement = new SessionManagement(getContext());
        id = sessionManagement.getSession();
        progressCircle = view.findViewById(R.id.progressCircle);
        rvMyBook = view.findViewById(R.id.rv_mybook);
        dataItemMyBooks = new ArrayList<>();
        showMyBook(id);

    }

    private void showMyBook(int id) {
        progressCircle.setVisibility(View.VISIBLE);
        ApiService.apiCall().getMyBookList(id)
                .enqueue(new Callback<ResponseMyBook>() {
                    @Override
                    public void onResponse(Call<ResponseMyBook> call, Response<ResponseMyBook> response) {
                        if (response.isSuccessful()){
                            progressCircle.setVisibility(View.GONE);
                            List<DataItemMyBook> a = response.body().getData();
                            for (DataItemMyBook data : a){
                                dataItemMyBooks.add(data);
                                adapter = new MyBookAdapter(dataItemMyBooks);
                                adapter.notifyItemInserted(dataItemMyBooks.lastIndexOf(dataItemMyBooks));

                                rvMyBook.setHasFixedSize(true);
                                rvMyBook.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvMyBook.setAdapter(adapter);
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

    @Override
    public void onResume() {
        super.onResume();
//        refresh();
//        dataItemMyBooks.clear();
//        showMyBook(id);
    }

//    private void refresh() {
//        current = ReadBookActivity.pageNumber + 1;
//        Log.d("CURRENT", String.valueOf(current));
//        total = 5;
//        progress = 100 * current / total;
//        percentage.setText(progress + "%");
//        Log.d("RESULT", String.valueOf(progress));
//        progressBar.setProgress(progress);
//    }


}