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
import android.widget.RelativeLayout;
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

public class login_activity extends AppCompatActivity implements View.OnClickListener {

    private TextView btn_login, txt_UP, txt_IK, txt_fblogin, txt_forgotpass;
    private EditText edt_username, edt_password;
    private ProgressBar Progressbar;
    private LinearLayout lin_fb;
    String f_image = "", fb_id = "", fb_username = "", fbemail = "";
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private RelativeLayout Rel_signup, Rel_forgotpass;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Log.e("login_activity","login_activity");

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        init();
        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
        if (pref.getString("Language", "").equals("en")) {

        } else if (pref.getString("Language", "").equals("fr")) {
            edt_username.setHint("Entrer courriel");
            edt_password.setHint("Mode de passe");
            txt_forgotpass.setText("Mot passe oublié");
            btn_login.setText("Connexion");
        }
    }

    public void init() {
        Progressbar = findViewById(R.id.Progressbar);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        txt_UP = findViewById(R.id.txt_UP);
        txt_IK = findViewById(R.id.txt_IK);
        Rel_signup = findViewById(R.id.Rel_signup);
        txt_fblogin = findViewById(R.id.txt_fblogin);
        txt_forgotpass = findViewById(R.id.txt_forgotpass);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        Rel_forgotpass = findViewById(R.id.Rel_forgotpass);
        lin_fb = findViewById(R.id.lin_fb);
        lin_fb.setOnClickListener(this);
        Rel_signup.setOnClickListener(this);
        Rel_forgotpass.setOnClickListener(this);

        txt_UP.setTypeface(AppController.logo_BPreplayBold);
        txt_IK.setTypeface(AppController.logo_BPreplayBold);
        txt_fblogin.setTypeface(AppController.OpenSans_Regular);
        txt_forgotpass.setTypeface(AppController.OpenSans_Regular);
        edt_username.setTypeface(AppController.OpenSans_Regular);
        edt_password.setTypeface(AppController.OpenSans_Regular);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edt_username.getText().toString().equals("")) {
                    Toast.makeText(login_activity.this, R.string.alert_email, Toast.LENGTH_LONG).show();
                } else if (edt_password.getText().toString().equals("")) {
                    Toast.makeText(login_activity.this, R.string.alert_password, Toast.LENGTH_LONG).show();
                } else if (!Appconstant.emailValidator(edt_username.getText().toString())) {
                    Toast.makeText(login_activity.this, R.string.alert_emailvalid, Toast.LENGTH_LONG).show();
                } else {
                    if (Appconstant.isNetworkAvailable(login_activity.this)) {
                        String email = edt_username.getText().toString().trim();
                        String password = edt_password.getText().toString().trim();
                        Login(email, password,"");
                        Appconstant.pos = 0;
                    } else {
                        Toast.makeText(login_activity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.Rel_signup:
                startActivity(new Intent(login_activity.this, signup_activity.class));
                finish();
                break;
            case R.id.lin_fb:
                facebooklogin();
                break;
            case R.id.Rel_forgotpass:
                startActivity(new Intent(login_activity.this, ForgotPasswordActivity.class));
                break;
        }

    }


    private void facebooklogin() {
        if (Appconstant.isNetworkAvailable(login_activity.this)) {
            callbackManager = CallbackManager.Factory.create();
            LoginManager manager = LoginManager.getInstance();
            Log.e("callbackManager", "callbackManager");
            manager.setLoginBehavior(LoginBehavior.WEB_ONLY);
            manager.logInWithReadPermissions(login_activity.this, Arrays.asList("public_profile", "user_about_me", "email", "user_birthday"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult result) {
                    // TODO Auto-generated method stub
                    System.out.println("onSuccess");
                    progressDialog = new ProgressDialog(login_activity.this);
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

                                /*Log.e("f_image", "" + f_image);
                                Log.e("fb_id", "" + fb_id);
                                Log.e("fb_username", "" + fb_username);
                                Log.e("fbemail", "" + fbemail);*/

                                /*SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userid", fb_id);
                                editor.putString("UserName", fb_username);
                                editor.putString("Email", fbemail);
                                editor.putString("Image", f_image);
                                editor.putString("login", Appconstant.socail_click);
                                editor.commit();
                                */
                                Login(fbemail, "", fb_id);
                                Appconstant.pos = 0;
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
                    Toast.makeText(login_activity.this, "Cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(login_activity.this, "onError", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(login_activity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void Login(final String email, final String password, final String FBID) {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.login, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.login + json_main.toString());
                    if (json_main.getString("success").equalsIgnoreCase("True")) {
                        JSONObject obj_User = json_main.getJSONObject("User");
                        SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userid", obj_User.getString("id"));
                        editor.putString("UserName", obj_User.getString("UserName"));
                        editor.putString("Email", obj_User.getString("Email"));
                        editor.putString("Image", obj_User.getString("Image"));
                        editor.putString("City", obj_User.getString("City"));
                        editor.putString("Country", obj_User.getString("Country"));
                        editor.putString("Biography", obj_User.getString("Biography"));
                        editor.putString("Languages", obj_User.getString("Languages"));
                        editor.putString("Phone", obj_User.getString("Phone"));
                        editor.commit();
                        if (Appconstant.bottomClick.equals("")) {
                            Intent i = new Intent(login_activity.this, nearby_job_activity.class);
                            startActivity(i);
                            finish();
                        } else if (Appconstant.bottomClick.equals("profile")) {
                            Intent i = new Intent(login_activity.this, ProfileActivity.class);
                            startActivity(i);
                            finish();
                        } else if (Appconstant.bottomClick.equals("nearJob")) {
                            Intent i = new Intent(login_activity.this, nearby_job_activity.class);
                            startActivity(i);
                            finish();
                        } else if (Appconstant.bottomClick.equals("myJob")) {
                            Intent i = new Intent(login_activity.this, MyJobActivity.class);
                            startActivity(i);
                            finish();
                        } else if (Appconstant.bottomClick.equals("Payment")) {
                            Intent i = new Intent(login_activity.this, PaymentActivity.class);
                            startActivity(i);
                            finish();
                        } else if (Appconstant.bottomClick.equals("fav_job")) {
                            Intent i = new Intent(login_activity.this, Favorite_JobsActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(login_activity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
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
                params.put("UserName", fb_username);
                params.put("Email", email);
                params.put("Password", password);
                params.put("FBID", FBID);
                params.put("Image", f_image);
                Log.e("params", "" + Appconstant.login + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.login);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
