package com.example.leonard.picbrowser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

import com.alexvasilkov.events.Events;
import com.alexvasilkov.gestures.animation.ViewPosition;

public class MainActivity extends BaseActivity {

    public static final String EVENT_SHOW_IMAGE = "show_image";
    public static final String EVENT_POSITION_CHANGED = "position_changed";

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing views
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image = (ImageView) findViewById(R.id.cross_from);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                // Requesting opening image in new activity and animating it from current position.
                ViewPosition position = ViewPosition.from(view);
                ShareActivity.open(MainActivity.this, position, 0);
            }
        });

        // Image position may change (i.e. when screen orientation is changed), so we should update
        // fullscreen image to ensure exit animation will return image into correct position.
        image.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Notifying fullscreen image activity about image position changes.
                ViewPosition position = ViewPosition.from(image);
                Events.create(EVENT_POSITION_CHANGED).param(position).post();
            }
        });

        // Loading image
    }

    @Events.Subscribe(EVENT_SHOW_IMAGE)
    private void showImage(boolean show) {
        // Fullscreen activity requested to show or hide original image
        image.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

}