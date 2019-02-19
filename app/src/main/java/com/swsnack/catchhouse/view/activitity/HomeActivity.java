package com.swsnack.catchhouse.view.activitity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityHomeBinding;
import com.swsnack.catchhouse.view.BaseActivity;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    float topFirstTouchY;
    float topY;
    float bottomFirstTouchY;
    float bottomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topY = getBinding().lyTop.getY();
        bottomY = getWindowManager().getDefaultDisplay().getHeight() / 2;
        getBinding().lyTop.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        topFirstTouchY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if(event.getRawY() >= topFirstTouchY) {
                            return true;
                        }

                        if(topFirstTouchY - event.getRawY() > getWindowManager().getDefaultDisplay().getHeight() / 3) {
                            Intent intent = new Intent(getApplicationContext(), BottomNavActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            return false;
                        }

                        getBinding().lyTop.animate()
                                .y(event.getRawY() - topFirstTouchY)
                                .setDuration(0)
                                .start();

                        getBinding().lyBottom.animate()
                            .y(bottomY + (topFirstTouchY - event.getRawY()))
                            .setDuration(0)
                            .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        getBinding().lyTop.animate()
                                .y(topY)
                                .setDuration(0)
                                .start();

                        getBinding().lyBottom.animate()
                                .y(bottomY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        getBinding().lyBottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bottomFirstTouchY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if(event.getRawY() <= bottomFirstTouchY) {
                            return true;
                        }
                        getBinding().lyBottom.animate()
                                .y(bottomY + (event.getRawY() - bottomFirstTouchY))
                                .setDuration(0)
                                .start();

                        getBinding().lyTop.animate()
                                .y(topY + (bottomFirstTouchY - event.getRawY()))
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        getBinding().lyBottom.animate()
                                .y(bottomY)
                                .setDuration(0)
                                .start();

                        getBinding().lyTop.animate()
                                .y(topY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }

}
