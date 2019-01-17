package com.ppl.nickj.pplqr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ppl.nickj.pplqr.db.AppDatabase;
import com.ppl.nickj.pplqr.db.Product;
import com.ppl.nickj.pplqr.db.dbinit;

import java.util.List;

public class Results extends AppCompatActivity {
    private AppDatabase db;
    private Product prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String code = "null";



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



        db = AppDatabase.getInMemoryDatabase(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("code");
            prod = db.productDao().find(code);
        }

        if(prod != null){
            tv.setText(prod.title);
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
}
