package com.swsnack.catchhouse.adapter.roomadapter;

import com.swsnack.catchhouse.adapter.BaseDiffUtil;
import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public class RoomListDiffUtil extends BaseDiffUtil<Room> {

    RoomListDiffUtil(List<Room> mOldList, List<Room> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getKey().equals(mNewList.get(newItemPosition).getKey());
    }
}
