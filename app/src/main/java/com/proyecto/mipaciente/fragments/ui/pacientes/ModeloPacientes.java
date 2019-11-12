package com.proyecto.mipaciente.fragments.ui.pacientes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModeloPacientes extends ViewModel {

    private MutableLiveData<String> mText;

    public ModeloPacientes() {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}