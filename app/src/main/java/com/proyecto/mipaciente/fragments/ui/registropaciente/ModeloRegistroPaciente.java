package com.proyecto.mipaciente.fragments.ui.registropaciente;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModeloRegistroPaciente extends ViewModel {

    private MutableLiveData<String> mText;

    public ModeloRegistroPaciente() {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}