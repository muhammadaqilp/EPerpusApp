package com.example.eperpusapp.Fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.example.eperpusapp.Adapter.MyBookAdapter;
import com.example.eperpusapp.Adapter.WishlistBookAdapter;
import com.example.eperpusapp.CategoryActivity;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemMyBook;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Model.ResponseMyBook;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.ProfileActivity;
import com.example.eperpusapp.R;
import com.example.eperpusapp.SearchActivity;
import com.example.eperpusapp.Session.SessionManagement;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private CollectionBookAdapter adapter;
    private ProgressBar progressBar, progressBarPage;
    private GridLayoutManager layoutManager;
    private NestedScrollView nestedScrollView;
    private ScrollView scrollView;
    ImageView imageViewProfile, imageBanner;
    List<DataItemBuku> dataItemList;
    RecyclerView rvCollection;
    TextView seeAll, name1;
    TextView cat1, cat2, cat3, cat4, cat5, cat6;
    public List<Integer> idbook = new ArrayList<>();
    public List<Integer> idmybook = new ArrayList<>();
    SessionManagement sessionManagement;

    HashMap<Integer, Integer> hashMap = new HashMap<>();
    HashMap<Integer, Integer> hashMapRead = new HashMap<>();

    public static final String EXTRA_USER_HOME = "extra_user_home";
    SearchView searchView;

    //variables for pagination
    private int page_number = 1;
//    private int item_count = 10;

    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total = 0;
    private int view_threshold = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCollection = view.findViewById(R.id.rv_collection);
        imageViewProfile = view.findViewById(R.id.profile_image);
        imageBanner = view.findViewById(R.id.img_banner);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarPage = view.findViewById(R.id.progressBarPage);
        nestedScrollView = view.findViewById(R.id.nested);
//        scrollView = view.findViewById(R.id.scroll);
        seeAll = view.findViewById(R.id.seeAll);
        name1 = view.findViewById(R.id.qw1);
        cat1 = view.findViewById(R.id.c1);
        cat2 = view.findViewById(R.id.c2);
        cat3 = view.findViewById(R.id.c3);
        cat4 = view.findViewById(R.id.c4);
        cat5 = view.findViewById(R.id.c5);
        cat6 = view.findViewById(R.id.c6);
        searchView = view.findViewById(R.id.searchView);
        imageViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        sessionManagement = new SessionManagement(getContext());
        layoutManager = new GridLayoutManager(getContext(), 2);

        Glide.with(this)
                .load("http://tulis.uinjkt.ac.id/img/slide/slide1.png")
                .into(imageBanner);

        String photo = sessionManagement.getSessionPhoto();

        Glide.with(this)
                .load(photo)
                .into(imageViewProfile);

        LinearLayout s = view.findViewById(R.id.linear);
        s.setOnTouchListener((v, event) -> {
            hideSoftKeyboard(getActivity());
            return false;
        });

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("EXTRA_CODE", 0);
                intent.putExtra("EXTRA_SEARCH", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        seeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        cat1.setOnClickListener(this);
        cat2.setOnClickListener(this);
        cat3.setOnClickListener(this);
        cat4.setOnClickListener(this);
        cat5.setOnClickListener(this);
        cat6.setOnClickListener(this);

        rvCollection.setNestedScrollingEnabled(false);
        dataItemList = new ArrayList<>();

        showBookList(page_number);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                page_number++;
                progressBarPage.setVisibility(View.VISIBLE);
                showBookList(page_number);
            }
        });

