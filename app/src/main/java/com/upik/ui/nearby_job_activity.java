package com.upik.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
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
import com.upik.adpter.InfoWindowAdapter;
import com.upik.model.CategoryModel;
import com.upik.model.JoblistModel;
import com.upik.model.NearByLocations;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;
import com.upik.utils.GPSTrackernew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class nearby_job_activity extends BaseActivity implements LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    public static LinearLayout linear_mainview, linearMapParent;
    private RelativeLayout calendar_view_LL, rel_ok, rel_cancel;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 10;
    final int REQUEST_LOCATION = 199;
    private LocationRequest mLocationRequest;
    public static SupportMapFragment mapFragment;
    private RecyclerView Recycler_Category, Recyler_nearbyjob;
    private List<NearByLocations> listNearByLocations = new ArrayList<>();
    private ProgressBar Progressbar;
    public static int pos = 0;
    public static CategoryAdpter categoryAdpter;
    private CalendarView calendarView;
    public static TextView txt_noJobs;
    private TextView txt_date;
    LatLng latLng;
    ArrayList<CategoryModel> arrayList_category = new ArrayList<>();
    ArrayList<JoblistModel> arrayList_job_list = new ArrayList<>();
    NearbyJobAdpter nearbyJobAdpter;
    String str_cal_date = "";
    String str_arr_date = "";
    Date cal_date = null;
    Date arr_date = null;
    SharedPreferences pref;
    GPSTrackernew gps;
    ImageView img_calender, img_click;
    String Latitude = "", Longitude = "", Catmapresponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.nearby_jobs_layout, frame_container);

        Log.e("nearby_job_activity", "nearby_job_activity");

        mContext = this;

        gps = new GPSTrackernew(mContext);
        Recyler_nearbyjob = findViewById(R.id.Recyler_nearbyjob);
        img_calender = findViewById(R.id.img_calender);
        linear_mainview = findViewById(R.id.linear_mainview);
        linearMapParent = findViewById(R.id.linearMapParent);
        Progressbar = findViewById(R.id.Progressbar);
        Recycler_Category = findViewById(R.id.Recycler_Category);
        txt_noJobs = findViewById(R.id.txt_noJobs);
        calendar_view_LL = findViewById(R.id.calendar_view_LL);
        img_click = findViewById(R.id.img_click);
        rel_ok = findViewById(R.id.rel_ok);
        rel_cancel = findViewById(R.id.rel_cancel);
        txt_date = findViewById(R.id.txt_date);

        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.setSelectionType(SelectionType.SINGLE);
        // calendarView.setSelectionType(SelectionType.MULTIPLE);
        // calendarView.setSelectionType(SelectionType.RANGE);
        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);

        Latitude = String.valueOf(gps.getLatitude());
        Longitude = String.valueOf(gps.getLongitude());
        Date d = new Date();
        CharSequence s = DateFormat.format("EEEE,MMMM dd, yyyy ", d.getTime());
        txt_date.setText(s);
        img_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar_view_LL.setVisibility(View.VISIBLE);
            }
        });

        rel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarView.getSelectedDates().size() > 0) {
                    Appconstant.filter = "1";
                    categoryAdpter.notifyDataSetChanged();
                }
                for (Calendar bean : calendarView.getSelectedDates()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Appconstant.dateString = formatter.format(bean.getTime());
                    Appconstant.arrayList.add(Appconstant.dateString);
                }
                calendar_view_LL.setVisibility(View.GONE);
                if (Appconstant.view.equals("map")) {
                } else {
                    nearbyJobAdpter.filter(Appconstant.dateString);
                }
            }
        });

        rel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar_view_LL.setVisibility(View.GONE);
                Appconstant.filter = "";
                calendarView.clearSelections();
                Appconstant.dateString = "";
                categoryAdpter.notifyDataSetChanged();
            }
        });

        img_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do note remove this click event
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

    public void category_list() {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        arrayList_category.clear();
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.category_list, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    arrayList_category.clear();
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.category_list + json_main.toString());
                    if (json_main.getString("success").equals("True")) {
                        JSONArray array_category_list = json_main.getJSONArray("category_list");
                        for (int i = 0; i < array_category_list.length(); i++) {
                            CategoryModel model = new CategoryModel();
                            model.setCategoryID(array_category_list.getJSONObject(i).getString("CategoryID"));
                            model.setCategoryName(array_category_list.getJSONObject(i).getString("CategoryName"));
                            model.setArray_job_list(array_category_list.getJSONObject(i).getString("job_list"));
                            arrayList_category.add(model);
                        }
                        if (arrayList_category.size() > 0) {
                            categoryAdpter = new CategoryAdpter(nearby_job_activity.this, arrayList_category);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(nearby_job_activity.this, LinearLayoutManager.HORIZONTAL, false);
                            Recycler_Category.setLayoutManager(linearLayoutManager);
                            Recycler_Category.setAdapter(categoryAdpter);
                            categoryAdpter.notifyDataSetChanged();
                        }
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
                Log.e("params", "" + Appconstant.category_list + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.category_list);
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
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adpter, parent, false);
            return new CategoryAdpter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CategoryAdpter.MyViewHolder holder, final int position) {
            final CategoryModel model = arrayList.get(position);

            if (model.getCategoryName().equals("") || model.getCategoryName().equals("null") || model.getCategoryName().equals(null)) {
            } else {
                holder.txt_category.setText(model.getCategoryName());
            }

            if (nearby_job_activity.pos == position) {
                holder.txt_category.setTextColor(Color.parseColor("#EA7F0E"));
                try {
                    Catmapresponse = model.getArray_job_list();
                    setUpMap(Catmapresponse);
                    JSONArray array_joblist;
                    arrayList_job_list.clear();
                    array_joblist = new JSONArray(model.getArray_job_list());
                    for (int i = 0; i < array_joblist.length(); i++) {
                        JoblistModel joblistModel = new JoblistModel();
                        joblistModel.setJobID(array_joblist.getJSONObject(i).getString("JobID"));
                        joblistModel.setCategoryID(array_joblist.getJSONObject(i).getString("CategoryID"));
                        joblistModel.setJobTitle(array_joblist.getJSONObject(i).getString("JobTitle"));
                        joblistModel.setIsFavorite(array_joblist.getJSONObject(i).getString("isFavorite"));
                        joblistModel.setMerchantID(array_joblist.getJSONObject(i).getString("MerchantID"));
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
                        joblistModel.setBackground(array_joblist.getJSONObject(i).getString("Background"));
                        joblistModel.setDate(array_joblist.getJSONObject(i).getString("Date"));
                        joblistModel.setStartDate(array_joblist.getJSONObject(i).getString("StartDate"));
                        joblistModel.setEndDate(array_joblist.getJSONObject(i).getString("EndDate"));
                        joblistModel.setUrgentListing(array_joblist.getJSONObject(i).getString("UrgentListing"));
                        joblistModel.setHourlyPay(array_joblist.getJSONObject(i).getString("HourlyPay"));
                        joblistModel.setPayType(array_joblist.getJSONObject(i).getString("PayType"));
                        joblistModel.setAdmin_Comment(array_joblist.getJSONObject(i).getString("Admin_Comment"));
                        joblistModel.setAdmin_Rating(array_joblist.getJSONObject(i).getString("Admin_Rating"));
                        arrayList_job_list.add(joblistModel);
                        // String date = array_joblist.getJSONObject(i).getString("CreatedDate");
                        String date = array_joblist.getJSONObject(i).getString("StartDate");
                        // Log.e("date","" + date);
                        String currentString1 = date;
                        String[] separated1 = currentString1.split("-");
                        separated1[0] = separated1[0].trim();
                        separated1[1] = separated1[1].trim();
                        separated1[2] = separated1[2].trim();
                        int year = Integer.parseInt(separated1[0]);
                        int month = Integer.parseInt(separated1[1]);
                        int day = Integer.parseInt(separated1[2]);
                        Set<Long> days = new TreeSet<>();
                        Calendar cal = Calendar.getInstance();
                        cal.set(cal.YEAR, year);
                        cal.set(cal.MONTH, (month - 1));
                        cal.set(cal.DAY_OF_MONTH, day);
                        days.add(cal.getTimeInMillis());
                        int textColor = Color.parseColor("#000000");
                        int selectedTextColor = Color.parseColor("#ffffff");
                        int disabledTextColor = Color.parseColor("#ff8000");
                        ConnectedDays connectedDays = new ConnectedDays(days, textColor, selectedTextColor, disabledTextColor);
                        calendarView.addConnectedDays(connectedDays);
                        calendarView.setSelectionType(SelectionType.SINGLE);
                        // calendarView.setSelectionType(SelectionType.MULTIPLE);
                        // calendarView.setSelectionType(SelectionType.RANGE);
                    }

                    if (Appconstant.view.equals("map")) {
                        linear_mainview.setVisibility(View.GONE);
                        linearMapParent.setVisibility(View.VISIBLE);
                        txt_noJobs.setVisibility(View.GONE);
                        Img_location.setImageResource(R.drawable.list);
                    } else {
                        Img_location.setImageResource(R.drawable.top_mapicon);
                        if (Appconstant.filter.equals("1")) {
                            if (arrayList_job_list.size() > 0) {
                                linear_mainview.setVisibility(View.VISIBLE);
                                linearMapParent.setVisibility(View.GONE);
                                txt_noJobs.setVisibility(View.GONE);
                                txt_date.setVisibility(View.VISIBLE);
                                nearbyJobAdpter = new NearbyJobAdpter(nearby_job_activity.this, arrayList_job_list);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(nearby_job_activity.this, LinearLayoutManager.VERTICAL, false);
                                Recyler_nearbyjob.setLayoutManager(linearLayoutManager);
                                Recyler_nearbyjob.setAdapter(nearbyJobAdpter);
                                nearbyJobAdpter.filter(Appconstant.dateString);
                            } else {
                                Recyler_nearbyjob.setVisibility(View.GONE);
                                txt_noJobs.setVisibility(View.VISIBLE);
                                linearMapParent.setVisibility(View.GONE);
                                txt_date.setVisibility(View.GONE);
                            }
                        } else {
                            if (arrayList_job_list.size() > 0) {
                                linear_mainview.setVisibility(View.VISIBLE);
                                linearMapParent.setVisibility(View.GONE);
                                txt_noJobs.setVisibility(View.GONE);
                                txt_date.setVisibility(View.GONE);
                                nearbyJobAdpter = new NearbyJobAdpter(nearby_job_activity.this, arrayList_job_list);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(nearby_job_activity.this, LinearLayoutManager.VERTICAL, false);
                                Recyler_nearbyjob.setLayoutManager(linearLayoutManager);
                                Recyler_nearbyjob.setAdapter(nearbyJobAdpter);
                                nearbyJobAdpter.notifyDataSetChanged();
                            } else {
                                Recyler_nearbyjob.setVisibility(View.GONE);
                                txt_noJobs.setVisibility(View.VISIBLE);
                                linearMapParent.setVisibility(View.GONE);
                                txt_date.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                holder.txt_category.setTextColor(Color.parseColor("#7f7f7f"));
            }

            holder.txt_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nearby_job_activity.pos = position;
                    categoryAdpter.notifyDataSetChanged();
                    linearMapParent.setVisibility(View.VISIBLE);
                    linear_mainview.setVisibility(View.GONE);
                    arrayList_job_list.clear();
                    Catmapresponse = "";
                    Catmapresponse = model.getArray_job_list();
                    try {
                        JSONArray array_joblist;
                        array_joblist = new JSONArray(model.getArray_job_list());
                        for (int i = 0; i < array_joblist.length(); i++) {
                            JoblistModel joblistModel = new JoblistModel();
                            joblistModel.setJobID(array_joblist.getJSONObject(i).getString("JobID"));
                            joblistModel.setCategoryID(array_joblist.getJSONObject(i).getString("CategoryID"));
                            joblistModel.setJobTitle(array_joblist.getJSONObject(i).getString("JobTitle"));
                            joblistModel.setIsFavorite(array_joblist.getJSONObject(i).getString("isFavorite"));
                            joblistModel.setMerchantID(array_joblist.getJSONObject(i).getString("MerchantID"));
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
                            joblistModel.setDate(array_joblist.getJSONObject(i).getString("Date"));
                            joblistModel.setBackground(array_joblist.getJSONObject(i).getString("Background"));
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
                            nearbyJobAdpter = new NearbyJobAdpter(nearby_job_activity.this, arrayList_job_list);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(nearby_job_activity.this, LinearLayoutManager.VERTICAL, false);
                            Recyler_nearbyjob.setLayoutManager(linearLayoutManager);
                            Recyler_nearbyjob.setAdapter(nearbyJobAdpter);
                        } else {
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
        private List<JoblistModel> arrayList = new ArrayList<JoblistModel>();
        private ArrayList<JoblistModel> arrayuser;
        LayoutInflater inflater;

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

        public NearbyJobAdpter(Context context, List<JoblistModel> list) {
            this.context = context;
            this.arrayList = list;
            inflater = LayoutInflater.from(context);
            this.arrayuser = new ArrayList<JoblistModel>();
            this.arrayuser.addAll(arrayList);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearbyjob_adpter, parent, false);
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

            if (model.getDate().equals("") || model.getDate().equals("null") || model.getDate().equals(null)) {
                holder.txt_date.setText("-");
            } else {
                holder.txt_date.setText(DateUtils.DateTime(model.getDate()));
            }

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

            if (model.getMiles().equals("") || model.getMiles().equals("null") || model.getMiles().equals(null)) {
                holder.txt_miles.setText("-");
            } else {
                holder.txt_miles.setText(model.getMiles() + " ml");
            }

            holder.img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Appconstant.isNetworkAvailable(mContext)) {
                        if (pref.getString("userid", "").equals("")) {
                            Toast.makeText(mContext, "Please login to select your favorite job.", Toast.LENGTH_LONG).show();
                        } else {
                            String IsFavorite = "";
                            if (model.getIsFavorite().equals("0")) {
                                IsFavorite = "1";
                            } else {
                                IsFavorite = "0";
                            }
                            favorite_job(model.getJobID(), IsFavorite);
                        }
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
                    Appconstant.Category = "";
                    Log.e("StartDate","" + model.getStartDate());
                    Log.e("EndDate","" + model.getEndDate());
                    Intent i = new Intent(mContext, job_description_activity.class);
                    i.putExtra("JobID", model.getJobID());
                    i.putExtra("CategoryID", model.getCategoryID());
                    i.putExtra("JobTitle", model.getJobTitle());
                    i.putExtra("MerchantID", model.getMerchantID());
                    i.putExtra("merchant", model.getMerchant());
                    i.putExtra("Timing", model.getTiming());
                    i.putExtra("JobTime", model.getJobTime());
                    i.putExtra("Agent", model.getAgent());
                    i.putExtra("CreatedDate", model.getCreatedDate());
                    i.putExtra("TotalPay", model.getTotalPay());
                    i.putExtra("WorkingHours", model.getWorkingHours());
                    i.putExtra("YourDescription", model.getYourDescription());
                    i.putExtra("Requirement", model.getRequirement());
                    i.putExtra("ArrivalInstuction", model.getArrivalInstuction());
                    i.putExtra("Lat", model.getLat());
                    i.putExtra("Long", model.getLong());
                    i.putExtra("Miles", model.getMiles());
                    i.putExtra("Rating", model.getRating());
                    i.putExtra("JobImage", model.getJobImage());
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
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            str_cal_date = charText;
            str_arr_date = "";
            arrayList.clear();
            try {
                cal_date = new SimpleDateFormat("yyyy-MM-dd").parse(str_cal_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (charText.length() == 0) {
                arrayList.addAll(arrayuser);
            } else {
                for (JoblistModel wp : arrayuser) {
                    // str_arr_date = wp.getCreatedDate();
                    str_arr_date = wp.getStartDate();
                    try {
                        arr_date = new SimpleDateFormat("yyyy-MM-dd").parse(str_arr_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (cal_date.equals(arr_date)) {
                        arrayList.add(wp);
                    }
                }
            }

            if (arrayList.size() > 0) {
                Recyler_nearbyjob.setVisibility(View.VISIBLE);
                txt_date.setVisibility(View.VISIBLE);
                txt_noJobs.setVisibility(View.GONE);
            } else {
                Recyler_nearbyjob.setVisibility(View.GONE);
                txt_date.setVisibility(View.GONE);
                txt_noJobs.setVisibility(View.VISIBLE);
            }
            nearbyJobAdpter.notifyDataSetChanged();
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
                    category_list();

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

    public void setUpMap(String Catmapresponse) {
        // Log.e("setUpMap", "setUpMap");
        try {
            if (googleMap != null) {
                googleMap.clear();
            }
            listNearByLocations.clear();
            JSONArray array_joblist;
            array_joblist = new JSONArray(Catmapresponse);
            for (int j = 0; j < array_joblist.length(); j++) {
                NearByLocations nearByLocations;
                nearByLocations = new NearByLocations(new LatLng(Double.parseDouble(array_joblist.getJSONObject(j).getString("Lat")),
                        Double.parseDouble(array_joblist.getJSONObject(j).getString("Long"))),
                        array_joblist.getJSONObject(j).getString("JobTitle"),
                        array_joblist.getJSONObject(j).getString("Timing"),
                        array_joblist.getJSONObject(j).getString("Agent"),
                        array_joblist.getJSONObject(j).getString("TotalPay"),
                        array_joblist.getJSONObject(j).getString("Lat"),
                        array_joblist.getJSONObject(j).getString("Long"),
                        array_joblist.getJSONObject(j).getString("JobID"),
                        array_joblist.getJSONObject(j).getString("CategoryID"),
                        array_joblist.getJSONObject(j).getString("JobTime"),
                        array_joblist.getJSONObject(j).getString("JobImage"),
                        array_joblist.getJSONObject(j).getString("WorkingHours"),
                        array_joblist.getJSONObject(j).getString("YourDescription"),
                        array_joblist.getJSONObject(j).getString("Requirement"),
                        array_joblist.getJSONObject(j).getString("ArrivalInstuction"),
                        array_joblist.getJSONObject(j).getString("CreatedDate"),
                        array_joblist.getJSONObject(j).getString("Miles"),
                        array_joblist.getJSONObject(j).getString("Rating"),
                        array_joblist.getJSONObject(j).getString("merchant"),
                        array_joblist.getJSONObject(j).getString("isFavorite"),
                        array_joblist.getJSONObject(j).getString("Background"),
                        array_joblist.getJSONObject(j).getString("Date"),
                        array_joblist.getJSONObject(j).getString("StartDate"),
                        array_joblist.getJSONObject(j).getString("EndDate"),
                        array_joblist.getJSONObject(j).getString("UrgentListing"),
                        array_joblist.getJSONObject(j).getString("HourlyPay"),
                        array_joblist.getJSONObject(j).getString("PayType"),
                        array_joblist.getJSONObject(j).getString("Admin_Comment"),
                        array_joblist.getJSONObject(j).getString("Admin_Rating"),
                        array_joblist.getJSONObject(j).getString("MerchantID"));
                listNearByLocations.add(nearByLocations);
            }

            initializeMap();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initializeMap() {
        try {
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
                            }
                        }
                    }
                } else
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //Log.e("onMapReady", "onMapReady");
        googleMap = map;
        googleMap.setMapType(map.MAP_TYPE_TERRAIN);
        map.getUiSettings().setMapToolbarEnabled(false);
        if (listNearByLocations != null && listNearByLocations.size() > 0) {
            latLng = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
            if (Double.parseDouble(Latitude) == 0.0 && Double.parseDouble(Longitude) == 0.0) {

            } else {
                for (int i = 0; i < listNearByLocations.size(); i++) {
                    if (Appconstant.filter.equals("1")) {
                        try {
                            cal_date = new SimpleDateFormat("yyyy-MM-dd").parse(Appconstant.dateString);
                            // str_arr_date = listNearByLocations.get(i).getCreatedDate();
                            str_arr_date = listNearByLocations.get(i).getStartDate();
                            arr_date = new SimpleDateFormat("yyyy-MM-dd").parse(str_arr_date);
                            /*if (cal_date.equals(arr_date)){
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                                        .position(listNearByLocations.get(i).getLatLng()));
                                marker.setTag(i);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(listNearByLocations.get(i).getLat()), Double.parseDouble(listNearByLocations.get(i).getLong())), 9.30F));
                            }*/

                            if (Appconstant.arrayList.contains(str_arr_date)) {
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                                        .position(listNearByLocations.get(i).getLatLng()));
                                marker.setTag(i);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(listNearByLocations.get(i).getLat()), Double.parseDouble(listNearByLocations.get(i).getLong())), 9.30F));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                                .position(listNearByLocations.get(i).getLatLng()));
                        marker.setTag(i);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(listNearByLocations.get(i).getLat()), Double.parseDouble(listNearByLocations.get(i).getLong())), 9.30F));
                    }
                }
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getTag() == null) {
                    } else {
                        int position = (int) (marker.getTag());
                        NearByLocations model = listNearByLocations.get(position);
                        InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(nearby_job_activity.this, model);
                        googleMap.setInfoWindowAdapter(infoWindowAdapter);
                    }
                    return false;
                }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Appconstant.Category = "";
                    int position = (int) (marker.getTag());
                    NearByLocations model = listNearByLocations.get(position);
                    Intent i = new Intent(mContext, job_description_activity.class);
                    i.putExtra("JobID", model.getJobID());
                    i.putExtra("CategoryID", model.getCategoryID());
                    i.putExtra("JobTitle", model.getJobTitle());
                    i.putExtra("merchant", model.getMerchant());
                    i.putExtra("MerchantID", model.getMerchantID());
                    i.putExtra("Timing", model.getTiming());
                    i.putExtra("JobTime", model.getJobTime());
                    i.putExtra("Agent", model.getAgent());
                    i.putExtra("CreatedDate", model.getCreatedDate());
                    i.putExtra("TotalPay", model.getTotalPay());
                    i.putExtra("WorkingHours", model.getWorkingHours());
                    i.putExtra("YourDescription", model.getYourDescription());
                    i.putExtra("Requirement", model.getRequirement());
                    i.putExtra("ArrivalInstuction", model.getArrivalInstuction());
                    i.putExtra("Lat", model.getLat());
                    i.putExtra("Long", model.getLong());
                    i.putExtra("Miles", model.getMiles());
                    i.putExtra("Rating", model.getRating());
                    i.putExtra("JobImage", model.getJobImage());
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
            });
        } else {
            LatLng latLng = new LatLng(45.50884, -73.58781);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 9.30F));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Log.e("onLocationChanged", "onLocationChanged");
        // LatLng latLng = new LatLng(Double.parseDouble(Latitude) ,Double.parseDouble(Longitude) );

        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());

        /*LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Longitude = String.valueOf(location.getLongitude());
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                .position(latLng));*/
        // LatLng latLng = new LatLng(45.50884, -73.58781);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(25000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (checkLocationPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, nearby_job_activity.this);
        } else {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public boolean checkLocationPermission() {
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
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            // Log.e("enableLoc onConnected", "enableLoc onConnected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            //  Log.d("Location error", "Location error " + connectionResult.getErrorCode());
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
                                status.startResolutionForResult(nearby_job_activity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        if (Appconstant.Silde_click.equals("home")) {
            slide_home.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            slide_nearjob.setBackgroundColor(Color.parseColor("#000000"));
        }
        if (Appconstant.isNetworkAvailable(nearby_job_activity.this)) {
            category_list();
        } else {
            Toast.makeText(nearby_job_activity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
        }
        super.onResume();
    }
}


