package com.example.eperpusapp.Layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.eperpusapp.R;

public class DialogConfirmation extends Dialog implements View.OnClickListener{

    private Context context;
    private Button btnYes, btnNo;

    public DialogConfirmation(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog1);

        btnNo = findViewById(R.id.btnNo);
        btnYes = findViewById(R.id.btnYes);

        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnYes:
                Toast.makeText(context, "YES CLICKED", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnNo:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }
}