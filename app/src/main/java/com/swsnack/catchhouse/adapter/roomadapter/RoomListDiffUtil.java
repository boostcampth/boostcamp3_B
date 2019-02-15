package com.swsnack.catchhouse.adapter.roomadapter;

import com.swsnack.catchhouse.adapter.BaseDiffUtil;
import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

public class RoomListDiffUtil extends BaseDiffUtil<RoomEntity> {

    RoomListDiffUtil(List<RoomEntity> mOldList, List<RoomEntity> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getRoomUid().equals(mNewList.get(newItemPosition).getRoomUid());
    }
}
