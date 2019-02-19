package com.swsnack.catchhouse.view.activitity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityHomeBinding;
import com.swsnack.catchhouse.view.BaseActivity;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.SEARCH;


public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    float topFirstTouchY;
    float topY;
    float bottomFirstTouchY;
    float bottomY;
    private long firstBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstBackPressedTime = 0;
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
                        if (event.getRawY() >= topFirstTouchY) {
                            return true;
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
                        if (topFirstTouchY - event.getRawY() > getWindowManager().getDefaultDisplay().getHeight() / 4) {
                            Intent intent = new Intent(getApplicationContext(), BottomNavActivity.class);
                            intent.putExtra(SEARCH, SEARCH);
                            startActivity(intent);
                            return true;
                        }
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
                        if (event.getRawY() <= bottomFirstTouchY) {
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

                        if (event.getRawY() - bottomFirstTouchY > getWindowManager().getDefaultDisplay().getHeight() / 4) {
                            if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                                Snackbar.make(getBinding().getRoot(), R.string.not_singed, Snackbar.LENGTH_SHORT).show();
                                return true;
                            }
                            startActivity(new Intent(getApplicationContext(), WriteActivity.class));
                            return true;
                        }
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

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > firstBackPressedTime + 2000) {
            firstBackPressedTime = System.currentTimeMillis();
            Snackbar.make(getBinding().getRoot(), getString(R.string.snack_back_pressed), Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= firstBackPressedTime + 2000) {
            finish();
        }
    }
}
