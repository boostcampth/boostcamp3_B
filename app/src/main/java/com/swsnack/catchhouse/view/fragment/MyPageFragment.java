package com.swsnack.catchhouse.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.roomadapter.RoomListAdapter;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.databinding.DialogChangeNickNameBinding;
import com.swsnack.catchhouse.databinding.DialogChangePasswordBinding;
import com.swsnack.catchhouse.databinding.FragmentMyPageBinding;
import com.swsnack.catchhouse.repository.favoriteroom.FavoriteRoomRepositoryImpl;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.PostActivity;
import com.swsnack.catchhouse.viewmodel.favoriteroomviewmodel.FavoriteRoomViewModel;
import com.swsnack.catchhouse.viewmodel.favoriteroomviewmodel.FavoriteRoomViewModelFactory;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.swsnack.catchhouse.Constant.INTENT_ROOM;
import static com.swsnack.catchhouse.Constant.RequestCode.GALLERY;
import static com.swsnack.catchhouse.Constant.SignInMethod.FACEBOOK;
import static com.swsnack.catchhouse.Constant.SignInMethod.GOOGLE;
import static com.swsnack.catchhouse.Constant.Ucrop.UCROP_HEIGHT_MAX;
import static com.swsnack.catchhouse.Constant.Ucrop.UCROP_SQUARE;
import static com.swsnack.catchhouse.Constant.Ucrop.UCROP_WIDTH_MAX;

public class MyPageFragment extends BaseFragment<FragmentMyPageBinding, UserViewModel> {

    private FavoriteRoomViewModel mFavoriteRoomViewModel;

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

        init();

        getBinding().setHandler(getViewModel());
        getBinding().setFavoriteRoom(mFavoriteRoomViewModel);
        getViewModel().getUserData();

        for (String signInMethod : FirebaseAuth.getInstance().getCurrentUser().getProviders()) {
            if (signInMethod.equals(FACEBOOK) || signInMethod.equals(GOOGLE)) {
                getBinding().tvMyPageChangePassword.setVisibility(View.GONE);
                break;
            }
        }

        RoomListAdapter favoriteRoomListAdapter = new RoomListAdapter(getContext(), getViewModel());
        getBinding().lyMyPageInclude.rvMyPageMyFavorite.setAdapter(favoriteRoomListAdapter);
        getBinding().lyMyPageInclude.rvMyPageMyFavorite
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        favoriteRoomListAdapter.setOnItemClickListener((viewHolder, position) -> {
            Room room = favoriteRoomListAdapter.getItem(position);
            startActivity(new Intent(getContext(), PostActivity.class).putExtra(INTENT_ROOM, room));
        });

        RoomListAdapter recentRoomListAdapter = new RoomListAdapter(getContext(), getViewModel());
        getBinding().lyMyPageInclude.rvMyPageRecentlyVisit.setAdapter(recentRoomListAdapter);
        getBinding().lyMyPageInclude.rvMyPageRecentlyVisit
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recentRoomListAdapter.setOnItemClickListener(((viewHolder, position) -> {
            Room room = recentRoomListAdapter.getItem(position);
            startActivity(new Intent(getContext(), PostActivity.class).putExtra(INTENT_ROOM, room));
        }));

        RoomListAdapter sellRoomListAdapter = new RoomListAdapter(getContext(), getViewModel());
        getBinding().lyMyPageInclude.rvMyPageMySell.setAdapter(sellRoomListAdapter);
        getBinding().lyMyPageInclude.rvMyPageMySell
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        sellRoomListAdapter.setOnItemClickListener(((viewHolder, position) -> {
            Room room = sellRoomListAdapter.getItem(position);
            startActivity(new Intent(getContext(), PostActivity.class).putExtra(INTENT_ROOM, room));
        }));

        getBinding().tvMyPageChangeNickName.setOnClickListener(v -> onChangeNickNameBtnClicked());

        getBinding().tvMyPageChangePassword.setOnClickListener(v -> onChangePasswordBtnClicked());

        getBinding().tvMyPageChangeProfile.setOnClickListener(v ->
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), GALLERY));

        getBinding().tvMyPageDelete.setOnClickListener(v -> onDeleteUserBtnClicked());

        getBinding().tvMyPageSignOut.setOnClickListener(v -> onSignOutBtnClicked());
    }

    private void init() {
        getBinding().ctlMyPage.setExpandedTitleColor(Color.TRANSPARENT);
        getBinding().ctlMyPage.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));

        mFavoriteRoomViewModel = ViewModelProviders.of(this,
                new FavoriteRoomViewModelFactory(FavoriteRoomRepositoryImpl.getInstance()))
                .get(FavoriteRoomViewModel.class);
    }

    private void onChangeNickNameBtnClicked() {
        DialogChangeNickNameBinding dialogBinding = DialogChangeNickNameBinding.inflate(getLayoutInflater());
        dialogBinding.setHandler(getViewModel());

        Dialog dialogChangeNickName = new Dialog(getContext());
        dialogChangeNickName.setContentView(dialogBinding.getRoot());
        dialogChangeNickName.show();

        dialogBinding.btnDialogChangeNickNameNegative.setOnClickListener(negative -> dialogChangeNickName.dismiss());
        dialogBinding.btnDialogChangeNickNamePositive.setOnClickListener(positive -> {
            getViewModel().changeNickName(dialogBinding.etDialogChangeNickName.getText().toString());
            dialogChangeNickName.dismiss();
        });
    }

    private void onChangePasswordBtnClicked() {
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
    }

    private void onDeleteUserBtnClicked() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.my_page_delete_user)
                .setMessage(R.string.dialog_delete_user)
                .setPositiveButton(R.string.dialog_positive, (dialog, which) -> getViewModel().deleteUser())
                .setNeutralButton(R.string.negative, ((dialog, which) -> dialog.dismiss()))
                .create().show();
    }

    private void onSignOutBtnClicked() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.my_page_sign_out)
                .setMessage(R.string.dialog_sign_out)
                .setPositiveButton(R.string.dialog_positive, (dialog, which) -> getViewModel().signOut())
                .setNeutralButton(R.string.negative, ((dialog, which) -> dialog.dismiss()))
                .create().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFavoriteRoomViewModel.getFavoriteRoom();
//        getViewModel().getFavoriteRoom();
//        getViewModel().getRecentRoom();
//        getViewModel().getSellRoom();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), fileName + "png"));
            UCrop.of(data.getData(), destinationUri)
                    .withAspectRatio(UCROP_SQUARE, UCROP_SQUARE)
                    .withMaxResultSize(UCROP_WIDTH_MAX, UCROP_HEIGHT_MAX)
                    .start(getActivity(), this);

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            getViewModel().updateProfile(UCrop.getOutput(data));
        }
    }
}
