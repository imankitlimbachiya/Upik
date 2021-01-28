package com.upik.ui;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
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
import com.upik.model.CategoryModel;
import com.upik.model.JoblistModel;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 5/3/2019.
 */

public class MyJobActivity extends BaseActivity {

    private Context mContext;
    private RecyclerView Recyler_nearbyjob, Recycler_Category;
    ArrayList<JoblistModel> arrayList_job_list = new ArrayList<>();
    ArrayList<CategoryModel> arrayList_category = new ArrayList<>();
    CategoryAdpter categoryAdpter;
    NearbyJobAdpter nearbyJobAdpter;
    ProgressBar Progressbar;
    SharedPreferences pref;
    String Catmapresponse = "";
    private TextView txt_noJobs;
    View view_line;
    TextView txt_pending, txt_active, txt_completed, txt_cancelled;
    LinearLayout lin_staticTab;
    ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.my_jobs_layout, frame_container);

        Log.e("MyJobActivity", "MyJobActivity");

        mContext = this;
        shimmer_view_container = findViewById(R.id.shimmer_layout);
        shimmer_view_container.setVisibility(View.VISIBLE);

        Img_location.setVisibility(View.GONE);
        Recyler_nearbyjob = findViewById(R.id.Recyler_nearbyjob);
        Recycler_Category = findViewById(R.id.Recycler_Category);
        txt_pending = findViewById(R.id.txt_pending);
        txt_active = findViewById(R.id.txt_active);
        txt_completed = findViewById(R.id.txt_completed);
        txt_cancelled = findViewById(R.id.txt_cancelled);
        lin_staticTab = findViewById(R.id.lin_staticTab);
        txt_noJobs = findViewById(R.id.txt_noJobs);
        view_line = findViewById(R.id.view_line);
        Progressbar = findViewById(R.id.Progressbar);
        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);

        if (pref.getString("Language", "").equals("en")) {
            txt_title.setText(R.string.myjobs);
            txt_pending.setText("Applied");
            txt_completed.setText("Completed");
        } else if (pref.getString("Language", "").equals("fr")) {
            txt_title.setText("Favoris");
            txt_pending.setText("Postuler");
            txt_completed.setText("Complété");
        }

        txt_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_pending.setTextColor(Color.parseColor("#EA7F0E"));
                txt_active.setTextColor(Color.parseColor("#7f7f7f"));
                txt_completed.setTextColor(Color.parseColor("#7f7f7f"));
                txt_cancelled.setTextColor(Color.parseColor("#7f7f7f"));
                if (Appconstant.isNetworkAvailable(mContext)) {
                } else {
                    Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
        txt_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_pending.setTextColor(Color.parseColor("#7f7f7f"));
                txt_active.setTextColor(Color.parseColor("#EA7F0E"));
                txt_completed.setTextColor(Color.parseColor("#7f7f7f"));
                txt_cancelled.setTextColor(Color.parseColor("#7f7f7f"));
                if (Appconstant.isNetworkAvailable(mContext)) {
                } else {
                    Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
        txt_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_pending.setTextColor(Color.parseColor("#7f7f7f"));
                txt_active.setTextColor(Color.parseColor("#7f7f7f"));
                txt_completed.setTextColor(Color.parseColor("#EA7F0E"));
                txt_cancelled.setTextColor(Color.parseColor("#7f7f7f"));
                if (Appconstant.isNetworkAvailable(mContext)) {
                } else {
                    Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
        txt_cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_pending.setTextColor(Color.parseColor("#7f7f7f"));
                txt_active.setTextColor(Color.parseColor("#7f7f7f"));
                txt_completed.setTextColor(Color.parseColor("#7f7f7f"));
                txt_cancelled.setTextColor(Color.parseColor("#EA7F0E"));
                if (Appconstant.isNetworkAvailable(mContext)) {
                } else {
                    Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Myjob_list() {
        String tag_string_req = "req";
        lin_staticTab.setVisibility(View.GONE);
        arrayList_category.clear();
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.Myjob_list, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                lin_staticTab.setVisibility(View.VISIBLE);
                txt_noJobs.setVisibility(View.GONE);
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
                try {
                    arrayList_category.clear();
                    JSONObject json_main = new JSONObject(response);
                    if (json_main.equals("") || json_main.equals("null") || json_main.equals(null) || json_main == null) {
                        lin_staticTab.setVisibility(View.VISIBLE);
                        txt_noJobs.setVisibility(View.VISIBLE);
                    } else {
                        Log.e("response", "" + Appconstant.Myjob_list + json_main.toString());
                        if (json_main.getString("success").equals("True")) {
                            JSONArray my_jobs = json_main.getJSONArray("my_jobs");
                            for (int i = 0; i < my_jobs.length(); i++) {
                                CategoryModel model = new CategoryModel();
                                model.setCategoryName(my_jobs.getJSONObject(i).getString("Category"));
                                model.setArray_job_list(my_jobs.getJSONObject(i).getString("job_list"));
                                arrayList_category.add(model);
                            }
                            if (arrayList_category.size() > 0) {
                                lin_staticTab.setVisibility(View.GONE);
                                txt_noJobs.setVisibility(View.GONE);
                                Recycler_Category.setVisibility(View.VISIBLE);
                                categoryAdpter = new CategoryAdpter(MyJobActivity.this, arrayList_category);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyJobActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                Recycler_Category.setLayoutManager(linearLayoutManager);
                                Recycler_Category.setAdapter(categoryAdpter);
                                categoryAdpter.notifyDataSetChanged();
                                view_line.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Recyler_nearbyjob.setVisibility(View.GONE);
                            Recycler_Category.setVisibility(View.GONE);
                            view_line.setVisibility(View.GONE);
                            lin_staticTab.setVisibility(View.VISIBLE);
                            txt_noJobs.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    Progressbar.setVisibility(View.GONE);
                    lin_staticTab.setVisibility(View.VISIBLE);
                    txt_noJobs.setVisibility(View.VISIBLE);
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmerAnimation();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Progressbar.setVisibility(View.GONE);
                lin_staticTab.setVisibility(View.VISIBLE);
                txt_noJobs.setVisibility(View.VISIBLE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", pref.getString("userid", ""));
                Log.e("params", "" + Appconstant.Myjob_list + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.Myjob_list);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public class CategoryAdpter extends RecyclerView.Adapter<CategoryAdpter.MyViewHolder> {

        private Context context;
        private List<CategoryModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView txt_category;

            public MyViewHolder(View view) {
                super(view);

                txt_category = view.findViewById(R.id.txt_category);
            }
        }

        public CategoryAdpter(Context context, List<CategoryModel> listPastOrders) {
            this.context = context;
            this.arrayList = listPastOrders;
        }

        @Override
        public CategoryAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adpter, parent, false);
            return new CategoryAdpter.MyViewHolder(itemView);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final CategoryAdpter.MyViewHolder holder, final int position) {
            final CategoryModel model = arrayList.get(position);

            if (model.getCategoryName().equals("") || model.getCategoryName().equals("null") || model.getCategoryName().equals(null)) {
            } else {
                if (pref.getString("Language", "").equals("en")) {
                    holder.txt_category.setText(model.getCategoryName());
                } else if (pref.getString("Language", "").equals("fr")) {
                    String CategoryName = model.getCategoryName();
                    if (CategoryName.equals("Applied")) {
                        holder.txt_category.setText("Postuler");
                    } else if (model.getCategoryName().equals("Completed")) {
                        holder.txt_category.setText("Complété");
                    } else {
                        holder.txt_category.setText(CategoryName);
                    }
                }
            }

            try {
                if (Appconstant.pos == position) {
                    Recycler_Category.setVisibility(View.VISIBLE);
                    holder.txt_category.setText(model.getCategoryName());
                    holder.txt_category.setTextColor(Color.parseColor("#EA7F0E"));
                    Appconstant.Category = model.getCategoryName();
                    Catmapresponse = model.getArray_job_list();
                    JSONArray array_jobList;
                    arrayList_job_list.clear();
                    array_jobList = new JSONArray(model.getArray_job_list());
                    for (int i = 0; i < array_jobList.length(); i++) {
                        JoblistModel joblistModel = new JoblistModel();
                        joblistModel.setJobID(array_jobList.getJSONObject(i).getString("JobID"));
                        joblistModel.setJobApplyID(array_jobList.getJSONObject(i).getString("JobApplyID"));
                        joblistModel.setCategoryID(array_jobList.getJSONObject(i).getString("CategoryID"));
                        joblistModel.setJobTitle(array_jobList.getJSONObject(i).getString("JobTitle"));
                        joblistModel.setIsFavorite(array_jobList.getJSONObject(i).getString("isFavorite"));
                        joblistModel.setMerchant(array_jobList.getJSONObject(i).getString("merchant"));
                        joblistModel.setMerchantID(array_jobList.getJSONObject(i).getString("MerchantID"));
                        joblistModel.setJobTime(array_jobList.getJSONObject(i).getString("JobTime"));
                        joblistModel.setJobImage(array_jobList.getJSONObject(i).getString("JobImage"));
                        joblistModel.setTotalPay(array_jobList.getJSONObject(i).getString("TotalPay"));
                        joblistModel.setWorkingHours(array_jobList.getJSONObject(i).getString("WorkingHours"));
                        joblistModel.setRequirement(array_jobList.getJSONObject(i).getString("Requirement"));
                        joblistModel.setYourDescription(array_jobList.getJSONObject(i).getString("YourDescription"));
                        joblistModel.setArrivalInstuction(array_jobList.getJSONObject(i).getString("ArrivalInstuction"));
                        joblistModel.setCreatedDate(array_jobList.getJSONObject(i).getString("CreatedDate"));
                        joblistModel.setLat(array_jobList.getJSONObject(i).getString("Lat"));
                        joblistModel.setLong(array_jobList.getJSONObject(i).getString("Long"));
                        joblistModel.setMiles(array_jobList.getJSONObject(i).getString("Miles"));
                        joblistModel.setRating(array_jobList.getJSONObject(i).getString("Rating"));
                        joblistModel.setTiming(array_jobList.getJSONObject(i).getString("Timing"));
                        joblistModel.setPhone(array_jobList.getJSONObject(i).getString("Phone"));
                        joblistModel.setEmail(array_jobList.getJSONObject(i).getString("Email"));
                        joblistModel.setDate(array_jobList.getJSONObject(i).getString("Date"));
                        joblistModel.setBackground(array_jobList.getJSONObject(i).getString("Background"));
                        // joblistModel.setRating(array_joblist.getJSONObject(i).getString("Rating"));
                        joblistModel.setStartDate(array_jobList.getJSONObject(i).getString("StartDate"));
                        joblistModel.setEndDate(array_jobList.getJSONObject(i).getString("EndDate"));
                        joblistModel.setUrgentListing(array_jobList.getJSONObject(i).getString("UrgentListing"));
                        joblistModel.setHourlyPay(array_jobList.getJSONObject(i).getString("HourlyPay"));
                        joblistModel.setPayType(array_jobList.getJSONObject(i).getString("PayType"));
                        joblistModel.setAdmin_Comment(array_jobList.getJSONObject(i).getString("Admin_Comment"));
                        joblistModel.setAdmin_Rating(array_jobList.getJSONObject(i).getString("Admin_Rating"));
                        arrayList_job_list.add(joblistModel);
                    }
                    if (arrayList_job_list.size() > 0) {
                        Recycler_Category.setVisibility(View.VISIBLE);
                        Recyler_nearbyjob.setVisibility(View.VISIBLE);
                        txt_noJobs.setVisibility(View.GONE);
                        nearbyJobAdpter = new NearbyJobAdpter(mContext, arrayList_job_list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        Recyler_nearbyjob.setLayoutManager(linearLayoutManager);
                        Recyler_nearbyjob.setAdapter(nearbyJobAdpter);
                        nearbyJobAdpter.notifyDataSetChanged();
                    } else {
                        Recycler_Category.setVisibility(View.VISIBLE);
                        Recyler_nearbyjob.setVisibility(View.GONE);
                        txt_noJobs.setVisibility(View.VISIBLE);
                        if (Appconstant.Category.equals("Applied")) {
                            txt_noJobs.setText("No more jobs..!");
                        } else if (Appconstant.Category.equals("Active")) {
                            txt_noJobs.setText("No more active jobs..!");
                        } else if (Appconstant.Category.equals("Completed")) {
                            txt_noJobs.setText("No more completed jobs..!");
                        } else if (Appconstant.Category.equals("Cancelled")) {
                            txt_noJobs.setText("No more cancelled jobs..!");
                        }
                    }
                } else {
                    Recycler_Category.setVisibility(View.VISIBLE);
                    holder.txt_category.setText(model.getCategoryName());
                    holder.txt_category.setTextColor(Color.parseColor("#7f7f7f"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            holder.txt_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appconstant.pos = position;
                    Appconstant.Category = model.getCategoryName();
                    categoryAdpter.notifyDataSetChanged();
                    arrayList_job_list.clear();
                    Catmapresponse = "";
                    Catmapresponse = model.getArray_job_list();
                    try {
                        JSONArray array_joblist;
                        array_joblist = new JSONArray(model.getArray_job_list());
                        for (int i = 0; i < array_joblist.length(); i++) {
                            JoblistModel joblistModel = new JoblistModel();
                            joblistModel.setJobID(array_joblist.getJSONObject(i).getString("JobID"));
                            joblistModel.setJobApplyID(array_joblist.getJSONObject(i).getString("JobApplyID"));
                            joblistModel.setCategoryID(array_joblist.getJSONObject(i).getString("CategoryID"));
                            joblistModel.setJobTitle(array_joblist.getJSONObject(i).getString("JobTitle"));
                            joblistModel.setIsFavorite(array_joblist.getJSONObject(i).getString("isFavorite"));
                            joblistModel.setMerchant(array_joblist.getJSONObject(i).getString("merchant"));
                            joblistModel.setMerchantID(array_joblist.getJSONObject(i).getString("MerchantID"));
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
                            joblistModel.setPhone(array_joblist.getJSONObject(i).getString("Phone"));
                            joblistModel.setEmail(array_joblist.getJSONObject(i).getString("Email"));
                            joblistModel.setDate(array_joblist.getJSONObject(i).getString("Date"));
                            joblistModel.setBackground(array_joblist.getJSONObject(i).getString("Background"));
                            joblistModel.setRating(array_joblist.getJSONObject(i).getString("Rating"));
                            joblistModel.setStartDate(array_joblist.getJSONObject(i).getString("StartDate"));
                            joblistModel.setEndDate(array_joblist.getJSONObject(i).getString("EndDate"));
                            joblistModel.setUrgentListing(array_joblist.getJSONObject(i).getString("UrgentListing"));
                            joblistModel.setHourlyPay(array_joblist.getJSONObject(i).getString("HourlyPay"));
                            joblistModel.setPayType(array_joblist.getJSONObject(i).getString("PayType"));
                            joblistModel.setAdmin_Comment(array_joblist.getJSONObject(i).getString("Admin_Comment"));
                            joblistModel.setAdmin_Rating(array_joblist.getJSONObject(i).getString("Admin_Rating"));
                            arrayList_job_list.add(joblistModel);
                        }
                        if (arrayList_job_list.size() > 0) {
                            Recyler_nearbyjob.setVisibility(View.VISIBLE);
                            txt_noJobs.setVisibility(View.GONE);
                            nearbyJobAdpter = new NearbyJobAdpter(mContext, arrayList_job_list);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            Recyler_nearbyjob.setLayoutManager(linearLayoutManager);
                            Recyler_nearbyjob.setAdapter(nearbyJobAdpter);
                            nearbyJobAdpter.notifyDataSetChanged();
                        } else {
                            if (Appconstant.Category.equals("Applied")) {
                                txt_noJobs.setText("No more applied jobs..!");
                            } else if (Appconstant.Category.equals("Active")) {
                                txt_noJobs.setText("No more active jobs..!");
                            } else if (Appconstant.Category.equals("Completed")) {
                                txt_noJobs.setText("No more completed jobs..!");
                            } else if (Appconstant.Category.equals("Cancelled")) {
                                txt_noJobs.setText("No more cancelled jobs..!");
                            }
                            Recyler_nearbyjob.setVisibility(View.GONE);
                            txt_noJobs.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public class NearbyJobAdpter extends RecyclerView.Adapter<NearbyJobAdpter.MyViewHolder> {

        private Context context;
        private List<JoblistModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView txt_Jobtitle, txt_job_desc, txt_date, txt_totalPay, txt_miles;
            private LinearLayout Linear_view;
            private CircleImageView img_jobimage, img_jobimagebadge;
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
                img_jobimagebadge = view.findViewById(R.id.img_jobimagebadge);
            }
        }

        public NearbyJobAdpter(Context context, List<JoblistModel> listPastOrders) {
            this.context = context;
            this.arrayList = listPastOrders;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearbyjob_adpter, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
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

            holder.img_fav.setVisibility(View.GONE);

            String UrgentListing = model.getUrgentListing();
            if (UrgentListing.equals("1")) {
                holder.img_jobimagebadge.setVisibility(View.VISIBLE);
            } else {
                holder.img_jobimagebadge.setVisibility(View.GONE);
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
                Glide.with(mContext).load(model.getJobImage()).centerCrop().into(holder.img_jobimage);
            }

            holder.Linear_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Appconstant.Category.equals("Applied")) {
                        Intent i = new Intent(mContext, job_description_activity.class);
                        i.putExtra("JobID", model.getJobID());
                        i.putExtra("JobApplyID", model.getJobApplyID());
                        i.putExtra("JobTitle", model.getJobTitle());
                        i.putExtra("merchant", model.getMerchant());
                        i.putExtra("MerchantID", model.getMerchantID());
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
                        i.putExtra("merchant", model.getMerchant());
                        i.putExtra("MerchantID", model.getMerchantID());
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
                    Myjob_list();
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

    @Override
    protected void onResume() {
        slide_Myjob.setBackgroundColor(Color.parseColor("#000000"));
        shimmer_view_container.startShimmerAnimation();
        if (Appconstant.isNetworkAvailable(mContext)) {
            Myjob_list();
        } else {
            Toast.makeText(mContext, R.string.check_connection, Toast.LENGTH_LONG).show();
            lin_staticTab.setVisibility(View.VISIBLE);
            txt_noJobs.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
