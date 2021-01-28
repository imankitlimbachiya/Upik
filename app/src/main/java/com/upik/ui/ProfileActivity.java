package com.upik.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.upik.model.WorkModel;
import com.upik.utils.Appconstant;
import com.upik.utils.DateUtils;
import com.upik.utils.Utility;
import com.upik.utils.WebAPIRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends BaseActivity {

    private Context mContext;
    // ImageView img_editWorkexp, img_editBio, img_ediUsername, img_ediEmailPhone, img_edicityCountry, img_edilan;
    SharedPreferences pref;
    private ProgressBar Progressbar1, Progressbar;
    private CircleImageView img_profile;
    private EditText txt_usename, txt_email, txt_phone, txt_city, txt_country, txt_biography, txt_language;
    private TextView txt_workExp;
    Bitmap mBitmap;
    String mPath = "";
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;
    File photo;
    private ArrayList<Bitmap> mTempBitmapArray = new ArrayList<Bitmap>();
    String userName = "", Email = "", Phone = "", City = "", Country = "", Biography = "", Languages = "", WorkExp = "";
    ProgressDialog progressDialog;
    RecyclerView Recyler_worklist;
    JobWorklistAdpter jobWorklistAdpter;
    ArrayList<WorkModel> arrayWorklist = new ArrayList<>();
    private TextView txt_nameSave, txt_phoneSave, txt_countrySave, txt_BioSave, txt_LanSave, txt_noJobs;
    TextView lbl_bio, lbl_language, lbl_work, txt_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frame_container);

        Log.e("ProfileActivity", "ProfileActivity");

        mContext = this;

        pref = getSharedPreferences("Login_Data", MODE_PRIVATE);

        lbl_bio = findViewById(R.id.lbl_bio);
        lbl_language = findViewById(R.id.lbl_language);
        lbl_work = findViewById(R.id.lbl_work);
        Img_location.setVisibility(View.GONE);
        // img_editWorkexp = findViewById(R.id.img_editWorkexp);
        txt_Save = findViewById(R.id.txt_Save);
        // img_ediUsername = findViewById(R.id.img_ediUsername);
        // img_ediEmailPhone = findViewById(R.id.img_ediEmailPhone);
        // img_edicityCountry = findViewById(R.id.img_edicityCountry);
        //  img_edilan = findViewById(R.id.img_edilan);
        Progressbar = findViewById(R.id.Progressbar);
        Progressbar1 = findViewById(R.id.Progressbar1);
        txt_usename = findViewById(R.id.txt_usename);
        txt_email = findViewById(R.id.txt_email);
        txt_phone = findViewById(R.id.txt_phone);
        txt_city = findViewById(R.id.txt_city);
        txt_country = findViewById(R.id.txt_country);
        txt_biography = findViewById(R.id.txt_biography);
        txt_language = findViewById(R.id.txt_language);
        txt_phoneSave = findViewById(R.id.txt_phoneSave);
        txt_countrySave = findViewById(R.id.txt_countrySave);
        txt_BioSave = findViewById(R.id.txt_BioSave);
        txt_LanSave = findViewById(R.id.txt_LanSave);
        txt_noJobs = findViewById(R.id.txt_noJobs);
        // txt_workExp = findViewById(R.id.txt_workExp);
        txt_nameSave = findViewById(R.id.txt_nameSave);
        img_profile = findViewById(R.id.img_profile);
        //   img_editBio = findViewById(R.id.img_editBio);
        Recyler_worklist = findViewById(R.id.Recyler_worklist);
        //  txt_usename.setEnabled(false);
        txt_email.setEnabled(false);
        // txt_phone.setEnabled(false);
        // txt_city.setEnabled(false);
        txt_country.setEnabled(false);
        // txt_biography.setEnabled(false);
        // txt_language.setEnabled(false);
        // txt_workExp.setEnabled(false);

        if (pref.getString("Language", "").equals("en")) {
            lbl_bio.setText("Biography");
            lbl_language.setText("Languages");
            lbl_work.setText("Work experience");
            txt_title.setText(R.string.profile);
            // txt_email.setHint("");
        } else if (pref.getString("Language", "").equals("fr")) {
            lbl_bio.setText("Biographie");
            lbl_language.setText("Languages");
            lbl_work.setText("Expérience professionnelle");
            txt_title.setText("Profil");
            // txt_email.setHint("");
        }

        lbl_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ProfileActivity.this, WorkExperienceActivity.class),200);
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });

        /*img_ediUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_usename.setEnabled(true);
                txt_nameSave.setVisibility(View.VISIBLE);
                img_ediUsername.setVisibility(View.GONE);
                txt_usename.setSelection(txt_usename.getText().length());
            }
        });*/

        txt_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = txt_usename.getText().toString().trim();
                Email = txt_email.getText().toString().trim();
                Phone = txt_phone.getText().toString().trim();
                City = txt_city.getText().toString().trim();
                Country = txt_country.getText().toString().trim();
                Biography = txt_biography.getText().toString().trim();
                Languages = txt_language.getText().toString().trim();
                if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        UpdateProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        new update_profile().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        txt_nameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_usename.setEnabled(false);
                txt_nameSave.setVisibility(View.GONE);
                txt_phoneSave.setVisibility(View.GONE);
                txt_countrySave.setVisibility(View.GONE);
                txt_LanSave.setVisibility(View.GONE);
                txt_BioSave.setVisibility(View.GONE);
                //    img_ediUsername.setVisibility(View.VISIBLE);
                userName = txt_usename.getText().toString().trim();
                Email = txt_email.getText().toString().trim();
                Phone = txt_phone.getText().toString().trim();
                City = txt_city.getText().toString().trim();
                Country = txt_country.getText().toString().trim();
                Biography = txt_biography.getText().toString().trim();
                Languages = txt_language.getText().toString().trim();

                if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        UpdateProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        new update_profile().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /*img_ediEmailPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_email.setEnabled(false);
                txt_phone.setEnabled(true);
                txt_phoneSave.setVisibility(View.VISIBLE);
                img_ediEmailPhone.setVisibility(View.GONE);
                txt_phone.setSelection(txt_phone.getText().length());
            }
        });*/

        txt_phoneSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_phoneSave.setVisibility(View.GONE);
                txt_nameSave.setVisibility(View.GONE);
                txt_countrySave.setVisibility(View.GONE);
                txt_LanSave.setVisibility(View.GONE);
                txt_BioSave.setVisibility(View.GONE);
                //  img_ediEmailPhone.setVisibility(View.VISIBLE);
                userName = txt_usename.getText().toString().trim();
                Email = txt_email.getText().toString().trim();
                Phone = txt_phone.getText().toString().trim();
                City = txt_city.getText().toString().trim();
                Country = txt_country.getText().toString().trim();
                Biography = txt_biography.getText().toString().trim();
                Languages = txt_language.getText().toString().trim();

                if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        UpdateProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        new update_profile().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /*img_edicityCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_city.setEnabled(true);
                txt_country.setEnabled(false);
                txt_countrySave.setVisibility(View.VISIBLE);
                img_edicityCountry.setVisibility(View.GONE);
                txt_city.setSelection(txt_city.getText().length());
            }
        });*/

        txt_countrySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_countrySave.setVisibility(View.GONE);
                txt_nameSave.setVisibility(View.GONE);
                txt_phoneSave.setVisibility(View.GONE);
                txt_LanSave.setVisibility(View.GONE);
                txt_BioSave.setVisibility(View.GONE);
                //  img_edicityCountry.setVisibility(View.VISIBLE);
                userName = txt_usename.getText().toString().trim();
                Email = txt_email.getText().toString().trim();
                Phone = txt_phone.getText().toString().trim();
                City = txt_city.getText().toString().trim();
                Country = txt_country.getText().toString().trim();
                Biography = txt_biography.getText().toString().trim();
                Languages = txt_language.getText().toString().trim();

                if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        UpdateProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        new update_profile().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /*img_edilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_language.setEnabled(true);
                txt_LanSave.setVisibility(View.VISIBLE);
                img_edilan.setVisibility(View.GONE);
                txt_language.setSelection(txt_language.getText().length());
            }
        });*/

        txt_LanSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_LanSave.setVisibility(View.GONE);
                txt_nameSave.setVisibility(View.GONE);
                txt_phoneSave.setVisibility(View.GONE);
                txt_countrySave.setVisibility(View.GONE);
                txt_BioSave.setVisibility(View.GONE);
                //   img_edilan.setVisibility(View.VISIBLE);
                userName = txt_usename.getText().toString().trim();
                Email = txt_email.getText().toString().trim();
                Phone = txt_phone.getText().toString().trim();
                City = txt_city.getText().toString().trim();
                Country = txt_country.getText().toString().trim();
                Biography = txt_biography.getText().toString().trim();
                Languages = txt_language.getText().toString().trim();

                if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        UpdateProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        new update_profile().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /*img_editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_biography.setEnabled(true);
                txt_BioSave.setVisibility(View.VISIBLE);
                img_editBio.setVisibility(View.GONE);
                txt_biography.setSelection(txt_biography.getText().length());
            }
        });*/

        txt_BioSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_BioSave.setVisibility(View.GONE);
                txt_nameSave.setVisibility(View.GONE);
                txt_phoneSave.setVisibility(View.GONE);
                txt_countrySave.setVisibility(View.GONE);
                txt_LanSave.setVisibility(View.GONE);
                //   img_editBio.setVisibility(View.VISIBLE);
                userName = txt_usename.getText().toString().trim();
                Email = txt_email.getText().toString().trim();
                Phone = txt_phone.getText().toString().trim();
                City = txt_city.getText().toString().trim();
                Country = txt_country.getText().toString().trim();
                Biography = txt_biography.getText().toString().trim();
                Languages = txt_language.getText().toString().trim();

                if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        UpdateProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        new update_profile().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
            Profile();
        } else {
            Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void Profile() {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.list_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.list_profile + json_main.toString());
                    if (json_main.getString("success").equalsIgnoreCase("True")) {
                      /*  txt_usename.setEnabled(false);
                        txt_email.setEnabled(false);
                        txt_phone.setEnabled(false);
                        txt_city.setEnabled(false);
                        txt_country.setEnabled(false);
                        txt_biography.setEnabled(false);
                        txt_language.setEnabled(false);*/
                        txt_nameSave.setVisibility(View.GONE);
                        txt_phoneSave.setVisibility(View.GONE);
                        txt_countrySave.setVisibility(View.GONE);
                        txt_LanSave.setVisibility(View.GONE);
                        txt_BioSave.setVisibility(View.GONE);
                        //    img_ediUsername.setVisibility(View.VISIBLE);
                        //    img_ediEmailPhone.setVisibility(View.VISIBLE);
                        //    img_edicityCountry.setVisibility(View.VISIBLE);
                        //    img_edilan.setVisibility(View.VISIBLE);
                        //    img_editBio.setVisibility(View.VISIBLE);

                        JSONObject obj_profile = json_main.getJSONObject("Profile");


                        SharedPreferences pref = getSharedPreferences("Login_Data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userid", obj_profile.getString("UserID"));
                        editor.putString("UserName", obj_profile.getString("UserName"));
                        editor.putString("Email", obj_profile.getString("Email"));
                        editor.putString("Image", obj_profile.getString("Image"));
                        editor.putString("Phone", obj_profile.getString("Phone"));
                        editor.putString("City", obj_profile.getString("City"));
                        editor.putString("Country", obj_profile.getString("Country"));
                        editor.putString("Biography", obj_profile.getString("Biography"));
                        editor.putString("Languages", obj_profile.getString("Languages"));
                        editor.commit();


                        if (obj_profile.getString("Image").equals("") || obj_profile.getString("Image").equals("null") || obj_profile.getString("Image").equals(null) || obj_profile.getString("Image") == null) {
                        } else {
                            Glide.with(mContext).load(obj_profile.getString("Image")).centerCrop().into(img_profile);
                            Glide.with(mContext).load(obj_profile.getString("Image")).centerCrop().into(img_sideImage);
                        }
                        if (obj_profile.getString("UserName").equals("") || obj_profile.getString("UserName").equals("null") || obj_profile.getString("UserName").equals(null) || obj_profile.getString("UserName") == null) {
                        } else {
                            txt_usename.setText(obj_profile.getString("UserName"));
                        }

                        if (obj_profile.getString("Email").equals("") || obj_profile.getString("Email").equals("null") || obj_profile.getString("Email").equals(null) || obj_profile.getString("Email") == null) {

                        } else {
                            txt_email.setText(obj_profile.getString("Email"));
                        }

                        if (obj_profile.getString("Phone").equals("") || obj_profile.getString("Phone").equals("null") || obj_profile.getString("Phone").equals(null) || obj_profile.getString("Phone") == null) {

                        } else {
                            txt_phone.setText(obj_profile.getString("Phone"));
                        }

                        if (obj_profile.getString("City").equals("") || obj_profile.getString("City").equals("null") || obj_profile.getString("City").equals(null) || obj_profile.getString("City") == null) {

                        } else {
                            txt_city.setText(obj_profile.getString("City"));
                        }

                        if (obj_profile.getString("Country").equals("") || obj_profile.getString("Country").equals("null") || obj_profile.getString("Country").equals(null) || obj_profile.getString("Country") == null) {

                        } else {
                            txt_country.setText(obj_profile.getString("Country"));
                        }

                        if (obj_profile.getString("Biography").equals("") || obj_profile.getString("Biography").equals("null") || obj_profile.getString("Biography").equals(null) || obj_profile.getString("Biography") == null) {

                        } else {
                            txt_biography.setText(obj_profile.getString("Biography"));
                        }
                        if (obj_profile.getString("Languages").equals("") || obj_profile.getString("Languages").equals("null") || obj_profile.getString("Languages").equals(null) || obj_profile.getString("Languages") == null) {

                        } else {
                            txt_language.setText(obj_profile.getString("Languages"));
                        }

                        arrayWorklist.clear();
                        JSONArray array_WorkExp = obj_profile.getJSONArray("WorkExp");
                        for (int i = 0; i < array_WorkExp.length(); i++) {
                            WorkModel model = new WorkModel();
                            model.setExperienceID(array_WorkExp.getJSONObject(i).getString("ExperienceID"));
                            model.setJobTitle(array_WorkExp.getJSONObject(i).getString("JobTitle"));
                            model.setStartDate(array_WorkExp.getJSONObject(i).getString("StartDate"));
                            model.setEndDate(array_WorkExp.getJSONObject(i).getString("EndDate"));
                            model.setJobDescription(array_WorkExp.getJSONObject(i).getString("JobDescription"));
                            arrayWorklist.add(model);
                        }

                        if (arrayWorklist.size() > 0) {
                            txt_noJobs.setVisibility(View.GONE);
                            Recyler_worklist.setVisibility(View.VISIBLE);
                            jobWorklistAdpter = new JobWorklistAdpter(mContext, arrayWorklist);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            Recyler_worklist.setLayoutManager(linearLayoutManager);
                            Recyler_worklist.setAdapter(jobWorklistAdpter);
                        } else {
                            txt_noJobs.setVisibility(View.VISIBLE);
                            Recyler_worklist.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
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
                Log.e("params", "" + Appconstant.list_profile + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.list_profile);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class update_profile extends AsyncTask<String, String, String> {
        private String imageupload = Appconstant.update_profile;
        private String response;
        String IMAGE_NAME, success, success_msg;
        JSONObject jsonObject;
        String erro_msg, status;
        private byte[] image_bytes = null;
        private WebAPIRequest webapirequest = new WebAPIRequest();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("onPreExecute", "onPreExecute");
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            Log.e("doInBackground", "doInBackground");
            image_bytes = getBitmapAsByteArray(mBitmap);
            mTempBitmapArray.add(mBitmap);
            image_bytes = getBitmapAsByteArray(mTempBitmapArray.get(0));
            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;
            String res = i1 + ".png";
            Log.e("res", "" + res);
            Log.e("params", webapirequest.update_profile(image_bytes, imageupload, res, pref.getString("userid", ""), userName, Email, Phone, City, Country, Biography, Languages));
            response = webapirequest.update_profile(image_bytes, imageupload, res, pref.getString("userid", ""), userName, Email, Phone, City, Country, Biography, Languages);
            Log.e("response", Appconstant.update_profile + response);
            try {
                jsonObject = new JSONObject(response);
                status = jsonObject.getString("success_msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.e("onPostExecute", "onPostExecute");
            Toast.makeText(ProfileActivity.this, status, Toast.LENGTH_SHORT).show();
            if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                Profile();
            } else {
                Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
            }
        }

        public byte[] getBitmapAsByteArray(Bitmap bitmap) {
            if (bitmap != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                return outputStream.toByteArray();
            } else {
                return null;
            }
        }
    }

    private void UpdateProfile() {
        String tag_string_req = "req";
        Progressbar.setVisibility(View.VISIBLE);
        final StringRequest strReq = new StringRequest(Request.Method.POST, Appconstant.update_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Progressbar.setVisibility(View.GONE);
                try {
                    JSONObject json_main = new JSONObject(response);
                    Log.e("response", "" + Appconstant.update_profile + json_main.toString());
                    if (json_main.getString("success").equalsIgnoreCase("True")) {
                        Toast.makeText(ProfileActivity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, json_main.getString("error_msg"), Toast.LENGTH_LONG).show();
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
                params.put("UserName", userName);
                params.put("Image", pref.getString("Image", ""));
                params.put("Email", Email);
                params.put("Phone", Phone);
                params.put("City", City);
                params.put("Country", Country);
                params.put("Biography", Biography);
                params.put("Languages", Languages);
                Log.e("params", "" + Appconstant.update_profile + params);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Appconstant.update_profile);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public class JobWorklistAdpter extends RecyclerView.Adapter<JobWorklistAdpter.MyViewHolder> {
        private Context context;
        private List<WorkModel> arrayList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView txt_Jobtitle, txt_date, txt_workExp;

            public MyViewHolder(View view) {
                super(view);
                txt_Jobtitle = view.findViewById(R.id.txt_Jobtitle);
                txt_date = view.findViewById(R.id.txt_date);
                txt_workExp = view.findViewById(R.id.txt_workExp);
            }
        }

        public JobWorklistAdpter(Context context, List<WorkModel> listPastOrders) {
            this.context = context;
            this.arrayList = listPastOrders;
        }

        @Override
        public JobWorklistAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_list_adpter, parent, false);

            return new JobWorklistAdpter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final JobWorklistAdpter.MyViewHolder holder, final int position) {
            final WorkModel model = arrayList.get(position);
            if (model.getJobTitle().equals("") || model.getJobTitle().equals("null") || model.getJobTitle().equals(null)) {

            } else {
                holder.txt_Jobtitle.setText(model.getJobTitle());
            }
            if (model.getJobDescription().equals("") || model.getJobDescription().equals("null") || model.getJobDescription().equals(null)) {

            } else {
                holder.txt_workExp.setText(model.getJobDescription());
            }

            holder.txt_date.setText("From " + DateUtils.NewDateTime(model.getStartDate()) + " - " + DateUtils.NewDateTime(model.getEndDate()));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


    private void selectimage() {
        String str[] = {"Choose From Gallery", "Open Camera", "Cancel"};
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setItems(str,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (which == 0) {
                            // gallery
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                REQUEST_CODE = 70;
                                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    photogallery();
                                } else {
                                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                                }
                            } else {
                                photogallery();
                            }
                        } else if (which == 1) {
                            // camera
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                MY_REQUEST_CODE = 40;
                                if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_REQUEST_CODE);
                                } else {
                                    photocamera_perm();
                                }
                            } else {
                                photocamera();
                            }
                        } else if (which == 2) {

                        }
                    }


                });
        alert.show();
    }

    @SuppressLint("NewApi")
    private void photocamera_perm() {
        REQUEST_CODE = 50;
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            photocamera();
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }

    private void photogallery() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 11);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/jpeg");
            startActivityForResult(galleryIntent, 33);
        }
    }

    private void photocamera() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK) {
                    Log.e("11", "11");
                    Uri selectedImage = data.getData();
                    String path = getPath(ProfileActivity.this, selectedImage);
                    if (path != null) {
                        mPath = path;
                        Log.e("mPath 1", "" + mPath);
                        try {
                            mBitmap = Utility.decodeSampledBitmap(ProfileActivity.this, Uri.fromFile(new File(mPath)));
                            Log.e("mBitmap", "" + mBitmap);
                            ExifInterface exif = new ExifInterface(photo.toString());
                            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                                mBitmap = rotate(mBitmap, 90);
                            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                                mBitmap = rotate(mBitmap, 270);
                            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                                mBitmap = rotate(mBitmap, 180);
                            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
                                mBitmap = rotate(mBitmap, 90);
                            }
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
                break;
            case 33:
                if (resultCode == RESULT_OK) {
                    Log.e("33", "33");
                    mTempBitmapArray.clear();
                    mPath = "";
                    Uri selectedImageUri = data.getData();
                    try {
                        mPath = getImagePath(selectedImageUri);
                        Log.e("mPath 2", "" + mPath);
                        try {
                            mBitmap = Utility.decodeSampledBitmap(ProfileActivity.this, Uri.fromFile(new File(mPath)));
                            Log.e("mBitmap", "" + mBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        img_profile.setImageBitmap(mBitmap);
                        // Glide.with(ProfileActivity.this).load(mBitmap).into(img_profile);
                    /*    userName = txt_usename.getText().toString().trim();
                        Email = txt_email.getText().toString().trim();
                        Phone = txt_phone.getText().toString().trim();
                        City = txt_city.getText().toString().trim();
                        Country = txt_country.getText().toString().trim();
                        Biography = txt_biography.getText().toString().trim();
                        Languages = txt_language.getText().toString().trim();
*/
                     /*   if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                            new update_profile().execute();
                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                        }*/
                        //
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else if (resultCode == SELECT_IMAGE) {
                    Uri selectedImage = data.getData();
                    String path = getPath(ProfileActivity.this, selectedImage);
                    if (path != null) {
                        mPath = path;
                        Log.e("mPath 3", "" + mPath);
                        try {
                            mBitmap = Utility.decodeSampledBitmap(ProfileActivity.this, Uri.fromFile(new File(mPath)));

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        img_profile.setImageBitmap(mBitmap);
                    }
                }
                break;
            case 1:
                Log.e("1", "1");
                if (resultCode == RESULT_OK) {
                    mTempBitmapArray.clear();
                    onCaptureImageResult(data);
                }
                break;
            case 200:
                if (resultCode == RESULT_OK) {
                    if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                        Profile();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public int getOrientation(Uri selectedImage) {
        int orientation = 0;
        final String[] projection = new String[]{MediaStore.Images.Media.ORIENTATION};
        final Cursor cursor = ProfileActivity.this.getContentResolver().query(selectedImage, projection, null, null, null);
        if (cursor != null) {
            final int orientationColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
            if (cursor.moveToFirst()) {
                orientation = cursor.isNull(orientationColumnIndex) ? 0 : cursor.getInt(orientationColumnIndex);
            }
            cursor.close();
        }
        return orientation;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        mPath = "";
        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPath = destination.getAbsolutePath();
        Log.e("mPath 4", "" + mPath);

        try {
            mBitmap = Utility.decodeSampledBitmap(ProfileActivity.this, Uri.fromFile(new File(mPath)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        userName = txt_usename.getText().toString().trim();
        Email = txt_email.getText().toString().trim();
        Phone = txt_phone.getText().toString().trim();
        City = txt_city.getText().toString().trim();
        Country = txt_country.getText().toString().trim();
        Biography = txt_biography.getText().toString().trim();
        Languages = txt_language.getText().toString().trim();

        if (mPath.equals("") || mPath.equals(null) || mPath.equals("null")) {
            if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                UpdateProfile();
            } else {
                Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
            }
        } else {
            if (Appconstant.isNetworkAvailable(ProfileActivity.this)) {
                new update_profile().execute();
            } else {
                Toast.makeText(ProfileActivity.this, R.string.check_connection, Toast.LENGTH_LONG).show();
            }
        }
        img_profile.setImageBitmap(mBitmap);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE || requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 40) {
                    photocamera_perm();
                } else if (requestCode == 50) {
                    photocamera();
                } else if (requestCode == 70) {
                    photogallery();
                }
            } else {
            }
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        // mtx.postRotate(degree);
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private String getImagePath(Uri selectedImageUri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(selectedImageUri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = ProfileActivity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(selectedImageUri.getScheme())) {
            //return selectedImageUri.getImagePath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }


    @Override
    protected void onResume() {
        slide_Profile.setBackgroundColor(Color.parseColor("#000000"));

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
