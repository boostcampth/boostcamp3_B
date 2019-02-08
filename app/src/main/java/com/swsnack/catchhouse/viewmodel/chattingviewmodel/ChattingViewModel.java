package com.swsnack.catchhouse.viewmodel.chattingviewmodel;

import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class ChattingViewModel extends ReactiveViewModel {

    private ViewModelListener mListener;

    ChattingViewModel(DataManager dataManager, ViewModelListener viewModelListener) {
        super(dataManager);
        this.mListener = viewModelListener;
    }
}