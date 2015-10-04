package com.gunsnrocket.newsscribe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dnt on 9/28/15.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;

    PostAdapter(Context c) {
        context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.simple_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.placeText.setText(TestActivity.PlaceholderFragment.posts.get(i).text);
        viewHolder.placePostAuthor.setText(TestActivity.PlaceholderFragment.posts.get(i).author);
        for (VKAttachments.VKApiAttachment j : TestActivity.PlaceholderFragment.posts.get(i).attachments) {
            if (j.getType() == VKAttachments.TYPE_PHOTO) {
                VKApiPhoto photo = (VKApiPhoto)j;
                Picasso.with(context).load(photo.photo_604).resize(1000, 700).centerInside().into(viewHolder.placeImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return TestActivity.PlaceholderFragment.posts.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        private LinearLayout placeTextHolder;
        private TextView placeText, placePostAuthor;
        private ImageView placeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            placeTextHolder = (LinearLayout)itemView.findViewById(R.id.placeTextHolder);
            placeText = (TextView)itemView.findViewById(R.id.placeText);
            placePostAuthor = (TextView)itemView.findViewById(R.id.placePostAuthor);
            placeImage = (ImageView)itemView.findViewById(R.id.placeImage);
        }
    }
}
