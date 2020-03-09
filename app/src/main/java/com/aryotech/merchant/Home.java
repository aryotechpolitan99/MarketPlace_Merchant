package com.aryotech.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aryotech.merchant.Adapter.AdapterMain;
import com.aryotech.merchant.Model.AddProduct;
import com.aryotech.merchant.Model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RequestQueue requestQueue;
    AdapterMain adapterMain = new AdapterMain(this);
    RecyclerView recyclerView;
    FloatingActionButton btnfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_list_product);
        recyclerView.setAdapter(adapterMain);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://210.210.154.65:4444/api/products";


        //tambah button FAB add Product
        btnfab = findViewById(R.id.fab_add);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFabAdd();
            }
        });


        JsonObjectRequest listProductReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    ArrayList<Product> products = new ArrayList<>();
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(data.getJSONObject(i).toString(), Product.class);
                        products.add(product);
                        adapterMain.addData(products);
                        adapterMain.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.getMessage());
                    }
                });

        requestQueue.add(listProductReq);
    }

    private void onClickFabAdd(){
        // intent Activity AddProduct
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
    }
}
