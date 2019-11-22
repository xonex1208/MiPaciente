/**
 * @PerfilPaciente.java 20/noviembre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase para mostrar el perfil del paciente
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 1.0.1
 */

package com.proyecto.mipaciente.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.mipaciente.R;
import com.squareup.picasso.Picasso;

public class PerfilPaciente extends AppCompatActivity
{

    private TextView telefono;
    private TextView email;
    private TextView edad;
    private TextView fechaNacimiento;
    private TextView sexo;
    private TextView nombre;
    private ImageView avatarPaciente;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_paciente);
        nombre= findViewById(R.id.perfil_paciente_nombre);
        telefono = findViewById(R.id.perfil_paciente_telefono);
        email = findViewById(R.id.perfil_paciente_correo);
        edad = findViewById(R.id.perfil_paciente_edad);
        fechaNacimiento = findViewById(R.id.perfil_paciente_fecha_nacimiento);
        sexo = findViewById(R.id.perfil_paciente_sexo);
        avatarPaciente= findViewById(R.id.perfil_paciente_avatar);
        Bundle bundle= getIntent().getExtras();

        if(!bundle.isEmpty())
        {
            telefono.setText(getIntent().getExtras().getString("telefono"));
            nombre.setText(getIntent().getExtras().getString("nombre"));
            email.setText(getIntent().getExtras().getString("email"));
            edad.setText(getIntent().getExtras().getString("edad"));
            fechaNacimiento.setText(getIntent().getExtras().getString("fechaNacimiento"));
            sexo.setText(getIntent().getExtras().getString("sexo"));
            Picasso.get().load(getIntent().getExtras().getString("url")).fit().centerCrop().into(avatarPaciente);
        }else {
            Toast.makeText(this,"No se recibieron datos",Toast.LENGTH_LONG).show();
        }
    }
}