//
//        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//
//            if (scrollY > oldScrollY) {
//                Log.i("AAAA", "Scroll DOWN");
//            }
//            if (scrollY < oldScrollY) {
//                Log.i("AAAA", "Scroll UP");
//            }
//
//            if (scrollY == 0) {
//                Log.i("AAAA", "TOP SCROLL");
//            }
//
//            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
//                Log.i("AAAA", "BOTTOM SCROLL");
//                // here where the trick is going
//                if (isLoading){
//                    page_number++;
//                    progressBarPage.setVisibility(View.VISIBLE);
//                    showBookList(page_number);
//                    // calling from adapter addToExistingList(list)
//                    // with the defined Adapter instance
//
//                    // reset the boolean(loading) to prevent
//                    // auto loading data from APi
//                    isLoading = false;
//                }
//            }
//        });
//        rvCollection.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                visibleItemCount = layoutManager.getChildCount();
//                totalItemCount = layoutManager.getItemCount();
//                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//
//                Log.d("AAAAA", String.valueOf(visibleItemCount));
//                Log.d("AAAAA", String.valueOf(totalItemCount));
//                Log.d("AAAAA", String.valueOf(pastVisibleItems));
//                Log.d("AAAAA", String.valueOf(dy));
//
//                if (dy > 0){
//                    if (isLoading){
//                        if (totalItemCount > previous_total){
//                            isLoading = false;
//                            previous_total = totalItemCount;
//                        }
//                    }
//
//                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + view_threshold)){
//                        page_number++;
//                        showBookList(page_number);
//                        isLoading = true;
//                    }
//                }
//            }
//        });
//        showBookList();
        readingList(sessionManagement.getSession());
        mybookList(sessionManagement.getSession());
    }

    private void showBookList(int page) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.apiCall().getCollectionList(page)
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, retrofit2.Response<ResponseBuku> response) {
                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            progressBarPage.setVisibility(View.GONE);
                            List<DataItemBuku> dataItemss = response.body().getData();
                            for (DataItemBuku dataItems : dataItemss) {
                                dataItemList.add(dataItems);
//                                Collections.shuffle(dataItemList);
                                adapter = new CollectionBookAdapter(dataItemList);
//                                adapter.notifyItemInserted(dataItemList.lastIndexOf(dataItemList));

                                rvCollection.setHasFixedSize(true);
                                rvCollection.setLayoutManager(layoutManager);
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

//    public void hideKeyboard(View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public static void hideSoftKeyboard(Activity activity) {
        View a = activity.getCurrentFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (a != null) {
            inputMethodManager.hideSoftInputFromWindow(a.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.c1:
                String str = cat1.getText().toString();
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("EXTRA_CODE", 1);
                intent.putExtra("EXTRA_DATA", str);
                startActivity(intent);
                break;

            case R.id.c2:
                String str1 = cat2.getText().toString();
                Intent intent1 = new Intent(getContext(), SearchActivity.class);
                intent1.putExtra("EXTRA_CODE", 1);
                intent1.putExtra("EXTRA_DATA", str1);
                startActivity(intent1);
                break;

            case R.id.c3:
                String str2 = cat3.getText().toString();
                Intent intent2 = new Intent(getContext(), SearchActivity.class);
                intent2.putExtra("EXTRA_CODE", 1);
                intent2.putExtra("EXTRA_DATA", str2);
                startActivity(intent2);
                break;

            case R.id.c4:
                String str3 = cat4.getText().toString();
                Intent intent3 = new Intent(getContext(), SearchActivity.class);
                intent3.putExtra("EXTRA_CODE", 1);
                intent3.putExtra("EXTRA_DATA", str3);
                startActivity(intent3);
                break;

            case R.id.c5:
                String str4 = cat5.getText().toString();
                Intent intent4 = new Intent(getContext(), SearchActivity.class);
                intent4.putExtra("EXTRA_CODE", 1);
                intent4.putExtra("EXTRA_DATA", str4);
                startActivity(intent4);
                break;

            case R.id.c6:
                String str5 = cat6.getText().toString();
                Intent intent5 = new Intent(getContext(), SearchActivity.class);
                intent5.putExtra("EXTRA_CODE", 1);
                intent5.putExtra("EXTRA_DATA", str5);
                startActivity(intent5);
                break;

            default:
                break;
        }
    }

    private void readingList(int session) {
        ApiService.apiCall().getWishlist(session)
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, Response<ResponseBuku> response) {
                        if (response.isSuccessful()) {
                            List<DataItemBuku> dataItemssz = response.body().getData();
                            if (dataItemssz.isEmpty()) {
                                saveTo(null);
                            } else {
                                for (DataItemBuku dataItems : dataItemssz) {
                                    idbook.add(dataItems.getIdbuku());
                                }
                                Log.d("LIST", String.valueOf(idbook));
                                saveTo(idbook);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBuku> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("PROFILE ACTIVITY", t.getMessage());
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
                                    hashMapRead.put(dataItems.getIdPinjam(), dataItems.getProgressBaca());
                                }
//                                Log.d("LIST", String.valueOf(idmybook));
                                saveToMy(hashMap);
                                saveToMyRead(hashMapRead);
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

    private void saveTo(List<Integer> a) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(a);

        String TAG = "LIST_ID_"+sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }

    private void saveToMy(HashMap<Integer, Integer> hashMap) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMap);

        String TAG = "LIST_BORROW_"+sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }


    private void saveToMyRead(HashMap<Integer, Integer> hashMapRead) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMapRead);

        String TAG = "LIST_PROGRESS_"+sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }
}