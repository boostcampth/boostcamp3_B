package com.swsnack.catchhouse.view.activitity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.chattingdata.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.locationdata.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.roomdata.remote.AppRoomDataManager;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.databinding.ActivityPostBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModel;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModelFactory;

public class PostActivity extends BaseActivity<ActivityPostBinding> {

    private PostViewModel mViewModel;

    @Override
    protected int getLayout() {
        return R.layout.activity_post;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();

        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
    }

    private void createViewModels() {
        mViewModel = ViewModelProviders.of(this,
                new PostViewModelFactory(
                        AppDataManager.getInstance(
                                AppAPIManager.getInstance(),
                                AppUserDataManager.getInstance(),
                                RemoteChattingManager.getInstance(),
                                AppRoomDataManager.getInstance(),
                                AppLocationDataManager.getInstance()
                        ),
                        this
                )).get(PostViewModel.class);
    }
}
