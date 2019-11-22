/**
 * @Login.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase donde el usuario iniciara sesion
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @author Abigail Guadalupe Balbuena Martinez
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.3
 */

package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.proyecto.mipaciente.fragments.Inicio;
import com.proyecto.mipaciente.modelos.Loginn;
import com.proyecto.mipaciente.R;

public class Login extends AppCompatActivity implements OnClickListener
{

    private EditText email;
    private  EditText contrasena;

    String emailPatron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final String TAG = "Login";

    FirebaseFirestore bd;
    //Variable para agregar usuarios y contraseña
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email =  findViewById(R.id.registro_login_usuario);
        contrasena = findViewById(R.id.registro_login_contraseña);

        Button btnRegistrar = findViewById(R.id.btn_login_registro);
        Button btnLogin = findViewById(R.id.btn_login_entrar);

        progressDialog = new ProgressDialog(this);
        bd = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        btnRegistrar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_login_registro:
                registrarUsuario();
                break;
            case R.id.btn_login_entrar:
                loguearUsuario();
                break;
        }

    }

    private void registrarUsuario()
    {
        Intent intent = new Intent(Login.this,RegistroDoctor.class);
        startActivity(intent);
    }

    private void loguearUsuario()
    {
        //Obtenemos el email y la contraseña
        final String usuarioS = email.getText().toString().trim();
        String contrasenaS = contrasena.getText().toString().trim();
        //Verificamos que el formato de email sea correcto
        if (validarEmail())
        {
            if (TextUtils.isEmpty(usuarioS))
            {
                Toast.makeText(this, "Se debe ingresar un e-mail", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(contrasenaS))
            {
                Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
                return;
            }
        }else
        {
            Toast.makeText(
                    this,
                    "Escriba una dirección de correo correcta",
                    Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        //loguear usuario
        mAuth.signInWithEmailAndPassword(usuarioS, contrasenaS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            int pos = usuarioS.indexOf("@");
                            String user = usuarioS.substring(0, pos);
                            Toast.makeText(
                                    Login.this,
                                    "Bienvenido: " + email.getText(),
                                    Toast.LENGTH_LONG).show();
                            Intent intencion = new Intent(getApplication(), Inicio.class);
                            startActivity(intencion);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Verifica tu correo o contraseña",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    protected void onStart()
    {
        //Verificar sesion activa
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            startActivity(new Intent(this, Inicio.class));
            finish();
        }
    }

    private boolean validarEmail()
    {
        return email.getText().toString().matches(emailPatron);
    }

}

