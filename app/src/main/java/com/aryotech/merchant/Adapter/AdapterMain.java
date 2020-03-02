package com.aryotech.merchant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryotech.merchant.DetailProduct;
import com.aryotech.merchant.Model.Product;
import com.aryotech.merchant.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.aryotech.merchant.DetailProduct.detailprod;
public class AdapterMain extends RecyclerView.Adapter<AdapterMain.MyViewHolder> {

    private Context context;
    private ArrayList<Product> products = new ArrayList<>();

    public AdapterMain(Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMain.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMain.MyViewHolder holder, final int position) {


        holder.tvName.setText(products.get(position).getProductNama());
        holder.tvMerch.setText(products.get(position).getMerchant().getMerchantName());
        holder.tvPriceList.setText("Rp " + products.get(position).getPrice());

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl + products.get(position).getProductImage();
        Glide.with(context)
                .load(url)
                .into(holder.ivProdImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listproduct = new Intent(context, DetailProduct.class);
                listproduct.putExtra(detailprod, products.get(position));
                context.startActivity(listproduct);
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProdImage;
        private TextView tvName, tvMerch, tvPriceList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_nm_prod);
            tvMerch = itemView.findViewById(R.id.tv2_merch);
            ivProdImage = itemView.findViewById(R.id.img_list);
            tvPriceList = itemView.findViewById(R.id.tv3_price);
        }
    }

    public void addData(ArrayList<Product> dataProd) {
        this.products = dataProd;
        notifyDataSetChanged();
    }
}
