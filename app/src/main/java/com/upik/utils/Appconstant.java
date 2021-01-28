package com.upik.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 5/3/2019.
 */

public class Appconstant {
    // public static String URL = "http://ilucenttechnolabs.com/projects/UPIK/api/";
    // public static String URL = "http://nifty-fits.com/briteleap/projects/upikwebapp/api/";
    public static String URL = "https://upik.ca/api/";

    public static String signup = URL + "signup.php";
    public static String login = URL + "login.php";
    public static String list_profile = URL + "list_profile.php";
    public static String update_profile = URL + "update_profile.php";
    public static String category_list = URL + "category_list.php";
    public static String Myjob_list = URL + "job_list.php";
    public static String add_work_experience = URL + "add_work_experience.php";
    public static String add_my_job = URL + "add_my_job.php";
    public static String forget_password = URL + "forget_password.php";
    public static String favorite_job = URL + "favorite_job.php";
    public static String favorite_job_list = URL + "favorite_job_list.php";
    public static String cancel_job = URL + "cancel_job.php";
    public static String add_withdraw = URL + "add_withdraw.php";
    public static String complete_job = URL + "complete_job.php";
    public static String add_feedback = URL + "add_feedback.php";
    public static String feedback_list = URL + "feedback_list.php";

    public static String view = "list", check_date = "", filter = "", dateString = "", socail_click = "";
    public static String Silde_click = "home";
    public static String bottomClick = "";
    public static String Category = "";
    public static String languageCheck = "1";
    public static int pos = 0;
    public static ArrayList<String> arrayList = new ArrayList<>();

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void ShowMsg(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }
}
