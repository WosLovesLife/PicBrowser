package com.example.leonard.picbrowser;

import android.support.annotation.DrawableRes;

/**
 * Created by leonard on 17/4/13.
 */

public class Photo {
    @DrawableRes
    int res;
    String url;

    public Photo() {
    }

    public Photo(int res) {
        this.res = res;
    }

    public Photo(String url) {
        this.url = url;
    }

    public Photo(int res, String url) {
        this.res = res;
        this.url = url;
    }
}
