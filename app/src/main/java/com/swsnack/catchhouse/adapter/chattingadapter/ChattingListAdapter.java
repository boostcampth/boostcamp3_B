package com.swsnack.catchhouse.adapter.chattingadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.BaseRecyclerViewAdapter;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.databinding.ItemChattingListBinding;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ChattingListAdapter extends BaseRecyclerViewAdapter<Chatting, ChattingListItemHolder> {

    private ChattingViewModel mChattingViewModel;

    public ChattingListAdapter(Context context, ChattingViewModel chattingViewModel) {
        super(context);
        this.mChattingViewModel = chattingViewModel;
    }

    @Override
    public void onBindView(ChattingListItemHolder holder, int position) {
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((ChattingListItemHolder) holder).onAttachHolder();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((ChattingListItemHolder) holder).onDetachHolder();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemChattingListBinding binding = ItemChattingListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        ChattingListItemHolder viewHolder = new ChattingListItemHolder(binding);
        binding.setHandler(mChattingViewModel);
        binding.setLifecycleOwner(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Log.d("포지션", position + "");

        ItemChattingListBinding binding = ((ChattingListItemHolder) holder).getBinding();
        binding.setChattingData(arrayList.get(position));
        mChattingViewModel.getUser(position,
                binding::setUserData,
                error -> Snackbar.make(binding.getRoot(), R.string.snack_failed_load_list, Snackbar.LENGTH_SHORT).show());

        if (arrayList.get(position).getMessages() != null) {
            List<Message> messages = arrayList.get(position).getMessages();
            binding.tvChattingListLastMessage.setText(messages.get(messages.size() - 1).getContent());
        } else {
            binding.tvChattingListLastMessage.setText("");
        }
    }

    public void setList(List<Chatting> newChattingList) {
        if (arrayList == null) {
            arrayList = newChattingList;
            notifyDataSetChanged();
            return;
        }

        ChattingDiffUtil diffCallback = new ChattingDiffUtil(this.arrayList, newChattingList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
        this.arrayList = newChattingList;
        result.dispatchUpdatesTo(this);
    }
}