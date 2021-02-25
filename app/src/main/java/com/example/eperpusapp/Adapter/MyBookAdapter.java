package com.example.eperpusapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemMyBook;
import com.example.eperpusapp.R;
import com.example.eperpusapp.databinding.ItemMybookBookBinding;

import java.util.List;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder>{

    private Context mContext;
    private List<DataItemMyBook> dataItems;

    public MyBookAdapter(List<DataItemMyBook> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mybook_book, parent, false);
        return new MyBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();

        final DataItemMyBook data = dataItems.get(position);

        String currentString = data.getTanggalKembali();
        String[] separated = currentString.split(" ");

        String tgl = separated[0]+" "+separated[1]+" "+separated[2]+" "+separated[3];

        holder.binding.bookTitle.setText(data.getJudulBuku());
        holder.binding.bookAuthors.setText(data.getPengarang());
        holder.binding.progressPercentage.setText(data.getProgressBaca()+"%");
        holder.binding.progressBar.setProgress(data.getProgressBaca());
        holder.binding.tglKembali.setText(tgl);

    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemMybookBookBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemMybookBookBinding.bind(itemView);

        }
    }
}
