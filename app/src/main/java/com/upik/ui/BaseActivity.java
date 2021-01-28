package com.upik.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.BuildConfig;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.utils.Appconstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/3/2019.
 */

public class BaseActivity extends AppCompatActivity {

    public DrawerLayout Drawer_layout;
    protected FrameLayout frame_container;
    public ImageView menu, Img_location;
    public static CircleImageView img_sideImage;
    public TextView txt_title, lbl_home, lbl_nearby, lbl_language, lbl_profile, lbl_myjob, lbl_payment, lbl_setting, lbl_login, txt_username, txt_email, txt_guest, lbl_favjob;
    public LinearLayout slide_home, slide_nearjob, slide_Profile, slide_Myjob, slidePayment, slide_language, slideSetting, slideLogin, slidelogout, slide_favjob;
    public LinearLayout lin_userDetail;
    public SharedPreferences pref;
    private Context mContext;
    private ProgressBar Progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mContext = this;
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
        Drawer_layout = findViewById(R.id.Drawer_layout);
        frame_container = findViewById(R.id.frame_container);
        Img_location = findViewById(R.id.Img_location);
        img_sideImage = findViewById(R.id.img_sideImage);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(onClickListenerBase);
        Img_location.setOnClickListener(onClickListenerBase);

        slide_home = findViewById(R.id.slide_home);
        slide_nearjob = findViewById(R.id.slide_nearjob);
        slide_Profile = findViewById(R.id.slide_Profile);
        slide_Myjob = findViewById(R.id.slide_Myjob);
        slidePayment = findViewById(R.id.slidePayment);
        slideSetting = findViewById(R.id.slideSetting);
        slideLogin = findViewById(R.id.slideLogin);
        slidelogout = findViewById(R.id.slidelogout);
        slide_language = findViewById(R.id.slide_language);
        slide_favjob = findViewById(R.id.slide_favjob);
        lin_userDetail = findViewById(R.id.lin_userDetail);
        Progressbar = findViewById(R.id.Progressbar);

        txt_title = findViewById(R.id.txt_title);
        lbl_home = findViewById(R.id.lbl_home);
        lbl_nearby = findViewById(R.id.lbl_nearby);
        lbl_profile = findViewById(R.id.lbl_profile);
        lbl_myjob = findViewById(R.id.lbl_myjob);
        lbl_payment = findViewById(R.id.lbl_payment);
        lbl_setting = findViewById(R.id.lbl_setting);
        lbl_login = findViewById(R.id.lbl_login);
        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
        txt_guest = findViewById(R.id.txt_guest);
        lbl_favjob = findViewById(R.id.lbl_favjob);
        lbl_language = findViewById(R.id.lbl_language);

