package com.example.eperpusapp.Layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.eperpusapp.R;

public class DialogNoInternet extends Dialog implements View.OnClickListener {

    private Context context;
    private Button btnDismiss;

    public DialogNoInternet(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_no_internet);

        btnDismiss = findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDismiss:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }
}