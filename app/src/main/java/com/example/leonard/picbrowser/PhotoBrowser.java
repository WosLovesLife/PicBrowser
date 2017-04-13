package com.example.leonard.picbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.alexvasilkov.gestures.animation.ViewPosition;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator.PositionUpdateListener;
import com.alexvasilkov.gestures.commons.DepthPageTransformer;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;

import java.util.List;

/**
 * Separate activity for fullscreen image. This activity should have translucent background
 * and should skip enter and exit animations.
 */
public class PhotoBrowser extends FrameLayout {
    private ViewPager.SimpleOnPageChangeListener pagerListener;

    private ViewsTransitionAnimator<Integer> animator;

    private ViewPager mViewPager;
    private View mBgView;

    private PhotoPagerAdapter pagerAdapter;

    public PhotoBrowser(@NonNull Context context) {
        this(context, null);
    }

    public PhotoBrowser(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoBrowser(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        onCreate();
    }

    public static void open(Activity from, ViewPosition position, @DrawableRes int imageId) {
    }

    protected void onCreate() {
        LayoutInflater.from(getContext()).inflate(R.layout.photo_browser, this, true);

        mViewPager = (ViewPager) findViewById(R.id.vp);
        mBgView = findViewById(R.id.view_bg);

        initPager();
    }

    private void initPager() {
        // Setting up pager views
        pagerAdapter = new PhotoPagerAdapter(mViewPager);

        pagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                onPhotoInPagerSelected(position);
            }
        };

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(pagerListener);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    public void initAnimator(RecyclerView from, SimpleTracker tracker) {
        final SimpleTracker pagerTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int pos) {
                RecyclePagerAdapter.ViewHolder holder = pagerAdapter.getViewHolder(pos);
                return holder == null ? null : PhotoPagerAdapter.getImage(holder);
            }
        };

        animator = GestureTransitions.from(from, tracker).into(mViewPager, pagerTracker);
        animator.addPositionUpdateListener(new PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                mBgView.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                mBgView.getBackground().setAlpha((int) (255 * position));
                if (isLeaving && position == 0f) {
                    pagerAdapter.setActivated(false);
                }
            }
        });
    }

    public void setData(List<Photo> photos) {
        pagerAdapter.setPhotos(photos);

        // Ensure listener called for 0 position
        pagerListener.onPageSelected(mViewPager.getCurrentItem());
    }

    public void show(int position) {
        pagerAdapter.setActivated(true);
        animator.enter(position, true);
    }

    public boolean onBackPressed() {
        if (!animator.isLeaving()) {
            animator.exit(true);
            return true;
        }
        return false;
    }
}