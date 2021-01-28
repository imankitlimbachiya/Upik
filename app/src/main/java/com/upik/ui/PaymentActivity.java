package com.upik.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.model.JoblistModel;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/3/2019.
 */

public class PaymentActivity extends BaseActivity {

    private Context mContext;
    private RecyclerView Recyler_nearbyjob;
    ArrayList<JoblistModel> arrayList_nearbyjob = new ArrayList<JoblistModel>();
    private ImageView img_startdate;
    String startDate = "";
    private ProgressBar Progressbar;
    public SharedPreferences pref;
    private TextView txt_noJobs, txt_total, txt_date;
    ShimmerFrameLayout shimmer_view_container;
    private LinearLayout Lin_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.payment_layout, frame_container);

        Log.e("PaymentActivity", "PaymentActivity");

        mContext = this;

        shimmer_view_container = findViewById(R.id.shimmer_layout);
        shimmer_view_container.setVisibility(View.VISIBLE);
        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
        txt_title.setText(R.string.payment);
        Img_location.setVisibility(View.GONE);
        Recyler_nearbyjob = findViewById(R.id.Recyler_nearbyjob);
        img_startdate = findViewById(R.id.img_startdate);
        Progressbar = findViewById(R.id.Progressbar);
        txt_noJobs = findViewById(R.id.txt_noJobs);
        txt_total = findViewById(R.id.txt_total);
        txt_date = findViewById(R.id.txt_date);
        Lin_main = findViewById(R.id.Lin_main);

        Date d = new Date();
        CharSequence s = DateFormat.format("EEEE,MMMM dd, yyyy ", d.getTime());
        txt_date.setText(s);
        String date = String.valueOf(s);
        startDate = DateUtils.getRecurringDayname(date);
        // img_startdate.setEnabled(false);
        img_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker();
            }
        });
    }

    public void complete_job(final String startDate) {
        String tag_string_req = "req";
        arrayList_nearbyjob.clear();
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.complete_job, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
                Lin_main.setVisibility(View.VISIBLE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    if (json_main.equals("") || json_main.equals("null") || json_main.equals(null) || json_main == null) {
                        Lin_main.setVisibility(View.GONE);
                    } else {
                        arrayList_nearbyjob.clear();
                        txt_noJobs.setVisibility(View.GONE);
                        Log.e("response", "" + Appconstant.complete_job + json_main.toString());
                        if (json_main.getString("success").equals("True")) {
                            if (json_main.getString("Total_Payment").equals("") || json_main.getString("Total_Payment").equals("null") || json_main.getString("Total_Payment").equals(null) || json_main.getString("Total_Payment") == null) {
                                txt_total.setText("$" + "0.0");
                            } else {
                                txt_total.setText("$" + json_main.getString("Total_Payment"));
                            }

                            JSONArray array_joblist = json_main.getJSONArray("Complate_Jobs");
                            for (int i = 0; i < array_joblist.length(); i++) {
                                JoblistModel joblistModel = new JoblistModel();
                                joblistModel.setJobID(array_joblist.getJSONObject(i).getString("JobID"));
                                joblistModel.setCategoryID(array_joblist.getJSONObject(i).getString("CategoryID"));
                                joblistModel.setJobTitle(array_joblist.getJSONObject(i).getString("JobTitle"));
                                joblistModel.setIsFavorite(array_joblist.getJSONObject(i).getString("isFavorite"));
                                joblistModel.setMerchant(array_joblist.getJSONObject(i).getString("merchant"));
                                joblistModel.setJobTime(array_joblist.getJSONObject(i).getString("JobTime"));
                                joblistModel.setJobImage(array_joblist.getJSONObject(i).getString("JobImage"));
                                joblistModel.setTotalPay(array_joblist.getJSONObject(i).getString("TotalPay"));
                                joblistModel.setWorkingHours(array_joblist.getJSONObject(i).getString("WorkingHours"));
                                joblistModel.setRequirement(array_joblist.getJSONObject(i).getString("Requirement"));
                                joblistModel.setYourDescription(array_joblist.getJSONObject(i).getString("YourDescription"));
                                joblistModel.setArrivalInstuction(array_joblist.getJSONObject(i).getString("ArrivalInstuction"));
                                joblistModel.setCreatedDate(array_joblist.getJSONObject(i).getString("CreatedDate"));
                                joblistModel.setLat(array_joblist.getJSONObject(i).getString("Lat"));
                                joblistModel.setLong(array_joblist.getJSONObject(i).getString("Long"));
                                joblistModel.setMiles(array_joblist.getJSONObject(i).getString("Miles"));
                                joblistModel.setRating(array_joblist.getJSONObject(i).getString("Rating"));
                                joblistModel.setTiming(array_joblist.getJSONObject(i).getString("Timing"));
                                joblistModel.setAgent(array_joblist.getJSONObject(i).getString("Agent"));
                                arrayList_nearbyjob.add(joblistModel);
                            }
                            if (arrayList_nearbyjob.size() > 0) {
                                txt_noJobs.setVisibility(View.GONE);
                                Recyler_nearbyjob.setVisibility(View.VISIBLE);
                                NearbyJobAdpter nearbyJobAdpter = new NearbyJobAdpter(mContext, arrayList_nearbyjob);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                                Recyler_nearbyjob.setLayoutManager(linearLayoutManager);
                                Recyler_nearbyjob.setAdapter(nearbyJobAdpter);
                                nearbyJobAdpter.notifyDataSetChanged();
                            } else {
                                txt_total.setText("$" + "0.0");
                                txt_noJobs.setVisibility(View.VISIBLE);
                                Recyler_nearbyjob.setVisibility(View.GONE);
                            }
                        } else {
                            txt_total.setText("$" + "0.0");
                            txt_noJobs.setVisibility(View.VISIBLE);
                            Recyler_nearbyjob.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    Progressbar.setVisibility(View.GONE);
                    txt_noJobs.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Progressbar.setVisibility(View.GONE);
                txt_noJobs.setVisibility(View.VISIBLE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", pref.getString("userid", ""));
                params.put("Date", startDate);
                Log.e("params", "" + Appconstant.complete_job + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.complete_job);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public class NearbyJobAdpter extends RecyclerView.Adapter<NearbyJobAdpter.MyViewHolder> {

        private Context context;
        private List<JoblistModel> arrayList = new ArrayList<JoblistModel>();
        private ArrayList<JoblistModel> arrayuser;
        LayoutInflater inflater;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView txt_Jobtitle, txt_job_desc, txt_date, txt_totalPay, txt_miles;
            private LinearLayout Linear_view;
            private CircleImageView img_jobimage;
            private ProgressBar Progressbar1;
            private ImageView img_fav;

            public MyViewHolder(View view) {
                super(view);

                txt_Jobtitle = view.findViewById(R.id.txt_Jobtitle);
                txt_job_desc = view.findViewById(R.id.txt_job_desc);
                txt_date = view.findViewById(R.id.txt_date);
                txt_totalPay = view.findViewById(R.id.txt_totalPay);
                Linear_view = view.findViewById(R.id.Linear_view);
                txt_miles = view.findViewById(R.id.txt_miles);
                img_jobimage = view.findViewById(R.id.img_jobimage);
                Progressbar1 = view.findViewById(R.id.Progressbar1);
                img_fav = view.findViewById(R.id.img_fav);
            }
        }

        public NearbyJobAdpter(Context context, List<JoblistModel> list) {
            this.context = context;
            this.arrayList = list;
            inflater = LayoutInflater.from(context);
            this.arrayuser = new ArrayList<JoblistModel>();
            this.arrayuser.addAll(arrayList);
        }

        @Override
        public NearbyJobAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearbyjob_adpter, parent, false);
            return new NearbyJobAdpter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final NearbyJobAdpter.MyViewHolder holder, int position) {
            final JoblistModel model = arrayList.get(position);

            if (model.getMerchant().equals("") || model.getMerchant().equals("null") || model.getMerchant().equals(null)) {
                holder.txt_Jobtitle.setText("-");
            } else {
                holder.txt_Jobtitle.setText(model.getMerchant());
            }
            if (model.getJobTitle().equals("") || model.getJobTitle().equals("null") || model.getJobTitle().equals(null)) {
                holder.txt_job_desc.setText("-");
            } else {
                holder.txt_job_desc.setText(model.getJobTitle());
            }
            if (model.getTotalPay().equals("") || model.getTotalPay().equals("null") || model.getTotalPay().equals(null)) {
                holder.txt_totalPay.setText("00");
            } else {
                holder.txt_totalPay.setText("$" + model.getTotalPay());
            }
            if (model.getCreatedDate().equals("") || model.getCreatedDate().equals("null") || model.getCreatedDate().equals(null)) {
                holder.txt_date.setText("-");
            } else {
                holder.txt_date.setText(DateUtils.DateTime(model.getCreatedDate()));
            }
            if (model.getIsFavorite().equals("0")) {
                holder.img_fav.setBackgroundResource(R.drawable.unlike);
            } else {
                holder.img_fav.setBackgroundResource(R.drawable.like);
            }
            if (model.getMiles().equals("") || model.getMiles().equals("null") || model.getMiles().equals(null)) {
                holder.txt_miles.setText("-");
            } else {
                holder.txt_miles.setText(model.getMiles() + " ml");
            }
            holder.img_fav.setVisibility(View.GONE);

            if (model.getJobImage().equals("") || model.getJobImage().equals("null") || model.getJobImage().equals(null) || model.getJobImage() == null) {

            } else {
                Glide.with(mContext)
                        .load(model.getJobImage())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.img_jobimage);
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void DateTimePicker() {
        final View dialogView = View.inflate(mContext, R.layout.datetimepickernew, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        dialogView.findViewById(R.id.rel_ok1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.GONE);
                // datePicker.setMaxDate(System.currentTimeMillis());
                Calendar c = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    c = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());
                }
                long time = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    time = c.getTimeInMillis();
                    Log.e("time", "" + time);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
                    Log.e("startDate", "" + startDate);
                }
                txt_date.setText(DateUtils.getRecurringDate(startDate));
                alertDialog.dismiss();
                complete_job(startDate);
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

    @Override
    protected void onResume() {
        slidePayment.setBackgroundColor(Color.parseColor("#000000"));
        shimmer_view_container.startShimmerAnimation();
        if (Appconstant.isNetworkAvailable(mContext)) {
            complete_job(startDate);
        } else {
            Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
        }
        super.onResume();
    }
}
