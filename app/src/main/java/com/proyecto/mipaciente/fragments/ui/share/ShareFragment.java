/**
 * @ShareFragment.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase creada por NavigationDrawer. Nota: a ser modifcada
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.1
 */

package com.proyecto.mipaciente.fragments.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.proyecto.mipaciente.R;

public class ShareFragment extends Fragment
{

    private ShareViewModel shareViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(this, new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                textView.setText(s);
            }
        });
        return root;
    }
}