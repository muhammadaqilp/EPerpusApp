package com.example.eperpusapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eperpusapp.R;
import com.example.eperpusapp.ReadBookActivity;

import static android.app.Activity.RESULT_OK;

public class MyBookFragment extends Fragment {

    private LinearLayout a;
    private TextView percentage;
    private ProgressBar progressBar;
    private int progress, current, total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        a = view.findViewById(R.id.linear);
        percentage = view.findViewById(R.id.progressPercentage);
        progressBar = view.findViewById(R.id.progressBar);

        a.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReadBookActivity.class);
            startActivity(intent);
        });

        refresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        current = ReadBookActivity.pageNumber+1;
        Log.d("CURRENT", String.valueOf(current));
        total = 5;
        progress = 100*current/total;
        percentage.setText(progress+"%");
        Log.d("RESULT", String.valueOf(progress));
        progressBar.setProgress(progress);
    }


}