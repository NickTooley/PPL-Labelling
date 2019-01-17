package com.ppl.nickj.pplqr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ppl.nickj.pplqr.db.AppDatabase;
import com.ppl.nickj.pplqr.db.Product;
import com.ppl.nickj.pplqr.db.dbinit;

import java.io.InputStream;
import java.util.List;

public class Results extends AppCompatActivity {
    private AppDatabase db;
    private Product prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String code = "No Products Found";



        TextView tv = (TextView) findViewById(R.id.code);

        tv.setText(code);

        Button getDB = (Button) findViewById(R.id.tempbtn);
        getDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getInMemoryDatabase(getApplicationContext());

                AsyncScraper scraper = new AsyncScraper(Results.this, db);
                scraper.execute();
            }
        });

//        ImageView prodView = (ImageView) findViewById(R.id.productImage);



        db = AppDatabase.getInMemoryDatabase(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("code");
            prod = db.productDao().find(code);
        }

        if(prod != null){
            tv.setText(prod.title);
            new DownloadImageTask((ImageView) findViewById(R.id.productImage)).execute("http://www.packagingproducts.co.nz"+prod.imageURL);
        }

    }

    class AsyncScraper extends AsyncTask<String, Void, List<Product>> {
        private Context context;
        private ProgressDialog dialog;
        private AppDatabase db;

        public AsyncScraper(Context context, AppDatabase db){
            this.context = context;
            dialog = new ProgressDialog(context);
            this.db = db;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Retrieving database information");
            dialog.show();
        }


        protected List<Product> doInBackground(String... search) {
            List<Product> products = dbinit.retrieveProductsJSON(getApplicationContext());
            return products;
        }

        protected void onPostExecute(List<Product> products){

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Toast.makeText(getApplicationContext(), "DB Successfully Updated", Toast.LENGTH_LONG).show();

        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
