package com.upik.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.BuildConfig;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.utils.Appconstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class signup_activity extends AppCompatActivity implements View.OnClickListener {
    private TextView btn_login, lbl_UP, lbl_IK, txt_Signup, txt_OR, txt_fblogin;
    private EditText edt_username, edt_Email, edt_password;
    private ProgressBar Progressbar;
    private LinearLayout lin_fb;
    String f_image = "", fb_id = "", fb_username = "", fbemail = "";
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        Log.e("signup_activity","signup_activity");

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        init();
    }

    public void init() {
        Progressbar = findViewById(R.id.Progressbar);

        btn_login = findViewById(R.id.btn_login);
        lbl_UP = findViewById(R.id.lbl_UP);
        lbl_IK = findViewById(R.id.lbl_IK);
        edt_username = findViewById(R.id.edt_username);
        edt_Email = findViewById(R.id.edt_Email);
        edt_password = findViewById(R.id.edt_password);
        txt_Signup = findViewById(R.id.txt_Signup);
        txt_OR = findViewById(R.id.txt_OR);
        txt_fblogin = findViewById(R.id.txt_fblogin);
        lin_fb = findViewById(R.id.lin_fb);

        lbl_UP.setTypeface(AppController.logo_BPreplayBold);
        lbl_IK.setTypeface(AppController.logo_BPreplayBold);
        edt_username.setTypeface(AppController.OpenSans_Regular);
        edt_Email.setTypeface(AppController.OpenSans_Regular);
        edt_password.setTypeface(AppController.OpenSans_Regular);
        txt_Signup.setTypeface(AppController.OpenSans_Regular);
        txt_OR.setTypeface(AppController.OpenSans_Regular);
        txt_fblogin.setTypeface(AppController.OpenSans_Regular);
        btn_login.setTypeface(AppController.OpenSans_Regular);

        txt_Signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        lin_fb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent intent_login = new Intent(signup_activity.this, login_activity.class);
                startActivity(intent_login);
                break;
            case R.id.txt_Signup:
                if (edt_username.getText().toString().equals("")) {
                    Toast.makeText(signup_activity.this,R.string.alert_username,Toast.LENGTH_LONG).show();
                }else if(edt_Email.getText().toString().equals("")){
                    Toast.makeText(signup_activity.this,R.string.alert_email,Toast.LENGTH_LONG).show();
                }else if(!Appconstant.emailValidator(edt_Email.getText().toString())){
                    Toast.makeText(signup_activity.this,R.string.alert_emailvalid,Toast.LENGTH_LONG).show();
                }else if(edt_password.getText().toString().equals("")){
                    Toast.makeText(signup_activity.this,R.string.alert_password,Toast.LENGTH_LONG).show();
                }else{
                    if(Appconstant.isNetworkAvailable(signup_activity.this)){
                        String userName=edt_username.getText().toString().trim();
                        String email=edt_Email.getText().toString().trim();
                        String password=edt_password.getText().toString().trim();
                        Signup(userName,email,password,"","");
                    }else{
                        Toast.makeText(signup_activity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.lin_fb:
                facebooklogin();
                break;
        }

    }


    private void Signup(final String userName,final String email,final String password,final String fb_id,final String image) {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.signup + json_main.toString());
                    if(json_main.getString("success").equalsIgnoreCase("True")){
                        Toast.makeText(signup_activity.this,json_main.getString("message"),Toast.LENGTH_LONG).show();
                       // JSONObject obj_User = json_main.getJSONObject("User");
                        SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userid", json_main.getString("id"));
                        editor.putString("UserName", json_main.getString("UserName"));
                        editor.putString("Email", json_main.getString("Email"));
                        editor.putString("Image", json_main.getString("Image"));
                        editor.putString("City", json_main.getString("City"));
                        editor.putString("Country", json_main.getString("Country"));
                        editor.putString("Biography", json_main.getString("Biography"));
                        editor.putString("Languages", json_main.getString("Languages"));
                        editor.putString("Phone", json_main.getString("Phone"));
                        editor.commit();
                        Intent intent_login = new Intent(signup_activity.this, nearby_job_activity.class);
                        startActivity(intent_login);
                    }else{
                        Toast.makeText(signup_activity.this,json_main.getString("error_msg"),Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Progressbar.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Progressbar.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName",userName);
                params.put("Email",email);
                params.put("Password",password);
                params.put("FBID",fb_id);
                params.put("Image",image);
                Log.e("params",""+Appconstant.signup+params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.signup);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void facebooklogin() {
        if (Appconstant. isNetworkAvailable(signup_activity.this)) {
            callbackManager = CallbackManager.Factory.create();
            LoginManager manager = LoginManager.getInstance();
            Log.e("callbackManager", "callbackManager");
            manager.setLoginBehavior(LoginBehavior.WEB_ONLY);
            manager.logInWithReadPermissions(signup_activity.this, Arrays.asList("public_profile", "user_about_me", "email", "user_birthday"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult result) {
                    // TODO Auto-generated method stub
                    System.out.println("onSuccess");
                    progressDialog = new ProgressDialog(signup_activity.this);
                    progressDialog.setMessage("Processing datas...");
                    progressDialog.show();
                    String accessToken = result.getAccessToken().getToken();
                    GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(org.json.JSONObject object, GraphResponse response) {
                            try {
                                Log.e("response", "" + response);
                                Log.e("object", "" + object);
                                fb_id = object.getString("id");
                                fb_username = object.getString("name");
                                fbemail = object.getString("email");
                                f_image = "http://graph.facebook.com/" + fb_id + "/picture?type=large";

                                Log.e("fb_id", "" + fb_id);
                                Log.e("fb_username", "" + fb_username);
                                Log.e("fbemail", "" + fbemail);
                                Log.e("f_image", "" + f_image);
                                Signup(fb_username,fbemail,"",fb_id,f_image);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    progressDialog.dismiss();
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,first_name,last_name,gender");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(signup_activity.this, "Cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(signup_activity.this, "onError", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(signup_activity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

}
