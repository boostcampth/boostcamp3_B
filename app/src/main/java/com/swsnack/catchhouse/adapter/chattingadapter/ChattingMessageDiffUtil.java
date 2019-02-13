package com.swsnack.catchhouse.adapter.chattingadapter;

import com.swsnack.catchhouse.adapter.BaseDiffUtil;
import com.swsnack.catchhouse.data.model.Message;

import java.util.List;

public class ChattingMessageDiffUtil extends BaseDiffUtil<Message> {

    ChattingMessageDiffUtil(List<Message> mOldList, List<Message> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return mOldList.get(i).getContent().equals(mNewList.get(i1).getContent());
    }
}
