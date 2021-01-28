package com.upik.VollySupport;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    private static AppController mInstance;
    public static Typeface OpenSans_bold, OpenSans_boldItalic, OpenSans_Extrabold, OpenSans_ExtraboldItalic, OpenSans_Italic, OpenSans_Light, OpenSans_LightItalic, OpenSans_Regular, OpenSans_Semibold, OpenSans_SemiboldItalic;
    public static Typeface logo_BPreplay, logo_BPreplayBold, logo_BPreplayBoldItalic, logo_BPreplayItalics;
    public static Typeface Roboto_black,Roboto_blackItalic,Roboto_Bold,Roboto_BoldCondensed,Roboto_BoldCondensedItalic,Roboto_BoldItalic,Roboto_Condensed,Roboto_CondensedItalic,Roboto_Light,Roboto_LightItalic,Roboto_Medium,Roboto_MediumItalic,Roboto_Regular,Roboto_Thin,Roboto_ThinItalic;

    @Override
    public void onCreate() {
        super.onCreate();
       /* Fabric.with(this, new Crashlytics());
       */

        OpenSans_bold = Typeface.createFromAsset(getAssets(), "OpenSans_Bold.ttf");
        OpenSans_boldItalic = Typeface.createFromAsset(getAssets(), "OpenSans_BoldItalic.ttf");
        OpenSans_Extrabold = Typeface.createFromAsset(getAssets(), "OpenSans_ExtraBold.ttf");
        OpenSans_ExtraboldItalic = Typeface.createFromAsset(getAssets(), "OpenSans_ExtraBoldItalic.ttf");
        OpenSans_Italic = Typeface.createFromAsset(getAssets(), "OpenSans_Italic.ttf");
        OpenSans_Light = Typeface.createFromAsset(getAssets(), "OpenSans_Light.ttf");
        OpenSans_LightItalic = Typeface.createFromAsset(getAssets(), "OpenSans_LightItalic.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getAssets(), "OpenSans_Semibold.ttf");
        OpenSans_SemiboldItalic = Typeface.createFromAsset(getAssets(), "OpenSans_SemiboldItalic.ttf");

        logo_BPreplay = Typeface.createFromAsset(getAssets(), "BPreplay.otf");
        logo_BPreplayBold = Typeface.createFromAsset(getAssets(), "BPreplayBold.otf");
        logo_BPreplayBoldItalic = Typeface.createFromAsset(getAssets(), "BPreplayBoldItalics.otf");
        logo_BPreplayItalics = Typeface.createFromAsset(getAssets(), "BPreplayItalics.otf");

        Roboto_black = Typeface.createFromAsset(getAssets(), "Roboto_Black.ttf");
        Roboto_blackItalic = Typeface.createFromAsset(getAssets(), "Roboto_BlackItalic.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "Roboto_Bold.ttf");
        Roboto_BoldCondensed = Typeface.createFromAsset(getAssets(), "Roboto_BoldCondensed.ttf");
        Roboto_BoldCondensedItalic = Typeface.createFromAsset(getAssets(), "Roboto_BoldCondensedItalic.ttf");
        Roboto_BoldItalic = Typeface.createFromAsset(getAssets(), "Roboto_BoldItalic.ttf");
        Roboto_Condensed = Typeface.createFromAsset(getAssets(), "Roboto_Condensed.ttf");
        Roboto_CondensedItalic = Typeface.createFromAsset(getAssets(), "Roboto_CondensedItalic.ttf");
        Roboto_Light = Typeface.createFromAsset(getAssets(), "Roboto_Italic.ttf");
        Roboto_LightItalic = Typeface.createFromAsset(getAssets(), "Roboto_LightItalic.ttf");
        Roboto_Medium = Typeface.createFromAsset(getAssets(), "Roboto_Medium.ttf");
        Roboto_MediumItalic = Typeface.createFromAsset(getAssets(), "Roboto_MediumItalic.ttf");
        Roboto_Regular = Typeface.createFromAsset(getAssets(), "Roboto_Regular.ttf");
        Roboto_Thin = Typeface.createFromAsset(getAssets(), "Roboto_Thin.ttf");
        Roboto_ThinItalic = Typeface.createFromAsset(getAssets(), "Roboto_ThinItalic.ttf");


        mInstance = this;
        printHashKey();
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.upik",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}