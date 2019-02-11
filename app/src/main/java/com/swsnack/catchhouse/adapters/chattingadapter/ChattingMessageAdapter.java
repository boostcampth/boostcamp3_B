package com.swsnack.catchhouse.adapters.chattingadapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.BaseRecyclerViewAdapter;
import com.swsnack.catchhouse.data.chattingdata.model.Message;
import com.swsnack.catchhouse.databinding.ItemChattingMessageBinding;

public class ChattingMessageAdapter extends BaseRecyclerViewAdapter<Message, ChattingMessageItemHolder> {

    public ChattingMessageAdapter(Context context) {
        super(context);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((ChattingMessageItemHolder) holder).onAttachHolder();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((ChattingMessageItemHolder) holder).onDetachHolder();
    }

    @Override
    public void onBindView(ChattingMessageItemHolder holder, int position) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemChattingMessageBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_chatting_message, viewGroup, false);

        ChattingMessageItemHolder viewHolder = new ChattingMessageItemHolder(binding);

        return viewHolder;
    }
}
