package com.example.leonard.picbrowser;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator.PositionUpdateListener;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;

import java.util.List;

public class PhotoPagerAdapter extends RecyclePagerAdapter<PhotoPagerAdapter.ViewHolder> {

    private static final long PROGRESS_DELAY = 300L;

    private final ViewPager viewPager;
    private List<Photo> photos;

    private boolean activated;

    public PhotoPagerAdapter(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int pos) {
        return photos == null || pos < 0 || pos >= photos.size() ? null : photos.get(pos);
    }

    /**
     * To prevent ViewPager from holding heavy views (with bitmaps)  while it is not showing
     * we may just pretend there are no items in this adapter ("activate" = false).
     * But once we need to run opening animation we should "activate" this adapter again.<br/>
     * Adapter is not activated by default.
     */
    public void setActivated(boolean activated) {
        if (this.activated != activated) {
            this.activated = activated;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return !activated || photos == null ? 0 : photos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        return new ViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Temporary disabling touch controls
        if (!holder.gesturesDisabled) {
            holder.image.getGestureController().getSettings().disableGestures();
            holder.gesturesDisabled = true;
        }
        holder.image.getGestureController().resetState();

        holder.progress.animate().setStartDelay(PROGRESS_DELAY).alpha(1f);

        Photo photo = photos.get(position);

        // Loading image
        if (photo.res > 0) { // from drawable res or color res

            holder.image.setImageResource(photo.res);
        } else if (!TextUtils.isEmpty(photo.url)) { // from url
            Bitmap cacheBitmap = PhotoBrowser.getCacheBitmap(Uri.parse(photo.url));
            if (cacheBitmap != null) {
                holder.image.setImageBitmap(cacheBitmap);
            } else {
                // todo download the image
                holder.image.setImageResource(R.drawable.img);
            }
        } else { // place holder
            holder.image.setImageResource(R.drawable.img);
        }
    }

    @Override
    public void onRecycleViewHolder(@NonNull ViewHolder holder) {
        super.onRecycleViewHolder(holder);
        if (holder.gesturesDisabled) {
            holder.image.getGestureController().getSettings().enableGestures();
            holder.gesturesDisabled = false;
        }

        //clear image
        holder.progress.animate().cancel();
        holder.progress.setAlpha(0f);
        holder.image.setImageDrawable(null);
    }

    public static GImageView getGestureView(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }

    class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        GImageView image;
        View progress;

        boolean gesturesDisabled;

        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
            progress = itemView.findViewById(R.id.pb);
            image = (GImageView) itemView.findViewById(R.id.cross_to);
            image.getGestureController().getSettings().enableGestures();
            image.getGestureController().enableScrollInViewPager(viewPager);
            image.getGestureController().getSettings().setAnimationsDuration(240);
            image.getGestureController().getSettings().setMaxZoom(10f).setDoubleTapZoom(3f);
            image.getPositionAnimator().addPositionUpdateListener(new PositionUpdateListener() {
                @Override
                public void onPositionUpdate(float position, boolean isLeaving) {
                    progress.setVisibility(position == 1f ? View.VISIBLE : View.INVISIBLE);
                }
            });
        }
    }

}
