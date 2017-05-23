package com.example.leonard.picbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonard on 17/4/13.
 */

public class MultiPhotoActivity extends AppCompatActivity {

    String[] ADDRESS = {
            "http://wx2.sinaimg.cn/mw690/707e96d5gy1ffo4d9sf3jj20qp0gpn2q.jpg",
            "http://wx2.sinaimg.cn/mw690/707e96d5gy1ffo4da51kyj20go09cgn9.jpg",
            "http://wx2.sinaimg.cn/mw690/005Bd8p4gy1ffmm2o5v13j30sg0lcn0b.jpg",
            "http://wx1.sinaimg.cn/mw690/697b3ffbly1ffo5ok606vj20dw0hu77h.jpg",
            "http://wx4.sinaimg.cn/mw690/697b3ffbly1ffo5ojxwelj20im0ahgof.jpg",
            "http://wx4.sinaimg.cn/mw690/69267083gy1ffltpxv6rkj22b42vtqva.jpg",
            "http://wx3.sinaimg.cn/mw690/69267083gy1ffluiltyqyj224x2o3qv7.jpg"
    };

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private FrameLayout mFlContainer;
    private PhotoBrowserHelper mPhotoBrowserHelper;

    @Override
    public void onBackPressed() {
        if (mPhotoBrowserHelper == null || !mPhotoBrowserHelper.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_photo);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new Adapter();
        mAdapter.mPhotos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (String s : ADDRESS) {
                mAdapter.mPhotos.add(new Photo(s));
            }
        }

        mRecyclerView.setAdapter(mAdapter);

        mFlContainer = (FrameLayout) ((ViewGroup) ((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(1);
    }

    class Adapter extends RecyclerView.Adapter<Holder> {
        List<Photo> mPhotos;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(parent);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            holder.mImageView.setImageURI(mPhotos.get(position).url);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPhotoBrowserHelper == null) {
                        mPhotoBrowserHelper = new PhotoBrowserHelper()
                                .createPhotoBrowser(MultiPhotoActivity.this, mPhotos, mRecyclerView);
                    }
                    mPhotoBrowserHelper.show(mFlContainer, holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPhotos != null ? mPhotos.size() : 0;
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        SimpleDraweeView mImageView;

        public Holder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small, parent, false));
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.cross_to);
        }
    }
}
