package com.example.eperpusapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.R;
import com.example.eperpusapp.SearchActivity;

import java.util.List;

public class CategoryBookAdapter extends RecyclerView.Adapter<CategoryBookAdapter.ViewHolder>{

    private Context mContext;
    private List<String> dataString;

    public CategoryBookAdapter(List<String> dataString) {
        this.dataString = dataString;
    }

    @NonNull
    @Override
    public CategoryBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_all, parent, false);
        return new CategoryBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryBookAdapter.ViewHolder holder, int position) {

        mContext = holder.itemView.getContext();

        final String category = dataString.get(position);

        holder.txtCategory.setText(category);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SearchActivity.class);
            intent.putExtra("EXTRA_CODE", 1);
            intent.putExtra("EXTRA_DATA", category);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dataString.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
}
