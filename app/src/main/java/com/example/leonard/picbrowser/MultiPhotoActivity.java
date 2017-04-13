package com.example.leonard.picbrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonard on 17/4/13.
 */

public class MultiPhotoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private PhotoBrowser mPhotoBrowser;
    private FrameLayout mFlContainer;

    @Override
    public void onBackPressed() {
        if (mPhotoBrowser == null || !mPhotoBrowser.onBackPressed()) {
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
            mAdapter.mPhotos.add(new Photo(R.color.color1));
            mAdapter.mPhotos.add(new Photo(R.color.color2));
            mAdapter.mPhotos.add(new Photo(R.color.color3));
            mAdapter.mPhotos.add(new Photo(R.color.color4));
            mAdapter.mPhotos.add(new Photo(R.color.color5));
            mAdapter.mPhotos.add(new Photo(R.color.color6));
            mAdapter.mPhotos.add(new Photo(R.color.color7));
        }

        mRecyclerView.setAdapter(mAdapter);

        mPhotoBrowser = new PhotoBrowser(this);
        mPhotoBrowser.setData(mAdapter.mPhotos);
        mPhotoBrowser.initAnimator(mRecyclerView, new SimpleTracker() {
            @Override
            protected View getViewAt(int position) {
                Holder holder = (Holder) mRecyclerView.findViewHolderForLayoutPosition(position);
                return holder == null ? null : holder.mImageView;
            }
        });

        mFlContainer = (FrameLayout) ((ViewGroup) ((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(1);
        mFlContainer.addView(mPhotoBrowser);
        mPhotoBrowser.setVisibility(View.INVISIBLE);
    }

    class Adapter extends RecyclerView.Adapter<Holder> {
        List<Photo> mPhotos;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(parent);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
//            holder.mImageView.setImageResource(mPhotos.get(position).res);
            holder.mImageView.setImageResource(R.drawable.img);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPhotoBrowser.setVisibility(View.VISIBLE);
                    mPhotoBrowser.show(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPhotos != null ? mPhotos.size() : 0;
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public Holder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small, parent, false));
            mImageView = (ImageView) itemView.findViewById(R.id.cross_to);
        }
    }
}
