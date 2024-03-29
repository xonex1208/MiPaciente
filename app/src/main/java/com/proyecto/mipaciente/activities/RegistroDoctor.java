/**
 * @RegistroDoctor.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase para registrar al doctor
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.1
 */

package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.mipaciente.modelos.Doctor;
import com.proyecto.mipaciente.R;

import java.util.Calendar;

public class RegistroDoctor extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    //Variables globales
    private EditText nombre;
    private EditText apellidos;
    private EditText email;
    private EditText telefono;
    private EditText especialidad;
    private EditText numeroDeCedula;
    private EditText contrasena;
    private EditText confirmarContrasena;
    private DatePickerDialog.OnDateSetListener fechaNacimiento;
    private TextView fechaNacimientoTextView;
    private ProgressDialog progressDialog;
    private String fecha;
    private String sexo;
    private int anioI;
    private int mesI;
    private int diaI;
    private boolean sexoSeleccionado=false;
    private boolean fechaSeleccionada=false;
    private FirebaseAuth autentificarUsuario;
    private FirebaseFirestore bd;
    private String emailPatron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_doctor);
        Button guardarBtn;
        Button cancelarBtn;

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
        fechaNacimientoTextView= findViewById(R.id.registro_fecha_nacimiento);
        progressDialog = new ProgressDialog(this);
        autentificarUsuario = FirebaseAuth.getInstance();
        //Inicializacion de la base de datos
        bd = FirebaseFirestore.getInstance();

        obtenerDatosSpinner();
        fechaNacimientoTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mostrarFecha();
            }
        });
        ponerFecha();
        //Guardar datos del usuario
        guardarBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validarDatos())
                {
                    //Registrar usuario con Authentication en Firebase
                    registrarUsuario();
                }

            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                limpiarCampos();
                Intent intent = new Intent(RegistroDoctor.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void limpiarCampos()
    {
        nombre.setText("");
        apellidos.setText("");
        email.setText("");
        telefono.setText("");
        especialidad.setText("");
        numeroDeCedula.setText("");
        contrasena.setText("");
        confirmarContrasena.setText("");
        fechaNacimientoTextView.setText(R.string.fecha_nacimiento);

    }

    private void registrarUsuarioFirestore(FirebaseAuth autentificarUsuario)
    {
        String emailS = email.getText().toString();
        String nombreS = nombre.getText().toString();
        String apellidosS = apellidos.getText().toString();
        String telefonoS = telefono.getText().toString();
        String especialidadS = especialidad.getText().toString();
        String numeroDeCedulaS = numeroDeCedula.getText().toString();
        String contrasenaS = contrasena.getText().toString();
        //CollectionReference dbDoctor = bd.collection("doctores");
        //Guardar los datos en el modelo
        Doctor doctor = new Doctor(
                nombreS,
                apellidosS,
                emailS,
                especialidadS,
                contrasenaS,
                Double.parseDouble(telefonoS),
                Integer.parseInt(numeroDeCedulaS),
                fecha,
                sexo
        );
        bd.collection("doctor").document(autentificarUsuario.getUid()).set(doctor).
                addOnSuccessListener(new OnSuccessListener<Void>()
                {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(RegistroDoctor.this, "Registro existoso",Toast.LENGTH_LONG).show();
                limpiarCampos();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(RegistroDoctor.this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void registrarUsuario()
    {
        //registramos un nuevo usuario
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        autentificarUsuario.createUserWithEmailAndPassword(email.getText().toString(), contrasena.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //Verificar si se pudo registrar
                        if(!task.isSuccessful())
                        {
                            try
                            {
                                throw task.getException();
                            }
                            catch(FirebaseAuthUserCollisionException e)
                            {
                                Toast.makeText(
                                        RegistroDoctor.this,
                                        "El email ya existe, intenta con otro",
                                        Toast.LENGTH_LONG).show();
                            } catch(Exception e)
                            {
                                System.out.println("Error: "+e.getMessage());
                            }
                        }
                        else
                        {
                            registrarUsuarioFirestore(autentificarUsuario);
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean validarDatos()
    {
        if (nombre.getText().toString().isEmpty()
            ||apellidos.getText().toString().isEmpty()
            ||email.getText().toString().isEmpty()
            ||telefono.getText().toString().isEmpty()
            ||especialidad.getText().toString().isEmpty()
            ||numeroDeCedula.getText().toString().isEmpty()
            ||contrasena.getText().toString().isEmpty()
            ||confirmarContrasena.getText().toString().isEmpty())
        {
            Toast.makeText(RegistroDoctor.this,"Llene todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }else if(confirmarContrasena.getText().toString().equals(contrasena.getText().toString()))
        {
            if (validarEmail())
            {
                if (validarLongitudDeContraseñas())
                {
                    if(sexoSeleccionado)
                    {
                        if(fechaSeleccionada)
                        {
                            if(!validarTelefono())
                            {
                                Toast.makeText(
                                        RegistroDoctor.this,
                                        "El número del telefono debe tener 10 dígitos",
                                        Toast.LENGTH_LONG).show();
                                return false;
                            }
                            else
                            {
                                if(!validarCedula())
                                {
                                    Toast.makeText(
                                            RegistroDoctor.this,
                                            "El número de cédula debe tener 8 dígitos",
                                            Toast.LENGTH_LONG).show();
                                    return false;
                                }
                                else
                                {
                                    return true;
                                }
                            }

                        }
                        else
                        {
                            Toast.makeText(
                                    RegistroDoctor.this,
                                    "Por favor selecciona tu fecha de nacimiento",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }else
                    {
                        Toast.makeText(
                                RegistroDoctor.this,
                                "Por favor selecciona tu sexo",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                else
                {
                    Toast.makeText(
                            RegistroDoctor.this,
                            "Las contraseñas deben ser de 8 caracteres minimos",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }else
            {
                Toast.makeText(
                        RegistroDoctor.this,
                        "Escriba una dirección de correo correcta",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }else
        {
            Toast.makeText(
                    RegistroDoctor.this,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean validarEmail()
    {
        return email.getText().toString().matches(emailPatron);
    }

    private boolean validarLongitudDeContraseñas()
    {
        return contrasena.getText().toString().length() >= 8;
    }

    private boolean validarTelefono()
    {
        return telefono.getText().toString().length() == 10;
    }

    private boolean validarCedula()
    {
        return numeroDeCedula.getText().toString().length() == 8;
    }

    private void mostrarFecha()
    {
        Calendar calendar= Calendar.getInstance();
        anioI = calendar.get(Calendar.YEAR);
        mesI = calendar.get(Calendar.MONTH);
        diaI = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Dialog,
                fechaNacimiento,
                anioI,mesI,diaI);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void ponerFecha()
    {
        fechaNacimiento= new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia)
            {
                mes= mes+1;
                fecha = anio+ "/"+mes+"/"+dia;
                fechaNacimientoTextView.setText(fecha);
                anioI=anio;
                mesI=mes;
                diaI=dia;
                if (!fecha.isEmpty())
                {
                    fechaSeleccionada=true;
                }
            }
        };
    }

    private void obtenerDatosSpinner()
    {
        Spinner spinner = findViewById(R.id.registro_sexo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sexo,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        //Obtener la posicion del AdapterView
        if(adapterView.getItemIdAtPosition(i)==0)
        {
            sexoSeleccionado=false;
        }
        else
        {
            sexo = adapterView.getItemAtPosition(i).toString();
            sexoSeleccionado=true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
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
