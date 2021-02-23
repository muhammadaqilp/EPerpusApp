package com.example.eperpusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.eperpusapp.Adapter.CollectionBookAdapter;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Model.ResponseUser;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private CollectionBookAdapter adapter;
    private ActivitySearchBinding binding;
    private String title, keyword, category;
    private int code;
    private List<DataItemBuku> dataItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataItemList = new ArrayList<>();

        keyword = getIntent().getStringExtra("EXTRA_SEARCH");
        code = getIntent().getIntExtra("EXTRA_CODE", -1);
        category = getIntent().getStringExtra("EXTRA_DATA");

        if (code == 0) {
            title = "Search: " + "\"" + keyword + "\"";
            showSearchBook(keyword);
        } else if (code == 1) {
            title = category;
            showCategoryBook(category);
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.txtToolbar.setText(title);

    }

    private void showCategoryBook(String category) {
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getCollectionCategory(category)
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, Response<ResponseBuku> response) {
                        if (response.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            List<DataItemBuku> dataItemss = response.body().getData();
                            for (DataItemBuku dataItems : dataItemss) {
                                dataItemList.add(dataItems);
                                Collections.shuffle(dataItemList);
                                adapter = new CollectionBookAdapter(dataItemList);
                                adapter.notifyItemInserted(dataItemList.lastIndexOf(dataItemList));

                                binding.rvSearchCollection.setHasFixedSize(true);
                                binding.rvSearchCollection.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                                binding.rvSearchCollection.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBuku> call, Throwable t) {
                        binding.rvSearchCollection.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("HOME FRAGMENT", t.getMessage());
                    }
                });
    }

    private void showSearchBook(String keyword) {
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getCollectionSearch(keyword)
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, Response<ResponseBuku> response) {
                        if (response.isSuccessful()) {
                            ResponseBuku resp = response.body();
                            if (resp.getMessage().equals("1")) {
                                binding.progressBar.setVisibility(View.GONE);
                                List<DataItemBuku> dataItemss = response.body().getData();
                                for (DataItemBuku dataItems : dataItemss) {
                                    dataItemList.add(dataItems);
                                    Collections.shuffle(dataItemList);
                                    adapter = new CollectionBookAdapter(dataItemList);
                                    adapter.notifyItemInserted(dataItemList.lastIndexOf(dataItemList));

                                    binding.rvSearchCollection.setHasFixedSize(true);
                                    binding.rvSearchCollection.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                                    binding.rvSearchCollection.setAdapter(adapter);
                                }
                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.txtEror.setVisibility(View.VISIBLE);
                                binding.rvSearchCollection.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBuku> call, Throwable t) {
                        binding.rvSearchCollection.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("HOME FRAGMENT", t.getMessage());
                    }
                });
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