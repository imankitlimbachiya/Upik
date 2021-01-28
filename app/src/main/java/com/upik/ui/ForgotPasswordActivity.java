package com.upik.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.utils.Appconstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 7/3/2019.
 */

public class ForgotPasswordActivity extends Activity implements View.OnClickListener {

    private LinearLayout img_back;
    private EditText edt_username, edt_password, edt_Cpassword;
    private TextView btn_Save;
    private ProgressBar Progressbar;
    private Context mContext;
    String email = "", password = "", cpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);

        Log.e("ForgotPasswordActivity","ForgotPasswordActivity");

        mContext = this;

        init();
    }

    public void init() {
        Progressbar = findViewById(R.id.Progressbar);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        edt_Cpassword = findViewById(R.id.edt_Cpassword);
        btn_Save = findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(this);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_Save:
                /*if (edt_username.getText().toString().equals("")) {
                    Toast.makeText(mContext, R.string.alert_email, Toast.LENGTH_LONG).show();
                } else if (!Appconstant.emailValidator(edt_username.getText().toString())) {
                    Toast.makeText(mContext, R.string.alert_emailvalid, Toast.LENGTH_LONG).show();
                } else if (edt_password.getText().toString().equals("")) {
                    Toast.makeText(mContext, R.string.alert_password, Toast.LENGTH_LONG).show();
                } else if (edt_Cpassword.getText().toString().equals("")) {
                    Toast.makeText(mContext, R.string.alert_cpassword, Toast.LENGTH_LONG).show();
                } else if (!edt_Cpassword.getText().toString().equals(edt_password.getText().toString())) {
                    Toast.makeText(mContext, R.string.alert_mismatchedPass, Toast.LENGTH_LONG).show();
                } else {
                    email = edt_username.getText().toString().trim();
                    password = edt_password.getText().toString().trim();
                    cpassword = edt_Cpassword.getText().toString().trim();
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        forget_password(email, password, cpassword);
                    } else {
                        Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }*/
                if (edt_username.getText().toString().equals("")) {
                    Toast.makeText(mContext, R.string.alert_email, Toast.LENGTH_LONG).show();
                } else {
                    email = edt_username.getText().toString().trim();
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        forget_password(email, password, cpassword);
                    } else {
                        Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void forget_password(final String email, final String password, final String cpassword) {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.forget_password, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.forget_password + json_main.toString());
                    if (json_main.getString("success").equalsIgnoreCase("True")) {
                        Toast.makeText(mContext, json_main.getString("success_msg"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
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
                params.put("Email", email);
                // params.put("newPassword", password);
                // params.put("confirmPassword", cpassword);
                Log.e("params", "" + Appconstant.forget_password + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.forget_password);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
