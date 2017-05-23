package com.example.leonard.picbrowser;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;

import java.util.List;

public class PhotoBrowserHelper {
    private PhotoBrowser mPhotoBrowser;

    ViewGroup mAnchor;

    PhotoBrowserHelper createPhotoBrowser(Activity activity, List<Photo> photos, final ViewGroup container) {
        mPhotoBrowser = new PhotoBrowser(activity);
        mPhotoBrowser.setData(photos);

        mPhotoBrowser.initAnimator(GestureTransitions.from(new ViewsTransitionAnimator.RequestListener<Integer>() {
            @Override
            public void onRequestView(@NonNull final Integer position) {
                if (!(container instanceof RecyclerView)) {
                    View child = container.getChildAt(position);
                    if (child != null) {
                        getAnimator().setFromView(position, child);
                    }

                    return;
                }

                final RecyclerView recyclerView = (RecyclerView) container;
                final MultiPhotoActivity.Holder holder = (MultiPhotoActivity.Holder) recyclerView.findViewHolderForLayoutPosition(position);
                if (holder == null) {
                    recyclerView.scrollToPosition(position);
                    recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            MultiPhotoActivity.Holder holder = (MultiPhotoActivity.Holder) recyclerView.findViewHolderForLayoutPosition(position);

                            ImageView imageView = holder != null ? holder.mImageView : null;
                            if (imageView != null) {
                                getAnimator().setFromView(position, imageView);
                            }
                        }
                    });
                } else {
                    ImageView imageView = holder.mImageView;
                    if (imageView != null) {
                        getAnimator().setFromView(position, imageView);
                    }
                }
            }
        }));
        mPhotoBrowser.setHelper(new PhotoBrowser.Helper() {
            @Override
            public void onDismiss() {
                mAnchor.removeView(mPhotoBrowser);
            }
        });

        return this;
    }

    public PhotoBrowserHelper show(ViewGroup anchor, int position) {
        mAnchor = anchor;

        mAnchor.addView(mPhotoBrowser);
        mPhotoBrowser.show(position);

        return this;
    }

    public boolean onBackPressed() {
        return mPhotoBrowser.onBackPressed();
    }
}