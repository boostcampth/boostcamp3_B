package com.swsnack.catchhouse.adapter.chattingadapter;

import com.swsnack.catchhouse.adapter.BaseDiffUtil;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChattingDiffUtil extends BaseDiffUtil<Chatting> {

    ChattingDiffUtil(List<Chatting> mOldList, List<Chatting> mNewList) {
        super(mOldList, mNewList);
    }

    @Override
    public boolean areContentsTheSame(int oldMessageIndex, int newMessageIndex) {
        if (mOldList.get(oldMessageIndex).getMessages() != null && mNewList.get(newMessageIndex).getMessages() != null) {
            List<Message> oldMessages = mOldList.get(oldMessageIndex).getMessages();
            List<Message> newMessages = mNewList.get(newMessageIndex).getMessages();
            return oldMessages.get(oldMessages.size() -1).equals(newMessages.get(newMessages.size() -1));
        }
        return false;
    }
}
