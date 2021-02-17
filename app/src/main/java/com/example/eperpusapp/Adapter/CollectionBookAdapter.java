package com.example.eperpusapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eperpusapp.BookDetailActivity;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.R;

import java.io.File;
import java.util.List;

public class CollectionBookAdapter extends RecyclerView.Adapter<CollectionBookAdapter.ViewHolder> {

    private Context mContext;
    private List<DataItemBuku> dataItems;

    public CollectionBookAdapter(List<DataItemBuku> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public CollectionBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionBookAdapter.ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();

        final DataItemBuku dataItem = dataItems.get(position);

        File imgFile = new File("D:/Pycharm Project/formInputEperpus/Cover/cover.PNG");
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.imageView.setImageBitmap(bitmap);
        }

        holder.tvBookTitle.setText(dataItem.getJudulBuku());
        holder.tvBookAuthors.setText(dataItem.getPengarang());

        int a = dataItem.getJumlahCopy();
        int b = dataItem.getTotalDipinjam();
        holder.tvAvailability.setText("Available "+(a-b)+" / "+a);

        holder.itemView.setOnClickListener(v -> {
            Intent intentDetail = new Intent(mContext, BookDetailActivity.class);
            intentDetail.putExtra(BookDetailActivity.EXTRA_BOOK_DETAIL, dataItem);
            mContext.startActivity(intentDetail);
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView tvBookTitle;
        public TextView tvBookAuthors;
        public TextView tvAvailability;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.img_item_book);
            tvBookTitle = itemView.findViewById(R.id.bookTitle);
            tvBookAuthors = itemView.findViewById(R.id.bookAuthors);
            tvAvailability = itemView.findViewById(R.id.bookAvalability);
        }
    }
}
