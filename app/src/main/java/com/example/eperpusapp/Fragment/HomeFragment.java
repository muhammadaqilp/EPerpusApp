package com.example.eperpusapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eperpusapp.Adapter.CollectionBookAdapter;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.ProfileActivity;
import com.example.eperpusapp.R;
import com.example.eperpusapp.Session.SessionManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private CollectionBookAdapter adapter;
    private ProgressBar progressBar;
    ImageView imageViewProfile, imageBanner;
    List<DataItemBuku> dataItemList;
    RecyclerView rvCollection;
    TextView cat1, name1;

    public static final String EXTRA_USER_HOME = "extra_user_home";
    SearchView searchView;

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
        imageBanner = view.findViewById(R.id.img_banner);
        progressBar = view.findViewById(R.id.progressBar);
        cat1 = view.findViewById(R.id.categ1);
        name1 = view.findViewById(R.id.qw1);
        searchView = view.findViewById(R.id.searchView);
        imageViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        Glide.with(this)
                .load("http://tulis.uinjkt.ac.id/img/slide/slide1.png")
                .into(imageBanner);

        LinearLayout s = view.findViewById(R.id.linear);
        s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(getActivity());
                return false;
            }
        });

        s.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideSoftKeyboard(getActivity());
                }
            }
        });

        rvCollection.setNestedScrollingEnabled(false);
        dataItemList = new ArrayList<>();

        showBookList();
    }

    private void showBookList() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getCollectionList()
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, retrofit2.Response<ResponseBuku> response) {
                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            List<DataItemBuku> dataItemss = response.body().getData();
                            for (DataItemBuku dataItems : dataItemss) {
                                dataItemList.add(dataItems);
                                Collections.shuffle(dataItemList);
                                adapter = new CollectionBookAdapter(dataItemList);
                                adapter.notifyItemInserted(dataItemList.lastIndexOf(dataItemList));

                                rvCollection.setHasFixedSize(true);
                                rvCollection.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                rvCollection.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBuku> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("HOME FRAGMENT", t.getMessage());
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        View a = activity.getCurrentFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (a != null) {
            inputMethodManager.hideSoftInputFromWindow(a.getWindowToken(), 0);
        }
    }


}