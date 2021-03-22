package com.example.eperpusapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eperpusapp.BookDetailActivity;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemHistory;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.R;
import com.example.eperpusapp.databinding.ItemHistoryBookBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryBookAdapter extends RecyclerView.Adapter<HistoryBookAdapter.ViewHolder>{

    private Context mContext;
    private List<DataItemHistory> dataList;

    public HistoryBookAdapter(List<DataItemHistory> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public HistoryBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_book, parent, false);
        return new HistoryBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBookAdapter.ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();

        final DataItemHistory data = dataList.get(position);

        holder.binding.bookTitle.setMaxLines(1);
        holder.binding.bookTitle.setText(data.getJudulBuku());
        holder.binding.bookAuthors.setMaxLines(1);
        holder.binding.bookAuthors.setText(data.getPengarang());
        holder.binding.progressPercentage.setText(data.getProgressBaca()+"%");

        int jumlahHalaman = data.getJumlahHalaman();
        int halaman = data.getProgressBaca();
        int progress = 100*halaman/jumlahHalaman;

        holder.binding.progressPercentage.setText(progress+"%");
        holder.binding.progressBar.setProgress(progress);

        Glide.with(mContext)
                .load(data.getFotoBuku())
                .into(holder.binding.imgBuku);

        String currentString = data.getTanggalPinjam();
        String[] separated = currentString.split(" ");
        String tglPinjam = separated[1]+" "+separated[2]+" "+separated[3];

        String currentString1 = data.getTanggalDikembalikan();
        String[] separated1 = currentString1.split(" ");
        String tglBalik = separated1[1]+" "+separated1[2]+" "+separated1[3];

        holder.binding.tglPinjam.setText(tglPinjam);
        holder.binding.tglKembali.setText(tglBalik);

        holder.binding.reBorrow.setOnClickListener(v -> {
            sendToDetail(data.getIdbuku());
        });

    }

    private void sendToDetail(int idbuku) {
        ApiService.apiCall().getCollectionDetail(idbuku)
                .enqueue(new Callback<ResponseBuku>() {
                    @Override
                    public void onResponse(Call<ResponseBuku> call, Response<ResponseBuku> response) {
                        if (response.isSuccessful()){
                            List<DataItemBuku> dataItemss = response.body().getData();
                            final DataItemBuku dataItem = dataItemss.get(0);
                            Intent intent = new Intent(mContext, BookDetailActivity.class);
                            intent.putExtra(BookDetailActivity.EXTRA_BOOK_DETAIL, dataItem);
                            mContext.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBuku> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DETAIL", t.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemHistoryBookBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemHistoryBookBinding.bind(itemView);
        }
    }
}
