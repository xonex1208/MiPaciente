package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.mipaciente.Doctor;
import com.proyecto.mipaciente.R;

public class RegistroDoctor extends AppCompatActivity {

    private  EditText nombre;
    private  EditText apellidos;
    private  EditText email;
    private  EditText telefono;
    private  EditText especialidad;
    private  EditText numeroDeCedula;
    private  EditText contrasena;
    private  EditText confirmarContrasena;

    private Button guardarBtn;
    private Button cancelarBtn;

    private  Doctor doctorClas;

    private static final String TAG = "RegistroDoctor";

    FirebaseFirestore bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_doctor);

        nombre = findViewById(R.id.registro_nombre);
        apellidos = findViewById(R.id.registro_apellidos);
        email = findViewById(R.id.registro_correo);
        telefono = findViewById(R.id.registro_telefono);
        especialidad = findViewById(R.id.registro_especialidad);
        numeroDeCedula = findViewById(R.id.registro_numero_cedula);
        contrasena = findViewById(R.id.registro_contrasena);
        confirmarContrasena = findViewById(R.id.registro_confirmar_contrasena);
        guardarBtn = findViewById(R.id.btn_guardar_registro);
        cancelarBtn = findViewById(R.id.btn_cancelar_registro);

        bd = FirebaseFirestore.getInstance();

        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreS = nombre.getText().toString();
                String apellidosS = apellidos.getText().toString();
                String emailS = email.getText().toString();
                String telefonoS = telefono.getText().toString();
                String especialidadS = especialidad.getText().toString();
                String numeroDeCedulaS = numeroDeCedula.getText().toString();
                String contrasenaS = contrasena.getText().toString();
                CollectionReference dbDoctor = bd.collection("persona");

                Doctor doctor = new Doctor(
                        nombreS,
                        apellidosS,
                        emailS,
                        especialidadS,
                        contrasenaS,
                        Integer.parseInt(telefonoS),
                        Integer.parseInt(numeroDeCedulaS)
                );
                dbDoctor.add(doctor)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RegistroDoctor.this, "Registro existoso",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistroDoctor.this, e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor finalDoc = new Doctor();
                DocumentReference docRef = bd.collection("persona").document("gjFpsWKjEzdADHvUGHym");
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Doctor doctorClas = documentSnapshot.toObject(Doctor.class);
                    }
                });
                Toast.makeText(RegistroDoctor.this,"Bienvenido: "+finalDoc.getNombre(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
