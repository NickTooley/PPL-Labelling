package com.ppl.nickj.pplqr.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class dbinit {

    // Simulate a blocking operation delaying each Food insertion with a delay:
    private static final int DELAY_MILLIS = 500;


    public static void populateAsync(final AppDatabase db, List<Product> products){

        PopulateDbAsyncList task = new PopulateDbAsyncList(db, products);
        task.execute();

    }

    private static class PopulateDbAsyncList extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final List<Product> products;

        PopulateDbAsyncList(AppDatabase db, List<Product> products) {
            mDb = db;
            this.products = products;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithList(mDb, products);
            return null;
        }

    }

    private static void populateWithList(AppDatabase db, List<Product> products){

        for(Product prod: products){
            long test = db.productDao().insertProduct(prod);
            Log.d("new ID", String.valueOf(test));

        }

    }

    public static List<Product> retrieveProductsJSON(Context context){
        String URL = "http://kate.ict.op.ac.nz/~toolnj1/PPL/retrieveJson.php?date=";
        final List<Product> allProducts = new ArrayList<Product>();
        final Context contxt = context;


        try{

            JsonArrayRequest jsonFoodInsert = new JsonArrayRequest
                    (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {


                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++){

                                try {
                                    JSONObject newObj = response.getJSONObject(i);
                                    if(i == response.length() - 1){
//                                        String nuDate = newObj.getString("date");
//                                        Log.d("New Sync", nuDate);
//                                        editor.putString("syncDate", nuDate);
//                                        editor.apply();
                                    }else {
                                        Product item = new Product();

                                        item.code = newObj.getString("code");
                                        item.imageURL = newObj.getString("imageURL");
                                        item.title = newObj.getString("title");

                                        Log.d("new item", item.title);

                                        allProducts.add(item);
                                    }



                                }catch(JSONException e){
                                    Log.d("JSONERR", e.getMessage());
                                }
                            }

                            AppDatabase db = AppDatabase.getInMemoryDatabase(contxt);
                            dbinit.populateAsync(db, allProducts);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("err", error.networkResponse.toString());
                        }

                    });
            Volley.newRequestQueue(context).add(jsonFoodInsert);



        }catch (Exception e){
            //Log.d("error", e.getMessage());
            return allProducts;
        }



        return allProducts;
    }


}
