package com.aryotech.merchant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aryotech.merchant.Model.Product;
import com.bumptech.glide.Glide;

public class DetailProduct extends AppCompatActivity {

    private TextView tvNmProd, tvNmMerch, tvQty, tvNmCat, tvPrice;
    private ImageView imgDetail;
    private Button btnEdit, btnDelete;
    public static String detailprod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);


        Product product = getIntent().getParcelableExtra(detailprod);
        inisialisasi();

        tvNmProd.setText("Name Product      :   " + product.getProductNama());
        tvQty.setText("Qty Product          :   " + String.valueOf(product.getProductQty()));
        tvNmCat.setText("Name Category      :   " + product.getCategory().getCategoryName());
        tvPrice.setText("Rp " + String.valueOf(product.getPrice()));

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl + product.getProductImage();
        Glide.with(this).load(url).into(imgDetail);
    }

    public void inisialisasi() {
        imgDetail = findViewById(R.id.img_detail_prod);
        tvNmProd = findViewById(R.id.tv_detail_prod);
        tvQty = findViewById(R.id.tv_detail_qty);
        tvNmMerch = findViewById(R.id.tv_detail_desc);
        tvNmCat = findViewById(R.id.tv_detail_cname);
        tvPrice = findViewById(R.id.tv_detail_price);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
    }
}
