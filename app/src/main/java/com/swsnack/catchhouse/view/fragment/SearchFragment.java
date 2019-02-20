package com.swsnack.catchhouse.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.adapter.RoomCardListAdapter;
import com.swsnack.catchhouse.adapter.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.databinding.FragmentSearchBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.PostActivity;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

import static com.swsnack.catchhouse.Constant.INTENT_ROOM;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchingViewModel> {
    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;

    @Override
    protected int getLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected Class<SearchingViewModel> getViewModelClass() {
        return SearchingViewModel.class;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().nmMap.onCreate(savedInstanceState);

        mFragmentManager = getActivity().getSupportFragmentManager();
        mDisposable = new CompositeDisposable();

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(this);
        getBinding().rvMapAddress.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        getBinding().rvMapAddress.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        AddressListAdapter adapter = new AddressListAdapter(getContext());

        getBinding().rvMapRoom.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        RoomCardListAdapter roomCardListAdapter = new RoomCardListAdapter(getContext());

        // Change floating button color
        getBinding().fbMapFilter.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        getBinding().rvMapAddress.setAdapter(adapter);
        getBinding().rvMapAddress.bringToFront();
        getBinding().rvMapRoom.setAdapter(roomCardListAdapter);
        getBinding().rvMapRoom.setVisibility(View.VISIBLE);
        getBinding().rvMapRoom.bringToFront();

        getBinding().nmMap.getMapAsync(getViewModel());


        getBinding().nmMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(getViewModel().isCardShow().getValue() == true) {
                            getViewModel().setCardShow(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:



                        break;
                }
                return false;
            }
        });

        getBinding().fbMapFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("csh", "asd");
            }
        });

        getBinding().svMap.setOnClickListener((v -> {
            if(getViewModel().isShowingCardView().getValue() == true) {
                getViewModel().setCardShow(false);
                getViewModel().setFinish(true);
            }
        }));

        getBinding().svMap.setOnSearchClickListener((v -> {
            if(getViewModel().isShowingCardView().getValue() == true) {
                getViewModel().setCardShow(false);
                getViewModel().setFinish(true);
            }
        }));

        getBinding().svMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                getBinding().svMap.clearFocus();
                getViewModel().setKeyword(getBinding().svMap.getQuery().toString());
                getViewModel().searchAddress();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length() == 0) {
                    getBinding().rvMapAddress.setVisibility(View.GONE);
                }
                return false;
            }
        });

        getBinding().svMap.setOnCloseListener(() -> {
            getBinding().rvMapAddress.setVisibility(View.GONE);
            return true;
        });



        adapter.setOnItemClickListener(((v, position) -> {
            getBinding().rvMapAddress.setVisibility(View.GONE);
            getViewModel().updateData(adapter.getItem(position).getLatitude(), adapter.getItem(position).getLongitude());
        }));

        roomCardListAdapter.setOnItemClickListener(((v, position) -> {
            Intent intent = new Intent(getActivity(), PostActivity.class);
            intent.putExtra(INTENT_ROOM, roomCardListAdapter.getItem(position));
            startActivity(intent);
        }));

        getBinding().fbMapFilter.setOnClickListener(v -> {
            new FilterFragment().show(mFragmentManager, "address selection");
        });
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.FILTER) {
            Log.v("csh","필터옴");
        }
    }
*/
    @Override
    public void onStart() {
        super.onStart();
        getBinding().nmMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBinding().nmMap.onResume();
        getViewModel().onUpdateMap();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getBinding().nmMap.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        getBinding().nmMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        getBinding().nmMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        getBinding().nmMap.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getBinding().nmMap.onDestroy();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    /*

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
*/


}