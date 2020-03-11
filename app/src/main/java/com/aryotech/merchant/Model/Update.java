package com.aryotech.merchant.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aryotech.merchant.Adapter.CategoriesAdapter;
import com.aryotech.merchant.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Update extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RequestQueue requestQueue;
    Spinner categoryDropDown;
    ArrayList<Category> categories;
    CategoriesAdapter categoriesAdapter;
    Button btnChoose, btnAddProduct;
    ImageView imageView;

    EditText editProductName, editProductQty, editProductDesc, editProductPrice;

    long id ;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc, productQty, productPrice, categoryId, merchantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Volley Queue Instance
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btnChoose = findViewById(R.id.btn_choose_image);
        btnAddProduct = findViewById(R.id.btn_add_item);

        // ImageView for hold choosed image from intent
        imageView = findViewById(R.id.image_form_file);

        //Edit Text
        editProductName = findViewById(R.id.input_item_name);
        editProductPrice = findViewById(R.id.input_item_price);
        editProductQty = findViewById(R.id.input_item_qty);
        editProductDesc = findViewById(R.id.input_item_desc);

        // Spinner Categories
        categoryDropDown = findViewById(R.id.category_dropdown);
        // Array list for hold categories from server
        categories = new ArrayList<>();
        // set categories adapter to Spinner
        categoriesAdapter = new CategoriesAdapter();
        categoryDropDown.setAdapter(categoriesAdapter);
        categoryDropDown.setOnItemSelectedListener(this);
        // get all categories from server
       getAllCategories();

        Product product1 = getIntent().getParcelableExtra("update");
        if (product1 != null){
            id = product1.getProductId();
            editProductName.setText(product1.getProductNama());
            editProductPrice.setText(String.valueOf(product1.getPrice()));
            editProductQty.setText(String.valueOf(product1.getProductQty()));
            editProductDesc.setText(product1.getProductDesc());

            String baseUrl = "http://210.210.154.65:4444/storage/";
            String url = baseUrl + product1.getProductImage();
            Glide.with(this)
                    .load(url)
                    .into(imageView);

        }

        //button choose image
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // harus dipastikan tidak ada data yang kosong...
                // lihat project VolumeApp untuk contoh validasi input tidak boleh kosong
                // ....
                // Berikut yang harus divalidasi :
                productName = editProductName.getText().toString();
                productDesc = editProductDesc.getText().toString();
                productPrice = editProductPrice.getText().toString();
                productQty = editProductQty.getText().toString();
                merchantId = "1"; //Sementara set ke 1

                //check String productImage ( sudah pilih gambar dari gallery atau belum)
                if (productImage == null) { // jika kosong,
                    productImage = null;     // isi dengan null
                }

                putProductToServer();
            }
        });
    }

    private void getAllCategories() {

        String url = "http://210.210.154.65:4444/api/categories";
        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // handle response
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                Gson gson = new Gson();
                                Category category1 = gson.fromJson(data.getJSONObject(i).toString(), Category.class);
                                categories.add(category1);
                            }

                            categoriesAdapter.addData(categories);
                            categoriesAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), String.valueOf(categoriesAdapter.getCount()), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(listCatReq);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.categoryId = String.valueOf(categoriesAdapter.getItemId(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void showFileChooser(){

        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    // get result image from intent above

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image",productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(imageView.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
                System.out.println("image : "+productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // convert image bitmap to string base64
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void putProductToServer(){

        final StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://210.210.154.65:4444/api/product/"+id+"/update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("response :", response);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Success Update product", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("productName", productName);
                params.put("productDesc", productDesc);
                params.put("productQty", productQty);

                if (productImage != null){
                    params.put("productImage", productImage);
                }
                params.put("productPrice", productPrice);
                params.put("categoryId", categoryId);
                params.put("merchantId", merchantId);
                return params;
            }
        };

        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}
