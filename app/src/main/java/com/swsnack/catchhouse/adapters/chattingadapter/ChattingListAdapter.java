package com.swsnack.catchhouse.adapters.chattingadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.BaseRecyclerViewAdapter;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;
import com.swsnack.catchhouse.databinding.ItemChattingListBinding;
import com.swsnack.catchhouse.util.DataConverter;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.List;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ItemChattingListBinding binding = ((ChattingListItemHolder) holder).getBinding();
        binding.setChattingData(arrayList.get(position));
        mChattingViewModel.getUser(position,
                binding::setUserData,
                error -> Snackbar.make(binding.getRoot(), R.string.snack_failed_load_list, Snackbar.LENGTH_SHORT).show());

        if (arrayList.get(position).getMessage() != null) {
            List<Message> messages = DataConverter.sortByValueFromMapToList(arrayList.get(position).getMessage());
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
