package com.example.eperpusapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eperpusapp.BookDetailActivity;
import com.example.eperpusapp.LoginActivity;
import com.example.eperpusapp.Model.DataItemBuku;
import com.example.eperpusapp.Model.DataItemMyBook;
import com.example.eperpusapp.Model.ResponseBuku;
import com.example.eperpusapp.Model.ResponseReturn;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.R;
import com.example.eperpusapp.ReadBookActivity;
import com.example.eperpusapp.Session.SessionManagement;
import com.example.eperpusapp.databinding.ItemMybookBookBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder>{

    private Context mContext;
    private List<DataItemMyBook> dataItems;
    private ProgressDialog dialog;
    SessionManagement sessionManagement;

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

        Glide.with(mContext)
                .load(data.getFotoBuku())
                .into(holder.binding.bookImage);
        holder.binding.bookTitle.setText(data.getJudulBuku());
        holder.binding.bookAuthors.setText(data.getPengarang());
        holder.binding.progressPercentage.setText(data.getProgressBaca()+"%");
        holder.binding.progressBar.setProgress(data.getProgressBaca());
        holder.binding.tglKembali.setText(tgl);

        holder.binding.info.setOnClickListener(v -> sendToDetail(data.getIdBuku()));

        holder.binding.bookImage.setOnClickListener(v -> readBook(data.getFileBuku()));

        holder.binding.btnReturn.setOnClickListener(v -> {
            dialog = new ProgressDialog(mContext);
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            returnBook(data.getIdBuku(), data.getIdPinjam(), ts, position);
        });

    }

    private void returnBook(int idBuku, int idPinjam, String timestamp, int position) {
        sessionManagement = new SessionManagement(mContext);
        int idUser = sessionManagement.getSession();

        dialog.setMessage("Return Book...");
        dialog.show();

        ApiService.apiCall().getResponseReturn(idUser, idBuku, idPinjam, timestamp)
                .enqueue(new Callback<ResponseReturn>() {
                    @Override
                    public void onResponse(Call<ResponseReturn> call, Response<ResponseReturn> response) {
                        if (response.isSuccessful()){
                            ResponseReturn resp = response.body();
                            String msg = resp.getMessage();
                            if(msg.equals("1")){
                                dialog.dismiss();
                                Toast.makeText(mContext, "Success to Return Book", Toast.LENGTH_SHORT).show();
                                removeAt(position);
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(mContext, "Failed to Return Book", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReturn> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readBook(String file) {
        Intent intent = new Intent(mContext, ReadBookActivity.class);
        intent.putExtra(ReadBookActivity.EXTRA_BOOK_FILE, file);
        mContext.startActivity(intent);
    }

    private void sendToDetail(int id) {
        ApiService.apiCall().getCollectionDetail(id)
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

    public void removeAt(int position) {
        dataItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataItems.size());
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
