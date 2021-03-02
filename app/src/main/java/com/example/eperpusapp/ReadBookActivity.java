package com.example.eperpusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eperpusapp.Fragment.MyBookFragment;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadBookActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private static final String TAG = ReadBookActivity.class.getSimpleName();

    Toolbar toolbar;
    PDFView pdfView;
    public static int pageNumber = -1;
    public static String EXTRA_BOOK_FILE = "EXTRA_BOOK_FILE";
    private ProgressDialog dialog;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        toolbar = findViewById(R.id.toolbar);
        pdfView = findViewById(R.id.pdfViewer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);

        String file = getIntent().getStringExtra(EXTRA_BOOK_FILE);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Toast.makeText(this, "Page Number: " + (pageNumber + 1), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /**
             * Showing Dialog
             */
            dialog.setMessage("Downloading file. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

//                int lengthOfFile = urlConnection.getContentLength();
//                int count;
//                byte data[] = new byte[1024];
//                long total = 0;
//                while ((count = inputStream.read(data)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
//                }

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

        @Override
        protected void onProgressUpdate(Void... values) {
            dialog.setProgress(Integer.parseInt(String.valueOf(values)));
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
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
            Toast.makeText(ReadBookActivity.this, page + 1 + " of " + pageCount, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageError(int page, Throwable t) {

        }
    }
}