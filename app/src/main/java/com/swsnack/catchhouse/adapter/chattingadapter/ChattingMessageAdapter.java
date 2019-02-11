package com.swsnack.catchhouse.adapters.chattingadapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.BaseRecyclerViewAdapter;
import com.swsnack.catchhouse.data.chattingdata.model.Message;
import com.swsnack.catchhouse.data.userdata.model.User;
import com.swsnack.catchhouse.databinding.ItemChattingMessageBinding;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.List;

public class ChattingMessageAdapter extends BaseRecyclerViewAdapter<Message, ChattingMessageItemHolder> {

    private User mUserData;
    private ChattingViewModel mChattingViewModel;

    public ChattingMessageAdapter(Context context, ChattingViewModel chattingViewModel) {
        super(context);
        this.mChattingViewModel = chattingViewModel;
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
        holder.getBinding().setMessage(arrayList.get(position));

        if (arrayList.get(position).getSendUuid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.getBinding().ivItemMessageProfile.setVisibility(View.GONE);
            holder.getBinding().tvItemMessageDestination.setVisibility(View.GONE);
            holder.getBinding().tvItemMessage.setVisibility(View.VISIBLE);
            return;
        }
        holder.getBinding().ivItemMessageProfile.setVisibility(View.VISIBLE);
        holder.getBinding().tvItemMessageDestination.setVisibility(View.VISIBLE);
        holder.getBinding().tvItemMessage.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemChattingMessageBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_chatting_message, viewGroup, false);

        ChattingMessageItemHolder viewHolder = new ChattingMessageItemHolder(binding);
        binding.setLifecycleOwner(viewHolder);
        binding.setHandler(mChattingViewModel);

        return viewHolder;
    }

    public void setList(List<Message> newChattingList) {
        if (arrayList == null) {
            arrayList = newChattingList;
            notifyDataSetChanged();
            return;
        }

        ChattingMessageDiffUtil diffCallback = new ChattingMessageDiffUtil(this.arrayList, newChattingList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
        this.arrayList = newChattingList;
        result.dispatchUpdatesTo(this);
    }

    public void setUserData(User user) {
        this.mUserData = user;
    }
}
