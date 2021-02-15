package com.example.eperpusapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eperpusapp.Adapter.CollectionBookAdapter;
import com.example.eperpusapp.BookDetailActivity;
import com.example.eperpusapp.Model.DataItem;
import com.example.eperpusapp.Model.Response;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.ProfileActivity;
import com.example.eperpusapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private CollectionBookAdapter adapter;
    ImageView imageViewProfile, imageViewBook;
    List<DataItem> dataItemList;
    RecyclerView rvCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCollection = view.findViewById(R.id.rv_collection);
        imageViewProfile = view.findViewById(R.id.profile_image);
        imageViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        rvCollection.setNestedScrollingEnabled(false);
        dataItemList = new ArrayList<>();

        showBookList();
    }

    private void showBookList() {
        ApiService.apiCall().getCollectionList()
                .enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (response.isSuccessful()) {
                            List<DataItem> dataItemss = response.body().getData();
                            for (DataItem dataItems : dataItemss) {
                                dataItemList.add(dataItems);

                                adapter = new CollectionBookAdapter(dataItemList);
                                adapter.notifyItemInserted(dataItemList.lastIndexOf(dataItemList));

                                rvCollection.setHasFixedSize(true);
                                rvCollection.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                rvCollection.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("HOME FRAGMENT", t.getMessage());
                    }
                });
    }
}