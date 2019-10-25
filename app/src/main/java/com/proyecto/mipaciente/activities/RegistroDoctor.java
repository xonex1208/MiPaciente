package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.mipaciente.modelos.Doctor;
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
    String nombree = "";

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
                if (validarDatos()){
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
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               nombre.setText("");
               apellidos.setText("");
               email.setText("");
               telefono.setText("");
               especialidad.setText("");
               numeroDeCedula.setText("");
               contrasena.setText("");
               confirmarContrasena.setText("");
            }
        });
    }

    private boolean validarDatos(){
        if (nombre.getText().toString().isEmpty()
            ||apellidos.getText().toString().isEmpty()
            ||email.getText().toString().isEmpty()
            ||telefono.getText().toString().isEmpty()
            ||especialidad.getText().toString().isEmpty()
            ||numeroDeCedula.getText().toString().isEmpty()
            ||contrasena.getText().toString().isEmpty()
            ||confirmarContrasena.getText().toString().isEmpty()){
            Toast.makeText(RegistroDoctor.this,"Llene todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }else if(confirmarContrasena.getText().toString().equals(contrasena.getText().toString())){
            return true;
        }else{
            Toast.makeText(RegistroDoctor.this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //Obtener datos de firestore

    /*private void obtenerDatos(){
        bd.collection("persona").whereEqualTo("nombre","Alfred")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d("Datos",document.getId() + " => "+ document.getData());
                                Log.d("Datos",document.getId() + " => "+ document.getData().get("nombre"));
                                String nombreess = (String)document.getString("nombre");
                                nombre.setText(nombreess);
                            }
                        }else{
                            Toast.makeText(RegistroDoctor.this, "Error al obtener los datos",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/
}
