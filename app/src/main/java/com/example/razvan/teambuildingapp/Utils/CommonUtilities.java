package com.example.razvan.teambuildingapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by LexGrup on 5/5/2017.
 */

public class CommonUtilities {
    public static void setAvatar(final Context context, String photoUrl, final ImageView avatarContainer){
        if(TextUtils.isEmpty(photoUrl))
            photoUrl = "http://quikpikz.com/images/avatar.png";
        Glide.with(context)
                .load(photoUrl)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(avatarContainer) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        avatarContainer.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
