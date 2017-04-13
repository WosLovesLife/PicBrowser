package com.example.leonard.picbrowser;

import android.support.annotation.DrawableRes;

/**
 * Created by leonard on 17/4/13.
 */

public class Photo {
    @DrawableRes
    int res;

    public Photo() {
    }

    public Photo(int res) {
        this.res = res;
    }
}
