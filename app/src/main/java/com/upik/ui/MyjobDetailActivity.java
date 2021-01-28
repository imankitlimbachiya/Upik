package com.upik.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.adpter.FeedbackListAdapter;
import com.upik.model.FeedbackListModel;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/3/2019.
 */

public class MyjobDetailActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 10, REQUEST_PHONE_CALL = 1;
    final int REQUEST_LOCATION = 199;
    RecyclerView feedbackView;
    private LocationRequest mLocationRequest;
    private TextView txt_Jobtitle, txt_jobtime, txt_date, txt_pay, txt_description, txt_WorkingHours, txt_requirement1,
            txt_arrival, txt_dateEnd, txt_payHourly, txt_comment;
    private CircleImageView img_jobimage, img_jobimagebadge;
    private LinearLayout back, lin_msg, lin_Call, lin_sms, ratingLayout;
    private TextView Canceljob, txt_Refund;
    private ProgressBar Progressbar;
    private ImageView img_fav;
    SupportMapFragment mapFragment;
    Double lat, longg;
    SharedPreferences pref;
    RelativeLayout relativeHourPay;
    RatingBar ratingBarMain;
    String Latitude = "", Longitude = "", StartDate = "", EndDate = "", UrgentListing = "", HourlyPay = "", PayType = "";
    String str_JobTitle = "", JobID = "", str_JobTime = "", str_CreatedDate = "", str_TotalPay = "", AdminComment = "",
            str_WorkingHours = "", str_desc = "", str_Requirement = "", AdminRating = "",
            str_Arrival = "", JobImage = "", merchant = "", Phone = "", Email = "", JobApplyID = "", MerchantID = "",
            IsFavorite = "", Rating = "", Date = "";
    TextView what, req, arrivel;
    Dialog mDialog;
    ArrayList<FeedbackListModel> feedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myjob_detail_layout);

        Log.e("MyjobDetailActivity", "MyjobDetailActivity");

        mContext = this;

        what = findViewById(R.id.what);
        req = findViewById(R.id.req);
        arrivel = findViewById(R.id.arrivel);

        txt_Jobtitle = findViewById(R.id.txt_Jobtitle);
        txt_jobtime = findViewById(R.id.txt_jobtime);
        txt_date = findViewById(R.id.txt_date);
        txt_pay = findViewById(R.id.txt_pay);
        back = findViewById(R.id.back);
        txt_WorkingHours = findViewById(R.id.txt_WorkingHours);
        txt_description = findViewById(R.id.txt_description);
        txt_requirement1 = findViewById(R.id.txt_requirement1);
        txt_arrival = findViewById(R.id.txt_arrival);
        img_jobimage = findViewById(R.id.img_jobimage);
        img_jobimagebadge = findViewById(R.id.img_jobimagebadge);
        lin_msg = findViewById(R.id.lin_msg);
        lin_Call = findViewById(R.id.lin_Call);
        lin_sms = findViewById(R.id.lin_sms);
        Canceljob = findViewById(R.id.Canceljob);
        txt_Refund = findViewById(R.id.txt_Refund);
        Progressbar = findViewById(R.id.Progressbar);
        img_fav = findViewById(R.id.img_fav);
        ratingBarMain = findViewById(R.id.ratingBarMain);
        txt_dateEnd = findViewById(R.id.txt_dateEnd);
        txt_payHourly = findViewById(R.id.txt_payHourly);
        relativeHourPay = findViewById(R.id.relativeHourPay);
        txt_comment = findViewById(R.id.txt_comment);
        ratingLayout = findViewById(R.id.ratingLayout);

        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);

        if (pref.getString("Language", "").equals("en")) {
            // what.setText("fr");
            // req.setText("fr");
            // arrivel.setText("fr");
        } else if (pref.getString("Language", "").equals("fr")) {
            what.setText("Les candidats doivent");
            req.setText("Compétences requises");
            arrivel.setText("Instructions d’arrivée");
        }

        intentData();

        lin_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Phone));
                if (ActivityCompat.checkSelfPermission(MyjobDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyjobDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    return;
                }
                startActivity(callIntent);
            }
        });

        lin_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:" + Email));
                startActivity(i);
            }
        });

        lin_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + Phone));
                startActivity(sendIntent);
            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFeedbackListApi();
            }
        });

        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsFavorite.equals("1")) {
                    favorite_job(JobID, "0");
                } else {
                    favorite_job(JobID, "1");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Canceljob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Appconstant.Category.equals("Active")) {
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mContext);
                        builder1.setTitle(R.string.app_name);
                        builder1.setMessage("Are you sure want to cancel this job?");
                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Appconstant.pos = 3;
                                cancel_job();
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
                    if (ratingBarMain.getRating() == 0.0) {
                        setFeedbackApi();
                    } else {
                        Toast.makeText(mContext, "You're Given Feedback Already", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            enableLoc();
        }

        if (checkLocationPermission()) {
            buildGoogleApiClient();
        }
    }

    public void setFeedbackListApi() {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.feedback_list_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        feedbackView = (RecyclerView) mDialog.findViewById(R.id.feedbackView);
        final ProgressBar progress = (ProgressBar) mDialog.findViewById(R.id.progress);
        feedbackListApi(JobID, progress);
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
                                // String str_msg = json_main.getString("error_msg");
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

    public void setFeedbackApi() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.leave_feedback);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        final EditText edt_comment = (EditText) dialog.findViewById(R.id.edt_comment);
        final ProgressBar progressbar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        dialog.findViewById(R.id.txt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                if (edt_comment.getText().toString().equals("")) {
                    Toast.makeText(MyjobDetailActivity.this, "Please Enter Your Feedback", Toast.LENGTH_SHORT).show();
                } else if (rating == 0.0) {
                    Toast.makeText(MyjobDetailActivity.this, "Please Give Your Rating", Toast.LENGTH_SHORT).show();
                } else {
                    feedbackApi(edt_comment.getText().toString(), rating);
                }
            }

            private void feedbackApi(final String Comment, final Float Rating) {
                String tag_string_req = "req";
                progressbar.setVisibility(View.VISIBLE);
                final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.add_feedback, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressbar.setVisibility(View.GONE);
                                    JSONObject json_main = new JSONObject(response);
                                    Log.e("response", "" + Appconstant.add_feedback + json_main);
                                    if (json_main.getString("success").equalsIgnoreCase("true")) {
                                        String str_msg = json_main.getString("error_msg");
                                        Toast.makeText(mContext, str_msg, Toast.LENGTH_LONG).show();
                                        edt_comment.setText("");
                                        ratingBarMain.setRating(Float.parseFloat(json_main.getString("Rating")));
                                        dialog.cancel();
                                    } else {
                                        // Toast.makeText(LoginActivity.this, json_main.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    progressbar.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                },
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                progressbar.setVisibility(View.GONE);
                            }
                        }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("UserID", pref.getString("userid", ""));
                        params.put("JobID", JobID);
                        params.put("Comment", Comment);
                        params.put("Rating", String.valueOf(Rating));
                        params.put("MerchantID", MerchantID);
                        Log.e("params", "" + Appconstant.add_feedback + params);
                        return params;
                    }
                };
                strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.add_feedback);
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        });
        dialog.show();
    }

    public void intentData() {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            str_JobTitle = (String) bd.get("JobTitle");
            JobID = (String) bd.get("JobID");
            merchant = (String) bd.get("merchant");
            MerchantID = (String) bd.get("MerchantID");
            str_JobTime = (String) bd.get("JobTime");
            str_CreatedDate = (String) bd.get("CreatedDate");
            str_TotalPay = (String) bd.get("TotalPay");
            str_WorkingHours = (String) bd.get("WorkingHours");
            str_desc = (String) bd.get("YourDescription");
            str_Requirement = (String) bd.get("Requirement");
            str_Arrival = (String) bd.get("ArrivalInstuction");
            Phone = (String) bd.get("Phone");
            Email = (String) bd.get("Email");
            JobImage = (String) bd.get("JobImage");
            JobApplyID = (String) bd.get("JobApplyID");
            lat = Double.parseDouble((String) bd.get("Lat"));
            longg = Double.parseDouble((String) bd.get("long"));
            IsFavorite = (String) bd.get("IsFavorite");
            Rating = (String) bd.get("Rating");
            Date = (String) bd.get("Date");
            StartDate = (String) bd.get("StartDate");
            EndDate = (String) bd.get("EndDate");
            UrgentListing = (String) bd.get("UrgentListing");
            HourlyPay = (String) bd.get("HourlyPay");
            PayType = (String) bd.get("PayType");
            AdminComment = (String) bd.get("AdminComment");
            AdminRating = (String) bd.get("AdminRating");

            if (AdminComment.equals("") || AdminComment.equals("null") || AdminComment.equals(null) || AdminComment == null) {
                txt_comment.setVisibility(View.GONE);
            } else {
                txt_comment.setVisibility(View.VISIBLE);
                txt_comment.setText(AdminComment);
            }

            if (UrgentListing.equals("1")) {
                img_jobimagebadge.setVisibility(View.VISIBLE);
            } else {
                img_jobimagebadge.setVisibility(View.GONE);
            }

            if (JobImage.equals("") || JobImage.equals("null") || JobImage.equals(null) || JobImage == null) {

            } else {
                Glide.with(mContext).load(JobImage).centerCrop().into(img_jobimage);
            }

            if (Rating.equals("") || Rating.equals("null") || Rating.equals(null) || Rating == null) {
            } else {
                ratingBarMain.setRating(Float.parseFloat(Rating));
            }

            if (IsFavorite.equals("") || IsFavorite.equals("null") || IsFavorite.equals(null) || IsFavorite == null || IsFavorite.equals("0")) {
                img_fav.setBackgroundResource(R.drawable.unlike);
            } else {
                img_fav.setBackgroundResource(R.drawable.like);
            }

            if (str_TotalPay.equals("") || str_TotalPay.equals("null") || str_TotalPay.equals(null) || str_TotalPay == null) {
                txt_pay.setText("-");
            } else {
                txt_pay.setText("$" + str_TotalPay);
            }

            if (PayType.equals("0")) {
                relativeHourPay.setVisibility(View.VISIBLE);
                if (HourlyPay.equals("") || HourlyPay.equals("null") || HourlyPay.equals(null) || HourlyPay == null) {
                    txt_payHourly.setText("-");
                } else {
                    txt_payHourly.setText("$" + HourlyPay);
                }
            } else {
                relativeHourPay.setVisibility(View.GONE);
            }

            if (EndDate.equals("") || EndDate.equals("null") || EndDate.equals(null) || EndDate == null) {
                txt_dateEnd.setText("-");
            } else {
                txt_dateEnd.setText(DateUtils.getNewFormat(EndDate));
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
        }
        initilizeMap();
    }

    private void favorite_job(final String JobID, final String isFavorite) {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.favorite_job, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
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

    public void cancel_job() {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.cancel_job, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.cancel_job + json_main.toString());
                    if (json_main.getString("success").equals("True")) {
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
                params.put("JobApplyID", JobApplyID);
                Log.e("params", "" + Appconstant.cancel_job + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.cancel_job);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void initilizeMap() {
        try {
            Log.e("initilizeMap", "initilizeMap");
            mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment));
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.e("buildGoogleApiClient", "buildGoogleApiClient");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                            if (googleMap != null) {
                                googleMap.setMyLocationEnabled(true);
                                Log.e("MY_PERMISSIONS_REQUEST_LOCATION", "MY_PERMISSIONS_REQUEST_LOCATION");
                            }
                        }
                    }
                } else
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean hasGPSDevice(Context context) {
        Log.e("hasGPSDevice", "hasGPSDevice");
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public boolean checkLocationPermission() {
        Log.e("checkLocationPermission", "checkLocationPermission");
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Location Permission is necessary!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void enableLoc() {
        Log.e("enableLoc", "enableLoc");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.e("enableLoc onConnected", "enableLoc onConnected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MyjobDetailActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("onConnected", "onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(25000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (checkLocationPermission()) {
            // Log.e("HVLA", "### checkLocationPermission true");
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, MyjobDetailActivity.this);
        } else {
            //Log.e("HVLA", "### checkLocationPermission false");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // Log.e("onLocationChanged", "onLocationChanged");
        // LatLng latLng = new LatLng(Double.parseDouble(Latitude) ,Double.parseDouble(Longitude) );
        /* LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("getLatitude", "" + location.getLatitude());
        Log.e("getLongitude", "" + location.getLongitude());
        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                .position(latLng));*/
        LatLng latLng = new LatLng(45.50884, -73.58781);
        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
        /*Marker marker = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                .position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 10.0F));*/
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.e("onMapReady", "onMapReady");
        googleMap = map;
        googleMap.setMapType(map.MAP_TYPE_TERRAIN);
        map.getUiSettings().setMapToolbarEnabled(false);
        if (lat == null && longg == null) {
            LatLng latLng = new LatLng(45.50884, -73.58781);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 9.30F));
        } else {
            LatLng latLng = new LatLng(lat, longg);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .position(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(String.valueOf(lat)), Double.parseDouble(String.valueOf(longg))), 9.30F));
        }
    }

    @Override
    protected void onResume() {
        if (Appconstant.Category.equals("Cancelled")) {
            Canceljob.setVisibility(View.GONE);
            txt_Refund.setVisibility(View.GONE);
        } else if (Appconstant.Category.equals("Active")) {
            Canceljob.setText("Cancel Job");
        }
        super.onResume();
    }
}
