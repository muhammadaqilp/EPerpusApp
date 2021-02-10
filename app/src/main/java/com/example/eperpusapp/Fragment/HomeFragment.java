package com.example.eperpusapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eperpusapp.BookDetailActivity;
import com.example.eperpusapp.ProfileActivity;
import com.example.eperpusapp.R;

public class HomeFragment extends Fragment {

    ImageView imageViewProfile, imageViewBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewProfile = view.findViewById(R.id.profile_image);
        imageViewBook = view.findViewById(R.id.img_item_book);
        imageViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        imageViewBook.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BookDetailActivity.class);
            startActivity(intent);
        });
        
        showBookList();
    }

    private void showBookList() {

    }
}