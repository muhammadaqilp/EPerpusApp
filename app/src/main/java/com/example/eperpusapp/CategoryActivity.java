package com.example.eperpusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eperpusapp.Adapter.CategoryBookAdapter;
import com.example.eperpusapp.Adapter.CollectionBookAdapter;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemCategory;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Model.ResponseCategory;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.databinding.ActivityCategoryBinding;
import com.example.eperpusapp.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private CategoryBookAdapter adapter;
    private List<DataItemCategory> dataItemList;
    private List<String> dataString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataItemList = new ArrayList<>();
        dataString = new ArrayList<>();

        showAllCategory();
    }

    private void showAllCategory() {

        ApiService.apiCall().getCategoryList()
                .enqueue(new Callback<ResponseCategory>() {
                    @Override
                    public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {
                        if (response.isSuccessful()){
                            List<String> c = response.body().getData();
                            for (String a : c){
                                dataString.add(a);
                                Log.d("CATEGORY", a);
                            }
                            Collections.sort(dataString, new Comparator<String>() {
                                @Override
                                public int compare(String o1, String o2) {
                                    return o1.compareTo(o2);
                                }
                            });
                            adapter = new CategoryBookAdapter(dataString);
                            adapter.notifyItemInserted(dataString.lastIndexOf(dataString));

                            binding.rvCategory.setHasFixedSize(true);
                            binding.rvCategory.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
                            binding.rvCategory.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseCategory> call, Throwable t) {

                    }
                });
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