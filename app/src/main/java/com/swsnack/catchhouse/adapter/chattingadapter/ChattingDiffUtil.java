package com.swsnack.catchhouse.adapter.chattingadapter;

import com.swsnack.catchhouse.adapter.BaseDiffUtil;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;

import java.util.List;

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
