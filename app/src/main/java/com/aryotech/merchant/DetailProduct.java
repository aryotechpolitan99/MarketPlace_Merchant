package com.aryotech.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aryotech.merchant.Model.AccessToken;
import com.aryotech.merchant.Model.Product;
import com.aryotech.merchant.Model.Update;
import com.aryotech.merchant.Utils.TokenManager;
import com.bumptech.glide.Glide;

import java.util.Hashtable;
import java.util.Map;

public class DetailProduct extends AppCompatActivity {

    private TextView tvNmProd, tvQty, tvNmCat, tvPrice, tvDesc;
    private ImageView imgDetail;
    private Button btnUpdate, btnDelete;
    public static String detailprod = "";
    private RequestQueue requestQueue;

    AccessToken accessToken;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        accessToken = TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).getToken();
        requestQueue = Volley.newRequestQueue(this);
        Product product = getIntent().getParcelableExtra(detailprod);
        inisialisasi();

        id = product.getProductId();

        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProduct.this, Update.class);
                intent.putExtra("update", product);
                startActivity(intent);

            }
        });

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                        "http://210.210.154.65:4444/api/merchant/product/" + id + "/delete", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DetailProduct.this, "Success Deleted", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailProduct.this,"gagal delete", Toast.LENGTH_SHORT).show();
                    }
                })
                {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers = new Hashtable<>();

                        headers.put("Accept","application/json");
                        headers.put("Content-Type","application/x-www-form-urlencoded");
                        // Autirization bearer token
                        headers.put("Authorization",accessToken.getTokenType()+" "+accessToken.getAccessToken());
                        return headers;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

        tvNmProd.setText("Name Product      :   " + product.getProductNama());
        tvQty.setText("Qty Product          :   " + String.valueOf(product.getProductQty()));
        tvNmCat.setText("Name Category      :   " + product.getCategory().getCategoryName());
        tvPrice.setText("Rp " + String.valueOf(product.getPrice()));
        tvDesc.setText(product.getProductDesc());

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl + product.getProductImage();
        Glide.with(this).load(url).into(imgDetail);
    }

    public void inisialisasi() {
        imgDetail = findViewById(R.id.img_detail_prod);
        tvNmProd = findViewById(R.id.tv_detail_prod);
        tvQty = findViewById(R.id.tv_detail_qty);
        tvDesc = findViewById(R.id.tv_detail_desc);
        tvNmCat = findViewById(R.id.tv_detail_cname);
        tvPrice = findViewById(R.id.tv_detail_price);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
    }
}