        lbl_home.setTypeface(AppController.OpenSans_Regular);
        lbl_nearby.setTypeface(AppController.OpenSans_Regular);
        lbl_profile.setTypeface(AppController.OpenSans_Regular);
        lbl_myjob.setTypeface(AppController.OpenSans_Regular);
        lbl_payment.setTypeface(AppController.OpenSans_Regular);
        lbl_setting.setTypeface(AppController.OpenSans_Regular);
        lbl_login.setTypeface(AppController.OpenSans_Regular);
        txt_username.setTypeface(AppController.OpenSans_Regular);
        txt_email.setTypeface(AppController.OpenSans_Regular);
        lbl_language.setTypeface(AppController.OpenSans_Regular);
    }

    View.OnClickListener onClickListenerBase = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu:
                    Appconstant.view = "";
                    openDrawer();
                    break;
                case R.id.Img_location:
                    if (Appconstant.view.equals("map")) {
                        Appconstant.view = "list";
                        nearby_job_activity.linear_mainview.setVisibility(View.VISIBLE);
                        nearby_job_activity.linearMapParent.setVisibility(View.GONE);
                        Img_location.setImageResource(R.drawable.top_mapicon);
                        //nearby_job_activity.pos = 0;
                        nearby_job_activity.categoryAdpter.notifyDataSetChanged();
                    } else {
                        Appconstant.view = "map";
                        Img_location.setImageResource(R.drawable.list);
                        nearby_job_activity.linear_mainview.setVisibility(View.GONE);
                        nearby_job_activity.linearMapParent.setVisibility(View.VISIBLE);
                        nearby_job_activity.txt_noJobs.setVisibility(View.GONE);
                        //  nearby_job_activity.pos = 0;
                        nearby_job_activity.categoryAdpter.notifyDataSetChanged();
                    }

                    break;

            }
        }
    };

    public void openDrawer() {
        if (Drawer_layout.isDrawerVisible(GravityCompat.START)) {
            Drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            Drawer_layout.openDrawer(GravityCompat.START);
        }
    }

    public void SlideMenu(View v) {
        Drawer_layout.closeDrawers();
        switch (v.getId()) {
            case R.id.slide_home:
                Appconstant.view = "";
                Appconstant.Silde_click = "home";
                Appconstant.view = "list";
                slide_home.setBackgroundColor(Color.parseColor("#000000"));
                startActivity(new Intent(BaseActivity.this, nearby_job_activity.class));
                finish();
                Img_location.setVisibility(View.VISIBLE);
                break;
            case R.id.slide_nearjob:
                Appconstant.view = "";
                Appconstant.Silde_click = "nearJob";
                Appconstant.bottomClick = "nearJob";
                Appconstant.view = "list";
                if (pref.getString("userid", "").equals("")) {
                    startActivity(new Intent(BaseActivity.this, login_activity.class));
                } else {
                    startActivity(new Intent(BaseActivity.this, nearby_job_activity.class));
                }
                slide_nearjob.setBackgroundColor(Color.parseColor("#000000"));
                Img_location.setVisibility(View.VISIBLE);
                break;
            case R.id.slide_Profile:
                Appconstant.view = "";
                Appconstant.bottomClick = "profile";
                Appconstant.view = "list";
                if (pref.getString("userid", "").equals("")) {
                    startActivity(new Intent(BaseActivity.this, login_activity.class));
                    finish();
                } else {
                    startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                    finish();
                }
                slide_Profile.setBackgroundColor(Color.parseColor("#000000"));
                break;
            case R.id.slide_language:
                Log.e("App","" + Appconstant.languageCheck);
                if (Appconstant.languageCheck.equals("1")) {
                    SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Language", "fr");
                    editor.commit();
                    Appconstant.languageCheck = "0";
                    Appconstant.Silde_click = "home";
                    Appconstant.view = "list";
                    slide_home.setBackgroundColor(Color.parseColor("#000000"));
                    startActivity(new Intent(BaseActivity.this, nearby_job_activity.class));
                    finish();
                    Img_location.setVisibility(View.VISIBLE);
                    Log.e("Language","" + pref.getString("Language", ""));
                } else {
                    SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Language", "en");
                    editor.commit();
                    Appconstant.languageCheck = "1";
                    Appconstant.Silde_click = "home";
                    Appconstant.view = "list";
                    slide_home.setBackgroundColor(Color.parseColor("#000000"));
                    startActivity(new Intent(BaseActivity.this, nearby_job_activity.class));
                    finish();
                    Img_location.setVisibility(View.VISIBLE);
                    Log.e("Language","" + pref.getString("Language", ""));
                }
                break;
            case R.id.slide_Myjob:
                Appconstant.view = "";
                Appconstant.bottomClick = "myJob";
                Appconstant.view = "list";
                slide_Myjob.setBackgroundColor(Color.parseColor("#000000"));
                if (pref.getString("userid", "").equals("")) {
                    startActivity(new Intent(BaseActivity.this, login_activity.class));
                    finish();
                } else {
                    startActivity(new Intent(BaseActivity.this, MyJobActivity.class));
                    finish();
                }
                break;
            case R.id.slidePayment:
                Appconstant.view = "";
                Appconstant.bottomClick = "Payment";
                Appconstant.view = "list";
                slidePayment.setBackgroundColor(Color.parseColor("#000000"));
                if (pref.getString("userid", "").equals("")) {
                    startActivity(new Intent(BaseActivity.this, login_activity.class));
                    finish();
                } else {
                    startActivity(new Intent(BaseActivity.this, PaymentActivity.class));
                    finish();
                }

                break;
            case R.id.slide_favjob:
                Appconstant.view = "";
                Appconstant.bottomClick = "fav_job";
                Appconstant.view = "list";
                slide_favjob.setBackgroundColor(Color.parseColor("#000000"));
                if (pref.getString("userid", "").equals("")) {
                    startActivity(new Intent(BaseActivity.this, login_activity.class));
                    finish();
                } else {
                    startActivity(new Intent(BaseActivity.this, Favorite_JobsActivity.class));
                    finish();
                }
                break;
          /*  case R.id.slideSetting:
                Appconstant.view = "";
                slideSetting.setBackgroundColor(Color.parseColor("#000000"));
                Img_location.setVisibility(View.GONE);
                txt_title.setText(R.string.setting);
                break;*/
            case R.id.slideLogin:
                Appconstant.view = "";
                Appconstant.view = "list";
                slideLogin.setBackgroundColor(Color.parseColor("#000000"));
                startActivity(new Intent(BaseActivity.this, login_activity.class));
                finish();
                break;
            case R.id.slidelogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage(R.string.alertlogout);
                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        SharedPreferences preferences = getSharedPreferences("Login_Data", 0);
                        preferences.edit().clear().commit();
                        Intent intent_login = new Intent(BaseActivity.this, nearby_job_activity.class);
                        startActivity(intent_login);
                        finish();
                        Appconstant.bottomClick = "";
                        Appconstant.view = "list";
                        LoginManager.getInstance().logOut();

                    }
                });
                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Appconstant.view = "list";
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                break;
        }
    }


    private void Profile() {
        String tag_string_req = "req";
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.list_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.list_profile + json_main.toString());
                    if (json_main.getString("success").equalsIgnoreCase("True")) {
                        JSONObject obj_profile = json_main.getJSONObject("Profile");
                        SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("UserID", obj_profile.getString("UserID"));
                        editor.putString("UserName", obj_profile.getString("UserName"));
                        editor.putString("Email", obj_profile.getString("Email"));
                        editor.putString("Image", obj_profile.getString("Image"));
                        editor.commit();
                        if (obj_profile.getString("Image").equals("") || obj_profile.getString("Image").equals("null") || obj_profile.getString("Image").equals(null) || obj_profile.getString("Image") == null) {

                        } else {
                            Glide.with(mContext).load(obj_profile.getString("Image")).centerCrop().into(img_sideImage);
                        }

                        if (obj_profile.getString("UserName").equals("") || obj_profile.getString("UserName").equals("null") || obj_profile.getString("UserName").equals(null) || obj_profile.getString("UserName") == null) {

                        } else {
                            txt_username.setText(pref.getString("UserName", ""));
                        }

                        if (obj_profile.getString("Email").equals("") || obj_profile.getString("Email").equals("null") || obj_profile.getString("Email").equals(null) || obj_profile.getString("Email") == null) {

                        } else {
                            txt_email.setText(pref.getString("Email", ""));
                        }
                        slideLogin.setVisibility(View.GONE);
                        slidelogout.setVisibility(View.VISIBLE);
                        lin_userDetail.setVisibility(View.VISIBLE);
                        txt_guest.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(BaseActivity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", pref.getString("userid", ""));
                Log.e("params", "" + Appconstant.list_profile + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.list_profile);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onResume() {
        Log.e("onResume Baseactivity", "onResume Baseactivity");
        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
        if (pref.getString("userid", "").equals("")) {
            slideLogin.setVisibility(View.VISIBLE);
            slidelogout.setVisibility(View.GONE);
            lin_userDetail.setVisibility(View.GONE);
            txt_guest.setVisibility(View.VISIBLE);
        } else {
            if (Appconstant.isNetworkAvailable(BaseActivity.this)) {
                Profile();
            } else {
                Toast.makeText(BaseActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
            }
        }

        Log.e("Language","" + pref.getString("Language", ""));

        if (pref.getString("Language", "").equals("en")) {
            lbl_language.setText("FR");
            lbl_home.setText("Home");
            lbl_nearby.setText("Nearby jobs");
            lbl_profile.setText("Profile");
            lbl_myjob.setText("My jobs");
            lbl_payment.setText("payment");
            lbl_setting.setText("Settings");
            lbl_login.setText("Login");
            // lbl_favjob.setText("fr");
        } else if (pref.getString("Language", "").equals("fr")) {
            lbl_language.setText("EN");
            lbl_home.setText("Accueil");
            lbl_nearby.setText("Emploi à proximité");
            lbl_profile.setText("Profil");
            lbl_myjob.setText("Favoris");
            lbl_payment.setText("paiement");
            lbl_setting.setText("Réglages");
            lbl_login.setText("Connexion");
            // lbl_favjob.setText("en");
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}