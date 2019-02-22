package com.swsnack.catchhouse.adapter.chattingadapter;


import com.swsnack.catchhouse.adapter.BaseDiffUtil;
import com.swsnack.catchhouse.data.model.Chatting;

import java.util.List;

public class ChattingDiffUtil extends BaseDiffUtil<Chatting> {

    ChattingDiffUtil(List<Chatting> mOldList, List<Chatting> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areItemsTheSame(int oldListIndex, int newListIndex) {
        return mOldList.get(oldListIndex).getRoomUid().equals(mNewList.get(newListIndex).getRoomUid());
    }

    @Override
    public boolean areContentsTheSame(int oldMessageIndex, int newMessageIndex) {
        return false;
    }
}