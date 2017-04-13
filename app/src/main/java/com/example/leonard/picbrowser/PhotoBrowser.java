package com.example.leonard.picbrowser;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonard on 17/4/13.
 */

public class PhotoBrowser extends AppCompatActivity {
    private ArrayList<Integer> mPhoto;
    private int mSelect;
    private ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.photo_browser);

        mPhoto = getIntent().getIntegerArrayListExtra("photo");
        mSelect = getIntent().getIntExtra("select", 0);

        Adapter adapter = new Adapter();
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mSelect);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
    }

    class Adapter extends PagerAdapter {
        List<Integer> data;

        @Override
        public int getCount() {
            return data != null ? data.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo_browser, container, false);
            container.addView(iv);
            iv.setImageDrawable(new ColorDrawable(data.get(position)));
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
