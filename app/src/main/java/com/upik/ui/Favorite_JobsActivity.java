package com.upik.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.upik.R;
import com.upik.VollySupport.AppController;
import com.upik.model.JoblistModel;
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
 * Created by Admin on 7/4/2019.
 */

public class Favorite_JobsActivity extends BaseActivity {

    private Context mContext;
    private RecyclerView Recyler_Favjob;
    FavJobAdpter favJobAdpter;
    ArrayList<JoblistModel> arrayList_job_list = new ArrayList<>();
    ProgressBar Progressbar;
    private TextView txt_noJobs;
    SharedPreferences pref;
    ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fav_jobs_layout, frame_container);

        Log.e("Favorite_JobsActivity", "Favorite_JobsActivity");

        mContext = this;
        shimmer_view_container = findViewById(R.id.shimmer_layout);
        shimmer_view_container.setVisibility(View.VISIBLE);
        txt_title.setText(R.string.Favorite);
        Img_location.setVisibility(View.GONE);
        Recyler_Favjob = findViewById(R.id.Recyler_Favjob);
        Progressbar = findViewById(R.id.Progressbar);
        txt_noJobs = findViewById(R.id.txt_noJobs);
        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
    }

    public void favorite_job_list() {
        String tag_string_req = "req";
        arrayList_job_list.clear();
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.favorite_job_list, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                txt_noJobs.setVisibility(View.GONE);
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
                try {
                    arrayList_job_list.clear();
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.favorite_job_list + json_main.toString());
                    if (json_main.getString("success").equals("True")) {
                        JSONArray array_Favorite_job_list = json_main.getJSONArray("Favorite_job_list");
                        for (int i = 0; i < array_Favorite_job_list.length(); i++) {
                            JoblistModel joblistModel = new JoblistModel();
                            joblistModel.setJobID(array_Favorite_job_list.getJSONObject(i).getString("JobID"));
                            joblistModel.setCategoryID(array_Favorite_job_list.getJSONObject(i).getString("CategoryID"));
                            joblistModel.setJobTitle(array_Favorite_job_list.getJSONObject(i).getString("JobTitle"));
                            joblistModel.setIsFavorite(array_Favorite_job_list.getJSONObject(i).getString("isFavorite"));
                            joblistModel.setBackground(array_Favorite_job_list.getJSONObject(i).getString("Background"));
                            joblistModel.setMerchantID(array_Favorite_job_list.getJSONObject(i).getString("MerchantID"));
                            joblistModel.setMerchant(array_Favorite_job_list.getJSONObject(i).getString("merchant"));
                            joblistModel.setJobTime(array_Favorite_job_list.getJSONObject(i).getString("JobTime"));
                            joblistModel.setJobImage(array_Favorite_job_list.getJSONObject(i).getString("JobImage"));
                            joblistModel.setTotalPay(array_Favorite_job_list.getJSONObject(i).getString("TotalPay"));
                            joblistModel.setWorkingHours(array_Favorite_job_list.getJSONObject(i).getString("WorkingHours"));
                            joblistModel.setRequirement(array_Favorite_job_list.getJSONObject(i).getString("Requirement"));
                            joblistModel.setYourDescription(array_Favorite_job_list.getJSONObject(i).getString("YourDescription"));
                            joblistModel.setArrivalInstuction(array_Favorite_job_list.getJSONObject(i).getString("ArrivalInstuction"));
                            joblistModel.setCreatedDate(array_Favorite_job_list.getJSONObject(i).getString("CreatedDate"));
                            joblistModel.setLat(array_Favorite_job_list.getJSONObject(i).getString("Lat"));
                            joblistModel.setLong(array_Favorite_job_list.getJSONObject(i).getString("Long"));
                            joblistModel.setMiles(array_Favorite_job_list.getJSONObject(i).getString("Miles"));
                            joblistModel.setRating(array_Favorite_job_list.getJSONObject(i).getString("Rating"));
                            joblistModel.setTiming(array_Favorite_job_list.getJSONObject(i).getString("Timing"));
                            joblistModel.setPhone(array_Favorite_job_list.getJSONObject(i).getString("Phone"));
                            joblistModel.setEmail(array_Favorite_job_list.getJSONObject(i).getString("Email"));
                            joblistModel.setJobApplyID(array_Favorite_job_list.getJSONObject(i).getString("JobApplyID"));
                            joblistModel.setStartDate(array_Favorite_job_list.getJSONObject(i).getString("StartDate"));
                            joblistModel.setEndDate(array_Favorite_job_list.getJSONObject(i).getString("EndDate"));
                            joblistModel.setDate(array_Favorite_job_list.getJSONObject(i).getString("Date"));
                            joblistModel.setHourlyPay(array_Favorite_job_list.getJSONObject(i).getString("HourlyPay"));
                            joblistModel.setPayType(array_Favorite_job_list.getJSONObject(i).getString("PayType"));
                            joblistModel.setAdmin_Comment(array_Favorite_job_list.getJSONObject(i).getString("Admin_Comment"));
                            joblistModel.setAdmin_Rating(array_Favorite_job_list.getJSONObject(i).getString("Admin_Rating"));
                            joblistModel.setUrgentListing(array_Favorite_job_list.getJSONObject(i).getString("UrgentListing"));
                            arrayList_job_list.add(joblistModel);
                        }
                        if (arrayList_job_list.size() > 0) {
                            Recyler_Favjob.setVisibility(View.VISIBLE);
                            txt_noJobs.setVisibility(View.GONE);
                            favJobAdpter = new FavJobAdpter(mContext, arrayList_job_list);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            Recyler_Favjob.setLayoutManager(linearLayoutManager);
                            Recyler_Favjob.setAdapter(favJobAdpter);
                            favJobAdpter.notifyDataSetChanged();
                        } else {
                            Recyler_Favjob.setVisibility(View.GONE);
                            txt_noJobs.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Recyler_Favjob.setVisibility(View.GONE);
                        txt_noJobs.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    txt_noJobs.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                txt_noJobs.setVisibility(View.VISIBLE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", pref.getString("userid", ""));
                Log.e("params", "" + Appconstant.favorite_job_list + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.favorite_job_list);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
                    } else {
                        Toast.makeText(mContext, json_main.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                    favorite_job_list();
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
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.favorite_job);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public class FavJobAdpter extends RecyclerView.Adapter<FavJobAdpter.MyViewHolder> {
        private Context context;
        private List<JoblistModel> arrayList;

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

        public FavJobAdpter(Context context, List<JoblistModel> listPastOrders) {
            this.context = context;
            this.arrayList = listPastOrders;
        }

        @Override
        public FavJobAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearbyjob_adpter, parent, false);

            return new FavJobAdpter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final FavJobAdpter.MyViewHolder holder, int position) {
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
            if (model.getMiles().equals("") || model.getMiles().equals("null") || model.getMiles().equals(null)) {
                holder.txt_miles.setText("-");
            } else {
                holder.txt_miles.setText(model.getMiles() + " ml");
            }

            if (model.getIsFavorite().equals("0")) {
                holder.img_fav.setBackgroundResource(R.drawable.unlike);
            } else {
                holder.img_fav.setBackgroundResource(R.drawable.like);
            }

            holder.img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        String IsFavorite = "";
                        if (model.getIsFavorite().equals("0")) {
                            IsFavorite = "1";
                        } else {
                            IsFavorite = "0";
                        }
                        favorite_job(model.getJobID(), IsFavorite);
                    } else {
                        Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            });

            if (model.getJobImage().equals("") || model.getJobImage().equals("null") || model.getJobImage().equals(null) || model.getJobImage() == null) {

            } else {
                Glide.with(mContext).load(model.getJobImage()).into(holder.img_jobimage);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Appconstant.Category.equals("Applied")) {
                        Intent i = new Intent(mContext, job_description_activity.class);
                        i.putExtra("JobID", model.getJobID());
                        i.putExtra("JobApplyID", model.getJobApplyID());
                        i.putExtra("JobTitle", model.getJobTitle());
                        i.putExtra("MerchantID", model.getMerchantID());
                        i.putExtra("merchant", model.getMerchant());
                        i.putExtra("JobTime", model.getJobTime());
                        i.putExtra("CreatedDate", model.getCreatedDate());
                        i.putExtra("TotalPay", model.getTotalPay());
                        i.putExtra("WorkingHours", model.getWorkingHours());
                        i.putExtra("YourDescription", model.getYourDescription());
                        i.putExtra("Requirement", model.getRequirement());
                        i.putExtra("ArrivalInstuction", model.getArrivalInstuction());
                        i.putExtra("Miles", model.getMiles());
                        i.putExtra("Rating", model.getRating());
                        i.putExtra("JobImage", model.getJobImage());
                        i.putExtra("Lat", model.getLat());
                        i.putExtra("long", model.getLong());
                        i.putExtra("Phone", model.getPhone());
                        i.putExtra("Email", model.getEmail());
                        i.putExtra("IsFavorite", model.getIsFavorite());
                        i.putExtra("Background", model.getBackground());
                        i.putExtra("Date", model.getDate());
                        i.putExtra("StartDate", model.getStartDate());
                        i.putExtra("EndDate", model.getEndDate());
                        i.putExtra("UrgentListing", model.getUrgentListing());
                        i.putExtra("HourlyPay", model.getHourlyPay());
                        i.putExtra("PayType", model.getPayType());
                        i.putExtra("AdminComment", model.getAdmin_Comment());
                        i.putExtra("AdminRating", model.getAdmin_Rating());
                        startActivity(i);
                    } else {
                        Intent i = new Intent(mContext, MyjobDetailActivity.class);
                        i.putExtra("JobID", model.getJobID());
                        i.putExtra("JobApplyID", model.getJobApplyID());
                        i.putExtra("JobTitle", model.getJobTitle());
                        i.putExtra("MerchantID", model.getMerchantID());
                        i.putExtra("merchant", model.getMerchant());
                        i.putExtra("JobTime", model.getJobTime());
                        i.putExtra("CreatedDate", model.getCreatedDate());
                        i.putExtra("TotalPay", model.getTotalPay());
                        i.putExtra("WorkingHours", model.getWorkingHours());
                        i.putExtra("YourDescription", model.getYourDescription());
                        i.putExtra("Requirement", model.getRequirement());
                        i.putExtra("ArrivalInstuction", model.getArrivalInstuction());
                        i.putExtra("Miles", model.getMiles());
                        i.putExtra("Rating", model.getRating());
                        i.putExtra("JobImage", model.getJobImage());
                        i.putExtra("Lat", model.getLat());
                        i.putExtra("long", model.getLong());
                        i.putExtra("Phone", model.getPhone());
                        i.putExtra("Email", model.getEmail());
                        i.putExtra("IsFavorite", model.getIsFavorite());
                        i.putExtra("Background", model.getBackground());
                        i.putExtra("Date", model.getDate());
                        i.putExtra("StartDate", model.getStartDate());
                        i.putExtra("EndDate", model.getEndDate());
                        i.putExtra("UrgentListing", model.getUrgentListing());
                        i.putExtra("HourlyPay", model.getHourlyPay());
                        i.putExtra("PayType", model.getPayType());
                        i.putExtra("AdminComment", model.getAdmin_Comment());
                        i.putExtra("AdminRating", model.getAdmin_Rating());
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    protected void onResume() {
        slide_favjob.setBackgroundColor(Color.parseColor("#000000"));
        shimmer_view_container.startShimmerAnimation();
        if (Appconstant.isNetworkAvailable(mContext)) {
            favorite_job_list();
        } else {
            Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
        }
        super.onResume();
    }
}
