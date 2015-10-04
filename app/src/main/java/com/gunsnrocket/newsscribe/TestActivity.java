package com.gunsnrocket.newsscribe;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;

/**
 * Created by dnt on 9/21/15.
 */
public class TestActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedIntentState){
        super.onCreate(savedIntentState);
        setContentView(R.layout.activity_test);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedIntentState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        //TextView postText;
        private RecyclerView list;
        private StaggeredGridLayoutManager layoutManager;
        private SlideInBottomAnimationAdapter animationAdapter;
        static ArrayList<Post> posts = new ArrayList<>();

        public static final String START_FROM = "start_from";
        public static boolean firstRequest = true;
        public static String nextPost;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstacneState) {
            return inflater.inflate(R.layout.another_fragment_test, container, false);
        }

        @Override
        public void onViewCreated(final View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            //postText = (TextView)view.findViewById(R.id.post_text);
            //postText.setMovementMethod(new ScrollingMovementMethod());
            //final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, posts);

            list = (RecyclerView)view.findViewById(R.id.list_view);
            layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            list.setLayoutManager(layoutManager);

            animationAdapter = new SlideInBottomAnimationAdapter(new PostAdapter(getContext()));
            animationAdapter.setDuration(500);
            animationAdapter.setInterpolator(new OvershootInterpolator(0.7f));
            //animationAdapter.setInterpolator(new OvershootInterpolator());
            list.setAdapter(animationAdapter);



            //list.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));


            VKRequest request = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS, "post", START_FROM, nextPost, VKApiConst.COUNT, "20"));

            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {

                    try {
                        nextPost = response.json.getJSONObject("response").getString("next_from");
                    } catch (JSONException e) {
                        Toast t = Toast.makeText(getContext(), "response Exception", Toast.LENGTH_LONG);
                        t.show();
                    }


                    for (int i = 0; i < 20; i++) {
                        try {
                            JSONObject obj = response.json.getJSONObject("response");
                            Post p = new Post();
                            p.parse(obj.getJSONArray("items").getJSONObject(i));
                            p.setAuthor(obj);
                            posts.add(p);
                            animationAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast t = Toast.makeText(getContext(), "get text Exception", Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                }
            });


        }

    }

}


