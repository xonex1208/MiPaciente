package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.proyecto.mipaciente.Loginn;
import com.proyecto.mipaciente.R;



public class Login extends AppCompatActivity implements OnClickListener{

    private EditText usuario;
    private  EditText contrasena;

    private Button btnRegistrar;
    private Button btnLogin;

    private Loginn loginClas;

    private static final String TAG = "Login";

    FirebaseFirestore bd;

    //Variable para agregar usuarios y contraseña

    private FirebaseAuth mAuth;

    private FirebaseApp firebaseApp;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario =  findViewById(R.id.registro_login_usuario);
        contrasena = findViewById(R.id.registro_login_contraseña);

        btnRegistrar =  findViewById(R.id.btn_login_registro);
        btnLogin =  findViewById(R.id.btn_login_entrar);

        progressDialog = new ProgressDialog(this);
        bd = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        btnRegistrar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);





    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login_registro:
                //Invocamos al m�t
                registrarUsuario();
                break;
            case R.id.btn_login_entrar:
                loguearUsuario();
                break;
        }

    }



    ////////
    private void registrarUsuario() {


        //Obtenemos el email y la contrase�a desde las cajas de texto
        String usuarioS = usuario.getText().toString().trim();
        String contrasenaS = contrasena.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vac�as
        if (TextUtils.isEmpty(usuarioS)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un usuario", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(contrasenaS)) {
            Toast.makeText(this, "Falta ingresar la contrase�a", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Realizando registro...");
        progressDialog.show();

        //registramos un nuevo usuario

        mAuth.createUserWithEmailAndPassword(usuarioS, contrasenaS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "Se ha registrado el usuario con el correo: " + usuario.getText(), Toast.LENGTH_LONG).show();
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

//                                if (task.getException() instanceof FirebaseFirestoreUserCollisionException) {//si se presenta una colisi�n
//                                    Toast.makeText(Login.this, "Ese usuario no existe ", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(Login.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
//                                }
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    private void loguearUsuario() {
        //Obtenemos el email y la contrase�a desde las cajas de texto
        final String usuarioS = usuario.getText().toString().trim();
        String contrasenaS = contrasena.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vac�as
        if (TextUtils.isEmpty(usuarioS)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un usuario", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(contrasenaS)) {
            Toast.makeText(this, "Falta ingresar la contrase�a", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Realizando consulta...");
        progressDialog.show();

        //loguear usuario
        mAuth.signInWithEmailAndPassword(usuarioS, contrasenaS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            int pos = usuarioS.indexOf("@");
                            String user = usuarioS.substring(0, pos);
                            Toast.makeText(Login.this, "Bienvenido: " + usuario.getText(), Toast.LENGTH_LONG).show();
                            Intent intencion = new Intent(getApplication(),RegistroDoctor.class);
                            intencion.putExtra("usuario", user);
                            startActivity(intencion);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                        }
                        progressDialog.dismiss();
                    }
                });


    }


}

