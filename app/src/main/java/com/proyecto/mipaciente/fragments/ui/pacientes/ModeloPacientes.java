/**
 * @ModeloPacientes.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase modelo requerida por NavigationDrawer
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.1
 */
package com.proyecto.mipaciente.fragments.ui.pacientes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModeloPacientes extends ViewModel
{

    private MutableLiveData<String> mText;

    public ModeloPacientes()
    {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}