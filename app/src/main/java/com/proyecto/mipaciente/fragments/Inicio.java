/**
 * @Inicio.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase que contiene a todos los Fragments del NavigationDrawer
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.1
 */
package com.proyecto.mipaciente.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.activities.Login;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

public class Inicio extends AppCompatActivity
{
    //Variables globales
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseFirestore bd;
    private FirebaseAuth autentificarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bd= FirebaseFirestore.getInstance();
        autentificarUsuario = FirebaseAuth.getInstance();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Mostrar correo en el NavHeader
        View hView =  navigationView.getHeaderView(0);
        TextView navCorreo = hView.findViewById(R.id.nav_header_correo);
        TextView navNombreCompleto = hView.findViewById(R.id.nav_header_nombre);
        obtenerNombreYEmailDoctor(navCorreo,navNombreCompleto);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    private void obtenerNombreYEmailDoctor(final TextView navCorreo, final TextView navNombreCompleto)
    {
        String uIDDoctor= autentificarUsuario.getUid();
        System.out.println("EL UID: "+uIDDoctor);
        DocumentReference docRef= bd.collection("doctor").document(uIDDoctor);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doctorDatos = task.getResult();
                    String nombreDoctor = doctorDatos.getString("nombre");
                    String apellidosDoctor = doctorDatos.getString("apellidos");
                    String emailDoctor = doctorDatos.getString("email");
                    System.out.println(nombreDoctor);
                    ponerDatosEnNavHeader(nombreDoctor+" "+apellidosDoctor,
                            emailDoctor,
                            navCorreo,
                            navNombreCompleto);
                }
                else
                {
                    Toast.makeText(
                            getApplication(),
                            "Se ha producido un error intentalo nuevamente",
                            Toast.LENGTH_LONG).show();
                    Log.d("error", "Fallo al obtener los datos del documento", task.getException());
                }
            }
        });
    }

    private void ponerDatosEnNavHeader(String nombreDoctor,
                                             String emailDoctor,
                                             TextView navCorreo,
                                             TextView navNombrecompleto)
    {
        navCorreo.setText(emailDoctor);
        navNombrecompleto.setText(nombreDoctor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        //Cerrar sesion
        if(id == R.id.cerrar_sesion)
        {
            autentificarUsuario.signOut();
            startActivity(new Intent(this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
