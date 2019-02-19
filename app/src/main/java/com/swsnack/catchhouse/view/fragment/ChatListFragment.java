package com.swsnack.catchhouse.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingListAdapter;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingListItemHolder;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.databinding.DialogChangeNickNameBinding;
import com.swsnack.catchhouse.databinding.DialogChangePasswordBinding;
import com.swsnack.catchhouse.databinding.FragmentChatListBinding;
import com.swsnack.catchhouse.databinding.ItemNavHeaderBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.BottomNavActivity;
import com.swsnack.catchhouse.view.activitity.ChattingMessageActivity;
import com.swsnack.catchhouse.view.activitity.WriteActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.swsnack.catchhouse.Constant.GALLERY;
import static com.swsnack.catchhouse.Constant.ParcelableData.CHATTING_DATA;
import static com.swsnack.catchhouse.Constant.ParcelableData.USER_DATA;
import static com.swsnack.catchhouse.Constant.SignInMethod.FACEBOOK;
import static com.swsnack.catchhouse.Constant.SignInMethod.GOOGLE;
import static com.swsnack.catchhouse.Constant.Ucrop.UCROP_HEIGHT_MAX;
import static com.swsnack.catchhouse.Constant.Ucrop.UCROP_SQUARE;
import static com.swsnack.catchhouse.Constant.Ucrop.UCROP_WIDTH_MAX;

public class ChatListFragment extends BaseFragment<FragmentChatListBinding, ChattingViewModel> {

    private UserViewModel mUserViewModel;

    @Override
    protected int getLayout() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected Class<ChattingViewModel> getViewModelClass() {
        return ChattingViewModel.class;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavActivity) {
            ((BottomNavActivity) Objects.requireNonNull(getActivity())).setViewPagerListener(this::getChattingList);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        setNavigationDrawer();

        getBinding().setHandler(getViewModel());
        ChattingListAdapter chattingListAdapter = new ChattingListAdapter(getContext(), getViewModel());
        getBinding().rvChatList.setAdapter(chattingListAdapter);
        getBinding().rvChatList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        chattingListAdapter.setOnItemClickListener((v, position) -> {
            Chatting chatting = chattingListAdapter.getItem(position);
            User user = ((ChattingListItemHolder) v).getBinding().getUserData();

            startActivity(
                    new Intent(getContext(),
                            ChattingMessageActivity.class)
                            .putExtra(CHATTING_DATA, chatting)
                            .putExtra(USER_DATA, user));
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().getChattingRoomList();
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModel().cancelChattingListChangingListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.cancelChattingListChangingListening();
    }

    private void getChattingList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getBinding().tvChatListNotSigned.setVisibility(View.VISIBLE);
            getViewModel().setChattingList(new ArrayList<>());
        } else {
            getViewModel().cancelChattingListChangingListening();
            getViewModel().getChattingRoomList();
            getBinding().tvChatListNotSigned.setVisibility(View.GONE);
        }
    }

    private void setNavigationDrawer() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(getBinding().tbChatList);

        ItemNavHeaderBinding itemNavHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_nav_header, getBinding().navView, false);
        itemNavHeaderBinding.setUserViewModel(mUserViewModel);
        itemNavHeaderBinding.setLifecycleOwner(this);
        getBinding().navView.addHeaderView(itemNavHeaderBinding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getBinding().navView.inflateMenu(R.menu.navigation_menu_firebase);
        } else {
            String signInMethod = FirebaseAuth.getInstance().getCurrentUser().getProviders().get(0);
            if (signInMethod.equals(FACEBOOK) || signInMethod.equals(GOOGLE)) {
                getBinding().navView.inflateMenu(R.menu.navigation_menu_sns);
            } else {
                getBinding().navView.inflateMenu(R.menu.navigation_menu_firebase);
            }
        }

        itemNavHeaderBinding.navHeaderBack.setOnClickListener(__ ->
                getBinding().drawerLayout.closeDrawer(GravityCompat.END));

        getBinding().navView.setNavigationItemSelectedListener(item -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                Snackbar.make(getBinding().getRoot(), R.string.not_singed, Snackbar.LENGTH_SHORT).show();
                return true;
            }

            switch (item.getItemId()) {
                case R.id.write:
                    startActivity(new Intent(getContext(), WriteActivity.class));
                    getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                    break;

                case R.id.change_nickname:
                    getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                    DialogChangeNickNameBinding changeNickNameBinding = DialogChangeNickNameBinding.inflate(getLayoutInflater());
                    changeNickNameBinding.setHandler(mUserViewModel);

                    Dialog dialogChangeNickName = new Dialog(getContext());
                    dialogChangeNickName.setContentView(changeNickNameBinding.getRoot());
                    dialogChangeNickName.show();

                    changeNickNameBinding.btnDialogChangeNickNameNegative.setOnClickListener(negative -> dialogChangeNickName.dismiss());
                    changeNickNameBinding.btnDialogChangeNickNamePositive.setOnClickListener(positive -> {
                        mUserViewModel.changeNickName(changeNickNameBinding.etDialogChangeNickName.getText().toString());
                        dialogChangeNickName.dismiss();
                    });
                    break;

                case R.id.change_password:
                    getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                    DialogChangePasswordBinding changePasswordBinding = DialogChangePasswordBinding.inflate(getLayoutInflater());
                    changePasswordBinding.setHandler(mUserViewModel);

                    Dialog dialogChangePassword = new Dialog(getContext());
                    dialogChangePassword.setContentView(changePasswordBinding.getRoot());
                    dialogChangePassword.show();

                    changePasswordBinding.tvDialogChangePasswordNegative.setOnClickListener(negative -> dialogChangePassword.dismiss());
                    changePasswordBinding.tvDialogChangePasswordPositive.setOnClickListener(positive -> {
                        if (!changePasswordBinding.etDialogChangePasswordNewPassword.getText().toString()
                                .equals(changePasswordBinding.etDialogChangePasswordNewPasswordConfirm.getText().toString())) {
                            Snackbar.make(getBinding().getRoot(), R.string.snack_wrong_password, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        mUserViewModel.updatePassword(changePasswordBinding.etDialogChangePasswordExPassword.getText().toString(),
                                changePasswordBinding.etDialogChangePasswordNewPassword.getText().toString());

                        dialogChangePassword.dismiss();
                    });
                    break;

                case R.id.change_profile:
                    getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                    startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), GALLERY);
                    break;

                case R.id.delete_account:
                    getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.my_page_delete_user)
                            .setMessage(R.string.dialog_delete_user)
                            .setPositiveButton(R.string.dialog_positive, (dialog, which) -> mUserViewModel.deleteUser())
                            .setNeutralButton(R.string.negative, ((dialog, which) -> dialog.dismiss()))
                            .create().show();
                    break;

                case R.id.logout:
                    getBinding().drawerLayout.getHandler().post(() -> getBinding().drawerLayout.closeDrawers());
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.my_page_sign_out)
                            .setMessage(R.string.dialog_sign_out)
                            .setPositiveButton(R.string.dialog_positive, (dialog, which) -> mUserViewModel.signOut())
                            .setNeutralButton(R.string.negative, ((dialog, which) -> dialog.dismiss()))
                            .create().show();
                    break;
            }
            return true;
        });
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
            mUserViewModel.updateProfile(UCrop.getOutput(data));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            getBinding().drawerLayout.openDrawer(GravityCompat.END);
            return true;
        }
        return true;
    }
}