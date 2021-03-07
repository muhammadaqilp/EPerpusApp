package com.example.eperpusapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eperpusapp.Fragment.MyBookFragment;
import com.example.eperpusapp.Model.ResponseReturn;
import com.example.eperpusapp.Network.ApiService;
import com.example.eperpusapp.Session.SessionManagement;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadBookActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private static final String TAG = ReadBookActivity.class.getSimpleName();

    Toolbar toolbar;
    PDFView pdfView;
    private int pageNumber;
    public static String EXTRA_BOOK_FILE = "EXTRA_BOOK_FILE";
    public static String EXTRA_BOOK_IDPINJAM = "EXTRA_BOOK_IDPINJAM";
    public static String EXTRA_BOOK_PAGE = "EXTRA_BOOK_PAGE";
    private ProgressDialog dialog;
    private int idpinjam;
    SessionManagement sessionManagement;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        toolbar = findViewById(R.id.toolbar);
        pdfView = findViewById(R.id.pdfViewer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManagement = new SessionManagement(this);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);

        String file = getIntent().getStringExtra(EXTRA_BOOK_FILE);
        idpinjam = getIntent().getIntExtra(EXTRA_BOOK_IDPINJAM, 0);
        pageNumber = getIntent().getIntExtra(EXTRA_BOOK_PAGE, 0);
        Log.d("FILE", file);


        new RetrievePDFfromUrl().execute(file);

//        pdfView.fromAsset("SKUIN.pdf")
//                .defaultPage(pageNumber)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .load();
//        displayFromAsset("SKUIN.pdf");
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        if (page > pageNumber) {
            pageNumber = page;
            setTitle(String.format("%s %s / %s", pdfView, page + 1, pageCount));
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

//    private void afterViews() {
//        pdfView.setBackgroundColor(Color.LTGRAY);
//        if (uri != null) {
//            displayFromUri(uri);
//        } else {
//            displayFromAsset(SAMPLE_FILE);
//        }
//        setTitle("SKUIN.pdf");
//    }

    private void displayFromAsset(String assetFileName) {
//        pdfFileName = assetFileName;

        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .swipeHorizontal(true)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    private void displayFromUri(Uri uri) {
//        pdfView = getFileName(uri);

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    public void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            uri = intent.getData();
            displayFromUri(uri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                updateProgress();
                updateProgressLocal();
                finish();
//                Toast.makeText(this, "Page Number: " + (pageNumber + 1), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    class RetrievePDFfromUrl extends AsyncTask<String, Integer, InputStream> implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /**
             * Showing Dialog
             */
            dialog.setMessage("Downloading file. Please wait...");
            dialog.show();
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                inputStream = new BufferedInputStream(url.openStream());

                int lengthOfFile = urlConnection.getContentLength();
                int count;
                byte[] data = new byte[16384];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    if (isCancelled()) {
                        inputStream.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    if (lengthOfFile > 0) {
                        publishProgress((int) ((total * 100) / lengthOfFile));
                    }
                }

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("READ BOOK", e.getMessage());
                return null;
            }
            return inputStream;
        }

//        @Override
//        protected void onProgressUpdate(Void... values) {
//            dialog.setProgress(Integer.parseInt(String.valueOf(values)));
//        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            dialog.dismiss();
            pdfView.fromStream(inputStream)
                    .defaultPage(pageNumber)
                    .swipeHorizontal(true)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .scrollHandle(new DefaultScrollHandle(ReadBookActivity.this))
                    .spacing(10) // in dp
                    .onPageError(this)
                    .pageSnap(true)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .load();
        }

        @Override
        public void loadComplete(int nbPages) {

        }

        @Override
        public void onPageChanged(int page, int pageCount) {
            if (page > pageNumber) {
                pageNumber = page;
                setTitle(String.format("%s %s / %s", pdfView, page + 1, pageCount));
            }
//            Toast.makeText(ReadBookActivity.this, page + 1 + " of " + pageCount, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageError(int page, Throwable t) {

        }
    }

    private void updateProgress() {
        int page = pageNumber+1;
        ApiService.apiCall().getResponseUpdateBaca(idpinjam, page)
                .enqueue(new Callback<ResponseReturn>() {
                    @Override
                    public void onResponse(Call<ResponseReturn> call, Response<ResponseReturn> response) {
                        if (response.isSuccessful()) {
                            ResponseReturn resp = response.body();
                            String msg = resp.getMessage();
                            if (msg.equals("1")) {
                                Toast.makeText(ReadBookActivity.this, "Last Page Read: "+page, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReadBookActivity.this, "Failed to Borrow Book", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReturn> call, Throwable t) {
                        Toast.makeText(ReadBookActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateProgressLocal() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        HashMap<Integer, Integer> hashMapRead = new HashMap<>();
        hashMapRead.put(idpinjam, pageNumber);
        String json = gson.toJson(hashMapRead);

        String TAG = "LIST_PROGRESS_"+sessionManagement.getSession();

        edit.putString(TAG, json);
        edit.commit();
    }
}