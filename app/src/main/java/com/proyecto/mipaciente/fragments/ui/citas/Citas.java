/**
 * @Citas.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase para ver las citas creadas
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.1
 */

package com.proyecto.mipaciente.fragments.ui.citas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.proyecto.mipaciente.R;

public class Citas extends Fragment
{

    private AppBarLayout barra;
    private TabLayout tabs;
    private ViewPager vistasPaginas;

    private ModeloCitas slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        slideshowViewModel =
                ViewModelProviders.of(this).get(ModeloCitas.class);
        View root = inflater.inflate(R.layout.fragment_citas, container, false);
        return root;
    }

}