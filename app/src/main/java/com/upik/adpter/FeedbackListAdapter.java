package com.upik.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.upik.R;
import com.upik.model.FeedbackListModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.MyViewHolder> {

    Context context;
    ArrayList<FeedbackListModel> arrayList = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername, txtComment;
        CircleImageView imgUser;
        RatingBar ratingBarMain;

        public MyViewHolder(View view) {
            super(view);

            txtUsername = view.findViewById(R.id.txtUsername);
            txtComment = view.findViewById(R.id.txtComment);

            imgUser = view.findViewById(R.id.imgUser);

            ratingBarMain = view.findViewById(R.id.ratingBarMain);
        }
    }

    public FeedbackListAdapter(Context context, ArrayList<FeedbackListModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_list_adpter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final FeedbackListModel model = arrayList.get(position);

        Glide.with(context).load(model.getImage()).centerCrop().into(holder.imgUser);
        holder.txtUsername.setText(model.getUserName());
        holder.txtComment.setText(model.getComment());
        holder.ratingBarMain.setRating(Float.parseFloat(model.getRating()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}