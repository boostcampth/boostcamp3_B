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
import com.swsnack.catchhouse.databinding.ItemChattingListBinding;
import com.swsnack.catchhouse.util.DataConverter;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.List;

public class ChattingListAdapter extends BaseRecyclerViewAdapter<Chatting, ChattingItemHolder> {

    private ChattingViewModel mChattingViewModel;

    public ChattingListAdapter(Context context, ChattingViewModel chattingViewModel) {
        super(context);
        this.mChattingViewModel = chattingViewModel;
    }

    @Override
    public void onBindView(ChattingItemHolder holder, int position) {
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((ChattingItemHolder) holder).onAttachHolder();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((ChattingItemHolder) holder).onDetachHolder();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemChattingListBinding binding = ItemChattingListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

        ChattingItemHolder viewHolder = new ChattingItemHolder(binding);

        binding.setLifecycleOwner(viewHolder);
        binding.setHandler(mChattingViewModel);
        binding.setChattingData(arrayList.get(i));
        mChattingViewModel.getUser(i,
                binding::setUserData,
                error -> Snackbar.make(viewGroup, R.string.snack_failed_load_list, Snackbar.LENGTH_SHORT));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (arrayList.get(position).getMessage() != null) {
            ((ChattingItemHolder) holder).getBinding()
                    .tvChattingListLastMessage.setText(
                    DataConverter.sortByValueFromMapToList(arrayList.get(position).getMessage()).get(0).getContent());
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
