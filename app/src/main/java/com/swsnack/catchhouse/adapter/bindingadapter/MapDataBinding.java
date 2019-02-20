package com.swsnack.catchhouse.adapter.bindingadapter;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.adapter.RoomCardListAdapter;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.RoomCard;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import java.util.List;

@InverseBindingMethods({
        @InverseBindingMethod(type = SeekBar.class, attribute = "android:progress"),
})
public class MapDataBinding {

    @BindingAdapter({"setAddressList"})
    public static void setAddressList(RecyclerView recyclerView, List<Address> addressList) {
        AddressListAdapter addressListAdapter = (AddressListAdapter) recyclerView.getAdapter();
        addressListAdapter.updateItems(addressList);
    }

    @BindingAdapter({"setRoomCardList"})
    public static void setRoomCardList(RecyclerView recyclerView, List<Room> roomCardList) {
        RoomCardListAdapter roomCardListAdapter = (RoomCardListAdapter) recyclerView.getAdapter();
        roomCardListAdapter.updateItems(roomCardList);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:checked")
    public static void setChecked(CompoundButton checkableView, Boolean isChecked) {
        checkableView.setChecked(isChecked != null ? isChecked : false);
    }

    @BindingAdapter("android:progress")
    public static void setProgress(SeekBar view, int progress) {
        if (progress != view.getProgress()) {
            view.setProgress(progress);
        }
    }

    @BindingAdapter("imageBitmap")
    public static void setImageBitmap(ImageView imageView, String uri) {
        Log.v("csh", "이미지들어옴" + uri);

        Glide.with(AppApplication.getAppContext()).asBitmap().load(uri).apply(new RequestOptions().override(120 * 2, 80 * 2).centerCrop()) // .override(320, 180).
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setImageBitmap(resource);
                        return false;
                    }
                }).submit();
    }

    @BindingAdapter("adapter")
    public static void setRecyclerItem(RecyclerView recyclerview, List<Room> items) {
        RoomCardListAdapter adapter = (RoomCardListAdapter) recyclerview.getAdapter();

        if (adapter != null && items != null) {
            adapter.updateItems(items);
        }

    }

    @BindingAdapter("isCardShow")
    public static void isCardShow(RecyclerView recyclerView, boolean cardShow) {

        if (recyclerView == null)
            return;

        if (cardShow == true) {
            recyclerView.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    recyclerView.getHeight() + 20,  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            recyclerView.startAnimation(animate);
        } else {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    recyclerView.getHeight() + 100); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(false);

            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    recyclerView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            recyclerView.startAnimation(animate);
        }
    }

    @BindingAdapter({"setNaverMapMarker", "bindViewModel"})
    public static void setMarker(MapView mapView, List<Marker> markerList, SearchingViewModel viewModel) {
        Log.v("csh", "setMarker");
        if (markerList == null || markerList.size() == 0) {
            return;
        }

        mapView.getMapAsync(naverMap -> {
            for (int i = 0; i < markerList.size(); i++) {

                Marker marker = markerList.get(i);
                InfoWindow infoWindow = new InfoWindow();
                infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(mapView.getContext()) {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow) {
                        Room room = (Room) infoWindow.getMarker().getTag();
                        return room.getSize() + "평";
                    }
                });


                marker.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        viewModel.onClickMarker(String.valueOf(marker.getPosition().latitude));
                        if (marker.getInfoWindow() == null) {


                        }

                        return false;
                    }
                });
                marker.setIcon(MarkerIcons.YELLOW);
                marker.setMap(naverMap);
                marker.setWidth(100);
                marker.setHeight(150);
                infoWindow.open(marker);
                Log.v("csh", "" + marker.getPosition().latitude);
            }
        });

    }

    @BindingAdapter("setPosition")
    public static void setPosition(MapView mapView, LatLng pos) {
        mapView.getMapAsync(naverMap -> {
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(pos);
            cameraUpdate.animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }
}
