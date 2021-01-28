package com.upik.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WorkExperienceActivity extends BaseActivity {
    private Context mContext;
    private TextView btn_addEXp, txt_startDate, txt_EndDate, txt_Save;
    private EditText txt_addJobtitle, txt_job_desc;
    private LinearLayout back, img_startdate, img_Enddate;
    SharedPreferences pref;
    private ProgressBar Progressbar;
    String startDate = "", endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workexperience);

        Log.e("WorkExperienceActivity","WorkExperienceActivity");


        mContext = this;
        back = findViewById(R.id.back);
        btn_addEXp = findViewById(R.id.btn_addEXp);
        txt_startDate = findViewById(R.id.txt_startDate);
        txt_EndDate = findViewById(R.id.txt_EndDate);
        txt_addJobtitle = findViewById(R.id.txt_addJobtitle);
        txt_job_desc = findViewById(R.id.txt_job_desc);
        img_startdate = findViewById(R.id.img_startdate);
        img_Enddate = findViewById(R.id.img_Enddate);
        Progressbar = findViewById(R.id.Progressbar);
        txt_Save = findViewById(R.id.txt_Save);

        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstant.check_date = "Startdate";
                Datitimepickertime1();
            }
        });
        img_Enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstant.check_date = "Enddate";
                Datitimepickertime1();
            }
        });

        txt_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_addJobtitle.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add job title.", Toast.LENGTH_LONG).show();
                } else if (txt_startDate.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add start date.", Toast.LENGTH_LONG).show();
                } else if (txt_EndDate.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add end date.", Toast.LENGTH_LONG).show();
                } else if (txt_job_desc.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add job description.", Toast.LENGTH_LONG).show();
                } else {
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        add_work_experience();
                    } else {
                        Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_addEXp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_addJobtitle.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add job title.", Toast.LENGTH_LONG).show();
                } else if (txt_startDate.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add start date.", Toast.LENGTH_LONG).show();
                } else if (txt_EndDate.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add end date.", Toast.LENGTH_LONG).show();
                } else if (txt_job_desc.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please add job description.", Toast.LENGTH_LONG).show();
                } else {
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        add_work_experience();
                    } else {
                        Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void add_work_experience() {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.add_work_experience, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.add_work_experience + json_main.toString());
                    if (json_main.getString("success").equals("True")) {
                        Toast.makeText(mContext, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
                        txt_addJobtitle.setText("");
                        txt_startDate.setText("");
                        txt_EndDate.setText("");
                        txt_job_desc.setText("");
                        Appconstant.check_date = "";
                        setResult(RESULT_OK);
                        finish();
                    }

                } catch (Exception e) {
                    setResult(RESULT_CANCELED);
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
                params.put("UserID", pref.getString("userid", ""));
                params.put("JobTitle", txt_addJobtitle.getText().toString());
                params.put("StartDate", txt_startDate.getText().toString());
                params.put("EndDate", txt_EndDate.getText().toString());
                params.put("JobDescription", txt_job_desc.getText().toString());
                Log.e("params", "" + Appconstant.add_work_experience + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.add_work_experience);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void Datitimepickertime1() {
        final View dialogView = View.inflate(mContext, R.layout.datetimepickernew, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        dialogView.findViewById(R.id.rel_ok1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.GONE);
                datePicker.setMaxDate(System.currentTimeMillis());
                Calendar c = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                long time = c.getTimeInMillis();
                if (Appconstant.check_date.equals("Startdate")) {
                    startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
                    txt_startDate.setText(startDate);
                } else {
                    endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
                    txt_EndDate.setText(endDate);
                }

                alertDialog.dismiss();
            }
        });


        dialogView.findViewById(R.id.rel_cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

}