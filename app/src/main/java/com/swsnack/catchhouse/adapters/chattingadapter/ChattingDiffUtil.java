package com.swsnack.catchhouse.adapters.chattingadapter;

import com.swsnack.catchhouse.adapters.BaseDiffUtil;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;

import java.util.List;

public class ChattingDiffUtil extends BaseDiffUtil<Chatting> {

    ChattingDiffUtil(List<Chatting> mOldList, List<Chatting> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        if(mOldList.get(i).getMessage() != null && mNewList.get(i1).getMessage() != null){
            return mOldList.get(i).getMessage().getContent().equals(mNewList.get(i1).getMessage().getContent());
        }
        return false;
    }
}
