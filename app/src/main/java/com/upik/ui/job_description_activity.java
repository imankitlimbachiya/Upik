package com.upik.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.adpter.FeedbackListAdapter;
import com.upik.model.FeedbackListModel;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class job_description_activity extends Activity {

    private Context mContext;
    private TextView txt_Jobtitle, txt_JobMarchant, txt_jobtime, txt_date, txt_pay, txt_description, txt_WorkingHours, txt_requirement1,
            txt_requirement2, txt_requirement3, txt_arrival, txt_apply, txt_dateEnd, txt_comment;
    private ImageView Img_location, img_fav, Background;
    private LinearLayout back, ratingLayout;
    private CircleImageView img_jobimage;
    ProgressBar Progressbar1;
    String fileName = "", StartDate = "", EndDate = "", UrgentListing = "", HourlyPay = "", PayType = "";
    RatingBar ratingBarMain;
    ArrayList<FeedbackListModel> feedbackList = new ArrayList<>();
    SharedPreferences pref;
    RecyclerView feedbackView;
    String str_JobTitle = "", str_JobID = "", JobApplyID = "", AdminComment = "", AdminRating = "",
            str_CategoryID = "", str_Rating = "", str_Miles = "", str_Timing = "", str_Date = "",
            str_Long = "", str_Lat = "", str_Agent = "", str_JobTime = "", str_CreatedDate = "",
            str_TotalPay = "", str_WorkingHours = "", str_desc = "", str_Requirement = "",Rating = "",
            str_Arrival = "", JobImage = "", merchant = "", IsFavorite = "", Background_Image = "", MerchantID = "";
    Dialog mDialog;
    TextView what, req, arrivel;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_descrition_layout);

        Log.e("job_description_activity","job_description_activity");

        mContext = this;

        txt_Jobtitle = findViewById(R.id.txt_Jobtitle);
        txt_JobMarchant = findViewById(R.id.txt_JobMarchant);
        txt_jobtime = findViewById(R.id.txt_jobtime);
        txt_date = findViewById(R.id.txt_date);
        txt_pay = findViewById(R.id.txt_pay);
        img_fav = findViewById(R.id.img_fav);
        back = findViewById(R.id.back);
        txt_WorkingHours = findViewById(R.id.txt_WorkingHours);
        txt_description = findViewById(R.id.txt_description);
        txt_requirement1 = findViewById(R.id.txt_requirement1);
        txt_requirement2 = findViewById(R.id.txt_requirement2);
        txt_requirement3 = findViewById(R.id.txt_requirement3);
        txt_arrival = findViewById(R.id.txt_arrival);
        Img_location = findViewById(R.id.Img_location);
        img_jobimage = findViewById(R.id.img_jobimage);
        Progressbar1 = findViewById(R.id.Progressbar1);
        txt_apply = findViewById(R.id.txt_apply);
        Background = findViewById(R.id.Background);
        ratingBarMain = findViewById(R.id.ratingBarMain);
        txt_dateEnd = findViewById(R.id.txt_dateEnd);
        txt_comment = findViewById(R.id.txt_comment);
        ratingLayout = findViewById(R.id.ratingLayout);

        what = findViewById(R.id.what);
        req = findViewById(R.id.req);
        arrivel = findViewById(R.id.arrivel);

        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
        if (pref.getString("Language", "").equals("en")) {
            // what.setText("fr");
            // req.setText("fr");
            // arrivel.setText("fr");
        } else if (pref.getString("Language", "").equals("fr")) {
            what.setText("Les candidats doivent");
            req.setText("Compétences requises");
            arrivel.setText("Instructions d’arrivée");
            txt_apply.setText("Postuler");
        }

        intentData();

        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsFavorite.equals("1")) {
                    favorite_job(str_JobID, "0");
                } else {
                    favorite_job(str_JobID, "1");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appconstant.view = "list";
                Intent intent = new Intent(job_description_activity.this, JobOnMapLocationActivity.class);
                intent.putExtra("lat", str_Lat);
                intent.putExtra("long", str_Long);
                startActivity(intent);
            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFeedbackListApi();
            }
        });

        txt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Appconstant.isNetworkAvailable(job_description_activity.this)) {
                    if (Appconstant.Category.equals("Applied")) {
                        if (Appconstant.isNetworkAvailable(mContext)) {
                            android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mContext);
                            builder1.setTitle(R.string.app_name);
                            builder1.setMessage("Are you sure want to withdraw this job?");
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    add_withdraw();
                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            android.support.v7.app.AlertDialog alert1 = builder1.create();
                            alert1.show();
                        } else {
                            Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String UserID = pref.getString("userid","");
                        String Email = pref.getString("Email","");
                        String Image = pref.getString("Image","");
                        String City = pref.getString("City","");
                        String Country = pref.getString("Country","");
                        String Biography = pref.getString("Biography","");
                        String Languages = pref.getString("Languages","");
                        String Phone = pref.getString("Phone","");

                        if (UserID.equals("") || UserID.equals("null") || UserID.equals(null) || UserID == null) {
                            Toast.makeText(mContext,"Please login for applying job.", Toast.LENGTH_LONG).show();
                        } else if (pref.getString("UserName","").equals("")) {
                            Toast.makeText(mContext,"", Toast.LENGTH_LONG).show();
                        } else if (Email.equals("") || Email.equals("null") || Email.equals(null) || Email == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else if (Image.equals("") || Image.equals("null") || Image.equals(null) || Image == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else if (City.equals("") || City.equals("null") || City.equals(null) || City == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else if (Country.equals("") || Country.equals("null") || Country.equals(null) || Country == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else if (Biography.equals("") || Biography.equals("null") || Biography.equals(null) || Biography == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else if (Languages.equals("") || Languages.equals("null") || Languages.equals(null) || Languages == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else if (Phone.equals("") || Phone.equals("null") || Phone.equals(null) || Phone == null) {
                            Toast.makeText(mContext,"Please Update your Profile", Toast.LENGTH_LONG).show();
                        } else {
                            add_my_job();
                        }
                    }
                } else {
                    Toast.makeText(job_description_activity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setFeedbackListApi() {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.feedback_list_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        feedbackView = (RecyclerView) mDialog.findViewById(R.id.feedbackView);
        final ProgressBar progress = (ProgressBar) mDialog.findViewById(R.id.progress);
        feedbackListApi(str_JobID, progress);
        mDialog.show();
    }

    private void feedbackListApi(final String JobID, final ProgressBar progress) {
        String tag_string_req = "req";
        progress.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.feedback_list, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progress.setVisibility(View.GONE);
                            JSONObject json_main = new JSONObject(response);
                            Log.e("response", "" + Appconstant.feedback_list + json_main);
                            if (json_main.getString("success").equalsIgnoreCase("true")) {
                                String str_msg = json_main.getString("error_msg");
                                // Toast.makeText(mContext, str_msg, Toast.LENGTH_LONG).show();
                                JSONArray arrayFeedbackList = json_main.getJSONArray("feedback_list");
                                for (int i = 0; i < arrayFeedbackList.length(); i++) {
                                    FeedbackListModel feedbackListModel = new FeedbackListModel();
                                    feedbackListModel.setFeedBackID(arrayFeedbackList.getJSONObject(i).getString("FeedBackID"));
                                    feedbackListModel.setUserID(arrayFeedbackList.getJSONObject(i).getString("UserID"));
                                    feedbackListModel.setJobID(arrayFeedbackList.getJSONObject(i).getString("JobID"));
                                    feedbackListModel.setImage(arrayFeedbackList.getJSONObject(i).getString("Image"));
                                    feedbackListModel.setUserName(arrayFeedbackList.getJSONObject(i).getString("UserName"));
                                    feedbackListModel.setComment(arrayFeedbackList.getJSONObject(i).getString("Comment"));
                                    feedbackListModel.setRating(arrayFeedbackList.getJSONObject(i).getString("Rating"));
                                    feedbackListModel.setCreatedDate(arrayFeedbackList.getJSONObject(i).getString("CreatedDate"));
                                    feedbackList.add(feedbackListModel);
                                }
                                FeedbackListAdapter feedbackListAdapter = new FeedbackListAdapter(mContext, feedbackList);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                feedbackView.setLayoutManager(linearLayoutManager);
                                feedbackView.setAdapter(feedbackListAdapter);
                            } else {
                                mDialog.cancel();
                                Toast.makeText(mContext, json_main.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progress.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                });
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MerchantID", MerchantID);
                Log.e("params", "" + Appconstant.feedback_list + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.feedback_list);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void intentData() {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            str_JobTitle = (String) bd.get("JobTitle");
            str_JobID = (String) bd.get("JobID");
            str_CategoryID = (String) bd.get("CategoryID");
            merchant = (String) bd.get("merchant");
            MerchantID = (String) bd.get("MerchantID");
            str_Timing = (String) bd.get("Timing");
            str_JobTime = (String) bd.get("JobTime");
            str_CreatedDate = (String) bd.get("CreatedDate");
            str_Date = (String) bd.get("Date");
            str_TotalPay = (String) bd.get("TotalPay");
            str_Agent = (String) bd.get("Agent");
            str_WorkingHours = (String) bd.get("WorkingHours");
            str_desc = (String) bd.get("YourDescription");
            str_Requirement = (String) bd.get("Requirement");
            str_Arrival = (String) bd.get("ArrivalInstuction");
            str_Lat = (String) bd.get("Lat");
            str_Long = (String) bd.get("Long");
            str_Miles = (String) bd.get("Miles");
            str_Rating = (String) bd.get("Rating");
            JobImage = (String) bd.get("JobImage");
            IsFavorite = (String) bd.get("IsFavorite");
            Background_Image = (String) bd.get("Background");
            Rating = (String) bd.get("Rating");
            StartDate = (String) bd.get("StartDate");
            EndDate = (String) bd.get("EndDate");
            UrgentListing = (String) bd.get("UrgentListing");
            HourlyPay = (String) bd.get("HourlyPay");
            PayType = (String) bd.get("PayType");
            AdminComment = (String) bd.get("AdminComment");
            AdminRating = (String) bd.get("AdminRating");

            if (AdminComment.equals("") || AdminComment.equals("null") || AdminComment.equals(null) || AdminComment == null) {
                txt_comment.setText("-");
            } else {
                txt_comment.setText(AdminComment);
            }

            if (Appconstant.Category.equals("Applied")) {
                JobApplyID = (String) bd.get("JobApplyID");
            }

            /*Log.e("str_JobTitle", "" + str_JobTitle);
            Log.e("str_CategoryID", "" + str_CategoryID);
            Log.e("str_JobID", "" + str_JobID);
            Log.e("merchant", "" + merchant);
            Log.e("str_Timing", "" + str_Timing);
            Log.e("str_JobTime", "" + str_JobTime);
            Log.e("str_CreatedDate", "" + str_CreatedDate);
            Log.e("str_TotalPay", "" + str_TotalPay);
            Log.e("str_Agent", "" + str_Agent);
            Log.e("str_WorkingHours", "" + str_WorkingHours);
            Log.e("str_desc", "" + str_desc);
            Log.e("str_Requirement", "" + str_Requirement);
            Log.e("str_Arrival", "" + str_Arrival);
            Log.e("str_Lat", "" + str_Lat);
            Log.e("str_Long", "" + str_Long);
            Log.e("str_Miles", "" + str_Miles);
            Log.e("str_Rating", "" + str_Rating);
            Log.e("JobImage", "" + JobImage);
            Log.e("Background_Image","" + Background_Image);
            Log.e("str_Date","" + str_Date);*/
            Log.e("Rating","" + Rating);
            Log.e("MerchantID","" + MerchantID);

            if (IsFavorite.equals("") || IsFavorite.equals("null") || IsFavorite.equals(null) || IsFavorite == null || IsFavorite.equals("0")) {
                img_fav.setBackgroundResource(R.drawable.unlike);
            } else {
                img_fav.setBackgroundResource(R.drawable.like);
            }

            if (Rating.equals("") || Rating.equals("null") || Rating.equals(null) || Rating == null) {
            } else {
                ratingBarMain.setRating(Float.parseFloat(Rating));
            }

            if (merchant.equals("") || merchant.equals("null") || merchant.equals(null) || merchant == null) {
                txt_JobMarchant.setText("-");
            } else {
                txt_JobMarchant.setText(merchant);
            }
            if (str_JobTitle.equals("") || str_JobTitle.equals("null") || str_JobTitle.equals(null) || str_JobTitle == null) {
                txt_Jobtitle.setText("-");
            } else {
                txt_Jobtitle.setText(str_JobTitle);
            }
            if (str_JobTime.equals("") || str_JobTime.equals("null") || str_JobTime.equals(null) || str_JobTime == null) {
                txt_jobtime.setText("-");
            } else {
                txt_jobtime.setText(str_JobTime);
            }
            if (StartDate.equals("") || StartDate.equals("null") || StartDate.equals(null) || StartDate == null) {
                txt_date.setText("-");
            } else {
                txt_date.setText(DateUtils.getNewFormat(StartDate));
            }

            if (EndDate.equals("") || EndDate.equals("null") || EndDate.equals(null) || EndDate == null) {
                txt_dateEnd.setText("-");
            } else {
                txt_dateEnd.setText(DateUtils.getNewFormat(EndDate));
            }

            if (str_TotalPay.equals("") || str_TotalPay.equals("null") || str_TotalPay.equals(null) || str_TotalPay == null) {
                txt_pay.setText("-");
            } else {
                txt_pay.setText("$" + str_TotalPay);
            }
            if (str_desc.equals("") || str_desc.equals("null") || str_desc.equals(null) || str_desc == null) {
                txt_description.setText("-");
            } else {
                txt_description.setText(str_desc);
            }
            if (str_Requirement.equals("") || str_Requirement.equals("null") || str_Requirement.equals(null) || str_desc == null) {
                txt_requirement1.setText("-");
            } else {
                txt_requirement1.setText(str_Requirement);
            }

            if (str_Arrival.equals("") || str_Arrival.equals("null") || str_Arrival.equals(null) || str_Arrival == null) {
                txt_arrival.setText("-");
            } else {
                txt_arrival.setText(str_Arrival);
            }
            if (str_WorkingHours.equals("") || str_WorkingHours.equals("null") || str_WorkingHours.equals(null) || str_WorkingHours == null) {
                txt_WorkingHours.setText("-");
            } else {
                txt_WorkingHours.setText("Est.working hours: " + str_WorkingHours + "hrs");
            }

            if (Background_Image.equals("") || Background_Image.equals("null") || Background_Image.equals(null) || Background_Image == null) {
                Background.setBackgroundResource(R.color.green);
            } else {
                Glide.with(mContext).load(Background_Image).centerCrop().into(Background);
            }

            if (JobImage.equals("") || JobImage.equals("null") || JobImage.equals(null) || JobImage == null) {
            } else {
                Glide.with(mContext).load(JobImage).centerCrop().into(img_jobimage);
            }
            String path = JobImage;
            fileName = new File(path).getName();
        }
    }

    private void favorite_job(final String JobID, final String isFavorite) {
        String tag_string_req = "req";
        Progressbar1.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.favorite_job, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar1.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.favorite_job + json_main.toString());
                    if (json_main.getString("success").equalsIgnoreCase("True")) {
                        Toast.makeText(mContext, json_main.getString("Message"), Toast.LENGTH_LONG).show();
                        if (json_main.getString("isFavorite").equals("1")) {
                            img_fav.setBackgroundResource(R.drawable.like);
                        } else {
                            img_fav.setBackgroundResource(R.drawable.unlike);
                        }
                    } else {
                        Toast.makeText(mContext, json_main.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Progressbar1.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Progressbar1.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", pref.getString("userid", ""));
                params.put("JobID", JobID);
                params.put("isFavorite", isFavorite);
                Log.e("params", "" + Appconstant.favorite_job + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.favorite_job);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void add_my_job() {
        String tag_string_req = "req";
        Progressbar1.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.add_my_job, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar1.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.add_my_job + json_main.toString());
                    if (json_main.getString("success").equals("True")) {
                        Toast.makeText(job_description_activity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(job_description_activity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Progressbar1.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Progressbar1.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", pref.getString("userid", ""));
                params.put("JobID", str_JobID);
                Log.e("param", "" + Appconstant.add_my_job + " " + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.add_my_job);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void add_withdraw() {
        String tag_string_req = "req";
        Progressbar1.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.add_withdraw, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar1.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.add_withdraw + json_main.toString());
                    if (json_main.getString("success").equals("True")) {
                        Toast.makeText(job_description_activity.this, json_main.getString("success_msg"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(job_description_activity.this, json_main.getString("success_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Progressbar1.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Progressbar1.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("JobApplyID", JobApplyID);
                Log.e("param", "" + Appconstant.add_withdraw + " " + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.add_withdraw);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onResume() {
        /*if (Appconstant.Silde_click.equals("home")) {
            slide_home.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            slide_nearjob.setBackgroundColor(Color.parseColor("#000000"));
        }*/

        if (Appconstant.Category.equals("Applied")) {
            txt_apply.setTextColor(Color.parseColor("#ffffff"));
            txt_apply.setAllCaps(true);
            txt_apply.setText("Withdraw");
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


