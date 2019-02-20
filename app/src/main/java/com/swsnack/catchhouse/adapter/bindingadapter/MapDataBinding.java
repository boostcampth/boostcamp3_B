package com.swsnack.catchhouse.adapter.bindingadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.adapter.RoomCardListAdapter;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.view.activitity.PostActivity;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.recyclerview.widget.RecyclerView;

import static com.swsnack.catchhouse.Constant.INTENT_ROOM;

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

    // @BindingAdapter("isCardShow")
    @BindingAdapter({"isCardShow", "bindViewModel"})
    public static void isCardShow(RecyclerView recyclerView, boolean cardShow, SearchingViewModel viewModel) {

        if (recyclerView == null)
            return;

        if (cardShow == true) {

            Log.v("csh", "true");
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
            Log.v("csh", "false");
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
/*
            if(viewModel.getSelectedInfo()!=null) {
                viewModel.clearInfo();
                //viewModel.getSelectedInfo().getValue().close();
            }*/

        }
    }

    @BindingAdapter({"setNaverMapMarker", "bindViewModel"})
    public static void setNaverMapMarker(MapView mapView, List<Marker> markerList, SearchingViewModel viewModel) {
        Log.v("csh", "setMarker");
        if (markerList == null || markerList.size() == 0) {
            return;
        }

        mapView.getMapAsync(naverMap -> {
            for (int i = 0; i < markerList.size(); i++) {

                Marker marker = markerList.get(i);
                InfoWindow infoWindow = new InfoWindow();
                /*
                infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(mapView.getContext()) {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow) {
                        Room room = (Room) infoWindow.getMarker().getTag();
                        String string = room.getFrom().substring(2)+" ~ "+room.getTo().substring(2) ;
                        return string;
                    }
                });*/
                infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(mapView.getContext()) {
                    @NonNull
                    @Override
                    protected View getContentView(@NonNull InfoWindow infoWindow) {

                        //Typeface typeface = getContext().getResources().getFont(R.font.youth);
                        // textView.setTypeface(typeface);

                        Room room = (Room) marker.getTag();

                        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.item_map_selected, null);
                        TextView address = view.findViewById(R.id.tv_selected_address);
                        TextView size = view.findViewById(R.id.tv_selected_size);
                        TextView date = view.findViewById(R.id.tv_selected_date);

                        address.setText(room.getAddress());
                        size.setText(room.getSize() + "평");
                        date.setText(room.getFrom().substring(2) + " ~ " + room.getTo().substring(2));
                        infoWindow.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                /* TODO: ViewModel에 Mutable 선언해서 View에서 observe한후 view에서 인텐트 열기 */
                                Intent intent = new Intent(AppApplication.getAppContext(), PostActivity.class);
                                intent.putExtra(INTENT_ROOM, room);
                                AppApplication.getAppContext().startActivity(intent);
                                return false;
                            }
                        });
                        return view;
                    }
                });


                marker.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {

                        viewModel.onClickMarker(marker, infoWindow);
                        CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(new CameraPosition(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), 15));
                        cameraUpdate.animate(CameraAnimation.Fly);
                        naverMap.moveCamera(cameraUpdate);
                        infoWindow.open(marker);
                        return false;
                    }
                });
                marker.setIcon(MarkerIcons.YELLOW);
                marker.setMap(naverMap);
                marker.setWidth(100);
                marker.setHeight(150);
                //infoWindow.open(marker);
                Log.v("csh", "" + marker.getPosition().latitude);
            }
        });

    }


    @BindingAdapter({"setCircle"})
    public static void setCircle(MapView mapView, CircleOverlay circle) {
        if (circle == null) {
            return;
        }

        mapView.getMapAsync(naverMap -> {
            circle.setMap(naverMap);
        });

    }

    @BindingAdapter("setPosition")
    public static void setPosition(MapView mapView, LatLngBounds pos) {
        if (pos == null) {
            return;
        }
        mapView.getMapAsync(naverMap -> {
            //CameraUpdate cameraUpdate = CameraUpdate.scrollTo(pos);
            Log.v("csh", "setPosition!!!!!!!!!!!!");

            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(pos, 500);


            cameraUpdate.animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);

        });
    }
}
