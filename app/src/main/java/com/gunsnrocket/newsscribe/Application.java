package com.gunsnrocket.newsscribe;

import com.vk.sdk.VKSdk;

/**
 * Created by dnt on 9/21/15.
 */
public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
