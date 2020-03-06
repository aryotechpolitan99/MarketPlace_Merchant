package com.aryotech.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @BindView(R.id.et_email2) EditText editTextEmail;
    @BindView(R.id.et_pass) EditText editTextPassword;
    @BindView(R.id.btn_login) Button buttonLogin;

    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CPASSWORD = "confirm_password";
    final String IS_MERCHANT = "is_merchant";
    final String MERCHANT_NAME = "merchant_name";

    AccessToken accessToken;
    RequestQueue requestQueue;

    String firstName, lastName, email, password, confirmPassword, merchantName;
    int isMerchant = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_regis)
    public void goToRegisterActivity(){
        Intent intent = new Intent(Login.this,Registry.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void register(){

        if(isValidInput() == true){
            postDataRegister();
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }

    }

    private void postDataRegister(){

        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/login";
        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accessToken);
                        Toast.makeText(Login.this, String.valueOf(accessToken.getAccessToken()), Toast.LENGTH_SHORT).show();

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
                        catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        catch (JSONException e){
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

                params.put(EMAIL,email);
                params.put(PASSWORD,password);


                return params;
            }
        };

        VolleyService.getINSTANCE(getApplicationContext()).addToRequestQueue(registerReq);
    }

    private boolean isValidInput(){

        boolean isValid = true;



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


        return isValid;
    }
}
