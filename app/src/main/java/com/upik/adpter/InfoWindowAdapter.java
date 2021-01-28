package com.upik.adpter;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.upik.R;
import com.upik.model.NearByLocations;


public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;
    private NearByLocations modelInfoWindow;
    private Marker myMarker;
    LinearLayout lin_popup;

    public InfoWindowAdapter(Activity context, NearByLocations modelInfoWindow) {
        this.context = context;
        this.modelInfoWindow = modelInfoWindow;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        this.myMarker = marker;
        View view = context.getLayoutInflater().inflate(R.layout.row_custom_info_window,null);
        lin_popup = view.findViewById(R.id.lin_popup);

        TextView txt_title=view.findViewById(R.id.txt_title);
        TextView txt_agent=view.findViewById(R.id.txt_agent);
        TextView txt_Timmimg=view.findViewById(R.id.txt_Timmimg);
        TextView txt_pay=view.findViewById(R.id.txt_pay);

        if (modelInfoWindow.getJobTitle().equals("")) {
            txt_title.setText("");
        } else {
            txt_title.setText(modelInfoWindow.getJobTitle());
        }

        if (modelInfoWindow.getMerchant().equals("")) {
            txt_agent.setText("");
        } else {
            txt_agent.setText(modelInfoWindow.getMerchant());
        }

        if (modelInfoWindow.getTiming().equals("")) {
            txt_Timmimg.setText("");
        } else {
            txt_Timmimg.setText(modelInfoWindow.getTiming());
        }
        if (modelInfoWindow.getTotalPay().equals("")) {
            txt_pay.setText("");
        } else {
            txt_pay.setText("$"+modelInfoWindow.getTotalPay());
        }
        return view;
    }
}
