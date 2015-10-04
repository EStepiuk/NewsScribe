package com.gunsnrocket.newsscribe;

import android.os.Parcel;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dnt on 9/28/15.
 */
class Post extends VKAttachments.VKApiAttachment {

    public String author;

    public String text;

    public VKAttachments attachments = new VKAttachments();

    /**
     * add with copy_history
     */
    public int repost_from_id = 0;

    public int likes_count;

    public int source_id;

    public int post_id;

    public int date;

    public int comments_count;

    public int repost_count;

    public boolean can_post_comment;

    public Post parse(JSONObject source) throws JSONException {
        source_id = source.optInt("source_id");
        post_id = source.optInt("post_id");
        text = source.optString("text");
        date = source.optInt("date");

        JSONObject comments = source.optJSONObject("comments");
        if (comments != null) {
            comments_count = comments.optInt("count");
            can_post_comment = (comments.optInt("can_post") == 1);
        }

        JSONObject likes = source.optJSONObject("likes");
        if (likes != null) {
            likes_count = likes.getInt("count");
        }

        JSONObject reposts = source.optJSONObject("reposts");
        if (reposts != null) {
            repost_count = reposts.optInt("count");
        }

        attachments.fill(source.optJSONArray("attachments"));

        return this;
    }

    public void setAuthor(JSONObject response) throws JSONException {
        if (source_id < 0) {
            JSONArray groups = response.getJSONArray("groups");
            for (int i = 0; i < groups.length(); i++) {
                JSONObject group = groups.getJSONObject(i);
                if (group.getInt("id") == Math.abs(source_id)) {
                    author = group.optString("name");
                    break;
                }
            }
        } else {
            JSONArray profiles = response.getJSONArray("profiles");
            for (int i = 0; i < profiles.length(); i++) {
                JSONObject profile = profiles.getJSONObject(i);
                if (profile.getInt("id") == Math.abs(source_id)) {
                    author = profile.optString("first_name") + " " + profile.optString("last_name");
                    break;
                }
            }
        }
    }
    /**
     ** Have to Override
     */
    @Override
    public CharSequence toAttachmentString() {
        return null;
    }

    @Override
    public String getType() {
        return VKAttachments.TYPE_POST;
    }

    @Override
    public int getId() {
        return post_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}