package com.proyecto.mipaciente.fragments.ui.citas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModeloCitas extends ViewModel {

    private MutableLiveData<String> mText;

    public ModeloCitas() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}