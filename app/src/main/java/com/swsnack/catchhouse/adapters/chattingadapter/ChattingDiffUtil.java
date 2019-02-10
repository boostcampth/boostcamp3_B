package com.swsnack.catchhouse.adapters.chattingadapter;

import com.swsnack.catchhouse.adapters.BaseDiffUtil;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.data.chattingdata.pojo.Message;

import java.util.List;
import java.util.Map;

public class ChattingDiffUtil extends BaseDiffUtil<Chatting> {

    ChattingDiffUtil(List<Chatting> mOldList, List<Chatting> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        if (mOldList.get(i).getMessage() != null && mNewList.get(i1).getMessage() != null) {
            Map<String, Message> oldMessages = mOldList.get(i).getMessage();
            Map<String, Message> newMessages = mOldList.get(i1).getMessage();
            return oldMessages.equals(newMessages);
        }
        return false;
    }
}
