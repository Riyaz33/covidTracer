package com.cs446.covidtracer.ui.tester;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TesterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TesterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(""); // ignore for now
    }

    public LiveData<String> getText() {
        return mText;
    }
}