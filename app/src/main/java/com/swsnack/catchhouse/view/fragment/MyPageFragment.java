package com.swsnack.catchhouse.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.roomadapter.FavoriteRoomAdapter;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.databinding.DialogChangeNickNameBinding;
import com.swsnack.catchhouse.databinding.DialogChangePasswordBinding;
import com.swsnack.catchhouse.databinding.FragmentMyPageBinding;
import com.swsnack.catchhouse.util.DataConverter;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.PostActivity;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.swsnack.catchhouse.Constant.GALLERY;
import static com.swsnack.catchhouse.Constant.INTENT_ROOM;
import static com.swsnack.catchhouse.Constant.SignInMethod.FACEBOOK;
import static com.swsnack.catchhouse.Constant.SignInMethod.GOOGLE;

public class MyPageFragment extends BaseFragment<FragmentMyPageBinding, UserViewModel> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_page;
    }

    @Override
    protected Class<UserViewModel> getViewModelClass() {
        return UserViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());
        getViewModel().getUserData();

        getBinding().ctlMyPage.setExpandedTitleColor(Color.TRANSPARENT);
        getBinding().ctlMyPage.setCollapsedTitleTextColor(Color.WHITE);

        for (String signInMethod : FirebaseAuth.getInstance().getCurrentUser().getProviders()) {
            if (signInMethod.equals(FACEBOOK) || signInMethod.equals(GOOGLE)) {
                getBinding().tvMyPageChangePassword.setVisibility(View.GONE);
                break;
            }
        }

        FavoriteRoomAdapter favoriteRoomAdapter = new FavoriteRoomAdapter(getContext(), getViewModel());
        getBinding().lyMyPageInclude.rvMyPageMyFavorite.setAdapter(favoriteRoomAdapter);
        getBinding().lyMyPageInclude.rvMyPageMyFavorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        favoriteRoomAdapter.setOnItemClickListener((viewHolder, position) -> {
            Room room = DataConverter.convertToRoom(favoriteRoomAdapter.getItem(position));
            startActivity(new Intent(getContext(), PostActivity.class).putExtra(INTENT_ROOM, room));
        });

        getBinding().tvMyPageChangeNickName.setOnClickListener(v -> {
            DialogChangeNickNameBinding dialogBinding = DialogChangeNickNameBinding.inflate(getLayoutInflater());
            dialogBinding.setHandler(getViewModel());

            Dialog dialogChangeNickName = new Dialog(getContext());
            dialogChangeNickName.setContentView(dialogBinding.getRoot());
            dialogChangeNickName.show();

            dialogBinding.tvDialogChangeNickNameNegative.setOnClickListener(negative -> dialogChangeNickName.dismiss());
            dialogBinding.tvDialogChangeNickNamePositive.setOnClickListener(positive -> {
                getViewModel().changeNickName(dialogBinding.etDialogChangeNickName.getText().toString());
                dialogChangeNickName.dismiss();
            });
        });

        getBinding().tvMyPageChangePassword.setOnClickListener(v -> {
            DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(getLayoutInflater());
            dialogBinding.setHandler(getViewModel());

            Dialog dialogChangePassword = new Dialog(getContext());
            dialogChangePassword.setContentView(dialogBinding.getRoot());
            dialogChangePassword.show();

            dialogBinding.tvDialogChangePasswordNegative.setOnClickListener(negative -> dialogChangePassword.dismiss());
            dialogBinding.tvDialogChangePasswordPositive.setOnClickListener(positive -> {
                if (!dialogBinding.etDialogChangePasswordNewPassword.getText().toString()
                        .equals(dialogBinding.etDialogChangePasswordNewPasswordConfirm.getText().toString())) {
                    Snackbar.make(getBinding().getRoot(), R.string.snack_wrong_password, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                getViewModel().updatePassword(dialogBinding.etDialogChangePasswordExPassword.getText().toString(),
                        dialogBinding.etDialogChangePasswordNewPassword.getText().toString());

                dialogChangePassword.dismiss();
            });
        });

        getBinding().tvMyPageChangeProfile.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), GALLERY));

        getBinding().lyMyPageInclude.tvMyPageRecentlyVisitSubTitle.setVisibility(View.GONE);
        getBinding().lyMyPageInclude.tvMyPageMyFavoriteSubTitle.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().getFavoriteRoom();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                getViewModel().updateProfile(data.getData());
            }
        }
    }
}
