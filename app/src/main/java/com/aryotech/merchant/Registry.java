package com.aryotech.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aryotech.merchant.Model.AccessToken;
import com.aryotech.merchant.Model.RegisterErrorResponse;
import com.aryotech.merchant.Network.VolleyService;
import com.aryotech.merchant.Utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class Registry extends AppCompatActivity {

    @BindView(R.id.et_firstname) EditText editTextFirstName;
    @BindView(R.id.et_lastname) EditText editTextLastName;
    @BindView(R.id.et_email) EditText editTextEmail;
    @BindView(R.id.et_passwordreg) EditText editTextPassword;
    @BindView(R.id.et_confirmpass) EditText editTextConfirmPassword;
    @BindView(R.id.et_merchname) EditText editTextMerchantName;
    @BindView(R.id.checkbox_merch) CheckBox checkBoxAsMerchant;

    AccessToken accessToken;
    RequestQueue requestQueue;

    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CPASSWORD = "confirm_password";
    final String IS_MERCHANT = "is_merchant";
    final String MERCHANT_NAME = "merchant_name";


    String firstName, lastName, email, password, confirmPassword, merchantName;
    int isMerchant = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_login)
    public void goToLoginActivity(){
        Intent intent = new Intent(Registry.this,Login.class);
        startActivity(intent);
    }

    @OnCheckedChanged(R.id.checkbox_merch)
    public void toggleShowMerchant(){
        if(checkBoxAsMerchant.isChecked()){
            editTextMerchantName.setVisibility(View.VISIBLE);
        }
        else{
            editTextMerchantName.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_signup)
    public void register(){
        if(isValidInput() == true){
            postDataRegister();
        }
    }

    private void postDataRegister(){
        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        confirmPassword = editTextConfirmPassword.getText().toString();
        isMerchant = 1;
        merchantName = editTextMerchantName.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/signup";
        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accessToken);
                        Toast.makeText(Registry.this, String.valueOf(accessToken.getAccessToken())
                                , Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf( error.networkResponse.statusCode );
                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);

                            RegisterErrorResponse errorResponse = new Gson().fromJson(res.getJSONObject("error").toString(),RegisterErrorResponse.class);

                            if (errorResponse.getEmailError().size()>0){
                                if (errorResponse.getEmailError().get(0)!= null){
                                    editTextEmail.setError(errorResponse.getEmailError().get(0));
                                }
                            }
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new Hashtable<>();

                headers.put("Accept","application/json");
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put(FIRST_NAME,firstName);
                params.put(LAST_NAME,lastName);
                params.put(EMAIL,email);
                params.put(PASSWORD,password);
                params.put(CPASSWORD,confirmPassword);
                params.put(IS_MERCHANT,String.valueOf(isMerchant));
                params.put(MERCHANT_NAME,merchantName);

                return params;
            }
        };
        VolleyService.getINSTANCE(getApplicationContext()).addToRequestQueue(registerReq);
    }

    private boolean isValidInput(){

        boolean isValid = true;

        if(editTextFirstName.getText().toString().isEmpty()){
            editTextFirstName.setError("First name cannot be empty");
            isValid = false;
        }
        if(editTextLastName.getText().toString().isEmpty()){
            editTextLastName.setError("Last name cannot be empty");
            isValid = false;
        }

        if(editTextEmail.getText().toString().isEmpty()){
            editTextEmail.setError("email name cannot be empty");
            isValid = false;
        }else if(!editTextEmail.getText().toString().contains("@")){
            editTextEmail.setError("must be a valid email");
            isValid = false;
        }

        if(editTextPassword.getText().toString().isEmpty()){
            editTextPassword.setError("Password cannot be empty");
            isValid = false;
        }
        else if(editTextPassword.getText().toString().length() < 8){
            editTextPassword.setError("Password must be 8 or more character");
            isValid = false;
        }

        if(editTextConfirmPassword.getText().toString().isEmpty()){
            editTextConfirmPassword.setError("Confirm password cannot be empty");
            isValid = false;
        }
        else if(!(editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString()))){
            editTextConfirmPassword.setError("Password did not match");
            isValid = false;
        }

        if(editTextMerchantName.getText().toString().isEmpty()){
            editTextMerchantName.setError("Merchant Name cannot be empty");
            isValid = false;
        }

        return isValid;
    }
}
