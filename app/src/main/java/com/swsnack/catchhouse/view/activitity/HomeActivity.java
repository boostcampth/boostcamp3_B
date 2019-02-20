package com.swsnack.catchhouse.view.activitity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skt.Tmap.TMapTapi;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityHomeBinding;
import com.swsnack.catchhouse.view.BaseActivity;

import static com.swsnack.catchhouse.Constant.ParcelableData.BOTTOM_NAVIGATION_POSITION;


public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    float firstTouchY;
    float topY;
    float bottomY;
    private long firstBackPressedTime;
    FirebaseUser uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstBackPressedTime = 0;
        topY = getBinding().lyTop.getY();
        bottomY = getWindowManager().getDefaultDisplay().getHeight() / 2;
        uuid = FirebaseAuth.getInstance().getCurrentUser();

        if (uuid == null) {
            getBinding().btLogin.setVisibility(View.VISIBLE);
        } else {
            getBinding().btLogin.setVisibility(View.INVISIBLE);
        }

        TMapTapi tApi = new TMapTapi(getApplicationContext());
        tApi.setSKTMapAuthentication(getResources().getString(R.string.tmap_api_key));

        getBinding().clRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        firstTouchY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (event.getRawY() >= firstTouchY) {
                            getBinding().lyTop.animate()
                                    .y(firstTouchY - event.getRawY())
                                    .setDuration(0)
                                    .start();

                            getBinding().lyBottom.animate()
                                    .y(bottomY + (event.getRawY() - firstTouchY))
                                    .setDuration(0)
                                    .start();
                        } else {
                            getBinding().lyTop.animate()
                                    .y(event.getRawY() - firstTouchY)
                                    .setDuration(0)
                                    .start();

                            getBinding().lyBottom.animate()
                                    .y(bottomY + (firstTouchY - event.getRawY()))
                                    .setDuration(0)
                                    .start();
                        }
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

                        if (firstTouchY - event.getRawY() > getWindowManager().getDefaultDisplay().getHeight() / 3) {
                            freezeUI();
                            Intent intent = new Intent(getApplicationContext(), BottomNavActivity.class);
                            intent.putExtra(BOTTOM_NAVIGATION_POSITION, 1);
                            startActivity(intent);

                            return true;
                        } else if (event.getRawY() - firstTouchY > getWindowManager().getDefaultDisplay().getHeight() / 3) {
                            if (uuid == null) {
                                Snackbar.make(getBinding().getRoot(), R.string.not_singed, Snackbar.LENGTH_SHORT).show();
                                return true;
                            }
                            freezeUI();
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

        getBinding().btLogin.setOnClickListener(__ -> {
                    Intent intent = new Intent(getApplicationContext(), BottomNavActivity.class);
                    intent.putExtra(BOTTOM_NAVIGATION_POSITION, 0);
                    startActivity(intent);
                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        unFreezeUI();
        uuid = FirebaseAuth.getInstance().getCurrentUser();

        if (uuid == null) {
            getBinding().btLogin.setVisibility(View.VISIBLE);
        } else {
            getBinding().btLogin.setVisibility(View.INVISIBLE);
        }
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
