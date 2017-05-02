package com.example.razvan.teambuildingapp.EventAttendantsRV;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.razvan.teambuildingapp.Entities.EventAttendant;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LexGrup on 5/2/2017.
 */

public class EventAttendantsAdapter extends RecyclerView.Adapter<EventAttendantsAdapter.EventAttendantViewHolder> {

    private List<EventAttendant> mDataSet;

    public EventAttendantsAdapter(@NonNull List<EventAttendant> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAttendantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_attendant, parent, false);

        return new EventAttendantViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventAttendantViewHolder holder, int position) {
        final EventAttendant attendant = mDataSet.get(position);
        holder.bind(attendant, position);

    }

    // Return the size of the dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // Provide a reference to the views for each data item
    static class EventAttendantViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_attendant_pic)
        ImageView ivAttendantPic;
        @BindView(R.id.tv_attendant_name)
        TextView tvAttendantName;

        EventAttendantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull EventAttendant attendant, int position) {
            final Context context = tvAttendantName.getContext();
            String picURL = "http://quikpikz.com/images/avatar.png";
            if(!TextUtils.isEmpty(attendant.getPhotoUrl())){
                picURL = attendant.getPhotoUrl();
            }
            tvAttendantName.setText(attendant.getName());
            Glide.with(context)
                    .load(picURL)
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(ivAttendantPic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivAttendantPic.setImageDrawable(circularBitmapDrawable);
                        }
            });
        }
    }
}
