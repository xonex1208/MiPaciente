/**
 * @RegistrarPaciente.java 8/noviembre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase para registrar al paciente
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.9
 */

package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.modelos.ExpedientePaciente;
import com.proyecto.mipaciente.modelos.Paciente;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class RegistrarPaciente extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    //Variables globales
    private EditText nombre;
    private EditText apellidos;
    private EditText email;
    private EditText telefono;
    private EditText edad;
    private EditText direccion;
    private EditText estadoCivil;
    private EditText parentesco;
    private EditText redSocial;
    private EditText ocupacion;
    private DatePickerDialog.OnDateSetListener fechaNacimiento;
    private TextView fechaNacimientoTextView;
    private String fecha;
    private String sexo;
    private int anioI;
    private int mesI;
    private int diaI;
    private boolean sexoSeleccionado = false;
    private boolean fechaSeleccionada = false;
    private ProgressDialog progressDialog;
    private ImageView imagenPacienteImageView;
    private Uri imagenPacienteUri;

    private FirebaseFirestore bd;
    private StorageReference referenciaImagen;
    private StorageTask evitarSpamTask;
    private FirebaseAuth autentificarUsuario;
    private DocumentReference documentReference;
    String emailPatron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //Constantes
    private static final int ESCOGER_IMAGEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_paciente);
        Button guardarBtn;
        Button cancelarBtn;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cerrar);
        getSupportActionBar().setTitle("Agregar Paciente");

        progressDialog = new ProgressDialog(this);
        nombre = findViewById(R.id.registro_nombre_paciente);
        apellidos = findViewById(R.id.registro_apellidos_paciente);
        email = findViewById(R.id.registro_correo_paciente);
        telefono = findViewById(R.id.registro_telefono_paciente);
        edad = findViewById(R.id.registro_edad_paciente);
        direccion = findViewById(R.id.registro_direccion_paciente);
        estadoCivil = findViewById(R.id.registro_estado_civil_paciente);
        parentesco = findViewById(R.id.registro_parentesco_paciente);
        redSocial = findViewById(R.id.registro_red_social_paciente);
        ocupacion = findViewById(R.id.registro_ocupacion_paciente);
        guardarBtn = findViewById(R.id.btn_guardar_registro_paciente);
        cancelarBtn = findViewById(R.id.btn_cancelar_registro_paciente);
        fechaNacimientoTextView= findViewById(R.id.registro_fecha_nacimiento_paciente);
        imagenPacienteImageView = findViewById(R.id.registro_imagen_paciente);

        //Inicializacion de la base de datos
        bd = FirebaseFirestore.getInstance();
        //Inicializar FirebaseStorage en la carpeta pacientes
        referenciaImagen = FirebaseStorage.getInstance().getReference("pacientes");
        //Inicializar Firebase Auth
        autentificarUsuario = FirebaseAuth.getInstance();
        obtenerDatosSpinner();
        String ruta = "android.resource://com.proyecto.mipaciente/drawable/avatar_paciente";
        imagenPacienteUri= Uri.parse(ruta);
        imagenPacienteImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                abrirFileChooser();
            }
        });

        fechaNacimientoTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mostrarFecha();
            }
        });
        ponerFecha();
        guardarBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Evitar con el if que el usuario spamie el boton de guardar
                if (evitarSpamTask!= null && evitarSpamTask.isInProgress())
                {
                    Toast.makeText(
                            RegistrarPaciente.this,
                            "Registro en progreso",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Guardar datos
                    if (validarDatos())
                    {
                        //Verificar si el email existe
                        verificarTelefono();
                        //registrarPaciente();
                    }
                }

            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }



    private void abrirFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,ESCOGER_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ESCOGER_IMAGEN && resultCode == RESULT_OK
        && data != null && data.getData() != null)
        {
            imagenPacienteUri=data.getData();
            //Poner la imagen en el textview
            try
            {
                Picasso.get().load(imagenPacienteUri).resize(500,500).rotate(-90).into(imagenPacienteImageView);
            }
            catch (Exception excepcion)
            {
                Toast.makeText(this,"Error al cargar la imagen",Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean validarDatos()
    {
        if (nombre.getText().toString().isEmpty()
                ||apellidos.getText().toString().isEmpty()
                ||email.getText().toString().isEmpty()
                ||telefono.getText().toString().isEmpty()
                ||edad.getText().toString().isEmpty()
                ||direccion.getText().toString().isEmpty()
                ||estadoCivil.getText().toString().isEmpty()
                ||parentesco.getText().toString().isEmpty()
                ||redSocial.getText().toString().isEmpty())
        {
            Toast.makeText(
                    this,
                    "Llene todos los campos",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if (validarEmail())
        {
            if(sexoSeleccionado)
            {
                if(fechaSeleccionada)
                {
                    if( ! validarTelefono())
                    {
                        Toast.makeText(
                                this,
                                "El número del telefono debe tener 10 dígitos",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                    else
                    {
                        return true;
                    }

                }
                else
                {
                    Toast.makeText(
                            this,
                            "Por favor selecciona una fecha de nacimiento",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(
                        this,
                        "Por favor selecciona tu sexo",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(
                    this,
                    "Escriba una dirección de correo correcta",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void registrarPaciente(String urlPaciente)
    {
        String uidDoctor=autentificarUsuario.getUid();
        String nombreS = nombre.getText().toString();
        String apellidosS = apellidos.getText().toString();
        String emailS = email.getText().toString();
        String telefonoS = telefono.getText().toString();
        String edadS = edad.getText().toString();
        String direccionS = direccion.getText().toString();
        String estadoCivilS = estadoCivil.getText().toString();
        String parentescoS = parentesco.getText().toString();
        String ocupacionS = ocupacion.getText().toString();
        String redSocialS = estadoCivil.getText().toString();
        CollectionReference dbPaciente = bd.collection("paciente");

        Paciente paciente = new Paciente(
                nombreS,
                apellidosS,
                emailS,
                telefonoS,
                fecha,
                Integer.parseInt(edadS),
                sexo,
                direccionS,
                estadoCivilS,
                parentescoS,
                ocupacionS,
                redSocialS,
                urlPaciente,
                uidDoctor
        );

        dbPaciente
                .add(paciente)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        registrarExpediente(documentReference.getId());
                        Toast.makeText(RegistrarPaciente.this, "Registro existoso",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RegistrarPaciente.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void registrarExpediente(String iDpaciente)
    {
        //Registro del expediente del Paciente
        documentReference=bd.collection("expedienteClinico").document(iDpaciente);

        ExpedientePaciente expedientePaciente = new ExpedientePaciente(
                false,
                false,
                false,
                false,
                false,
                "",
                iDpaciente
        );
        bd.collection("expedienteClinico").document(iDpaciente)
                .set(expedientePaciente).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("HOLa","REGISTRO EXITOSO EXPEDIENTE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("HOLa","FALLO AL REGISTRAR EXPEDIENTE");
            }
        });
    }
    private void subirFoto()
    {
        if (imagenPacienteUri != null)
        {
            StorageReference referenciaArchivo = referenciaImagen.
                    child(System.currentTimeMillis()+"."+
                    getExtensionArchivo(imagenPacienteUri));
            evitarSpamTask= referenciaArchivo.putFile(imagenPacienteUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri descargarUrl = urlTask.getResult();
                            String urlImagen= descargarUrl.toString();
                            //Se manda la URL de la imagen
                            registrarPaciente(urlImagen);
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(
                                    RegistrarPaciente.this,
                                    "No se cargo la imagen",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(this,"No selecciono ninguna foto",Toast.LENGTH_LONG).show();
        }
    }

    private String getExtensionArchivo(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void verificarTelefono()
    {
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        //Obtener el numero de telefono y validar si existe del Doctor logeando actualemente
        bd.collection("paciente")
                .whereEqualTo("idDelDoctor",autentificarUsuario.getUid())
                .whereEqualTo("telefono",telefono.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {
                        boolean isExisting = false;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                        {
                            Log.d("Datos",document.getId() + " => "+ document.getData());
                            Log.d("Datos",document.getId() + " => "+ document.getData().get("telefono"));
                            String tel = document.getString("telefono");
                            System.out.println("Obtener datos");
                            System.out.println("Telefono: "+tel);
                            if (tel.equals(telefono.getText().toString()))
                            {
                                isExisting = true;
                            }
                        }
                        if (isExisting)
                        {
                            Toast.makeText(
                                    RegistrarPaciente.this,
                                    "El número de telefono ya esta asociado a otro paciente",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                        else
                        {
                            subirFoto();
                            //registrarPaciente();
                        }

                    }
                });
        System.out.println("Returning");
    }

    private boolean validarEmail()
    {
        return email.getText().toString().matches(emailPatron);
    }

    private boolean validarTelefono()
    {
        return telefono.getText().toString().length() == 10;
    }

    private void mostrarFecha()
    {
        //Mostrar la fecha a seleccionar en un DatePicker
        Calendar calendar= Calendar.getInstance();
        anioI = calendar.get(Calendar.YEAR);
        mesI = calendar.get(Calendar.MONTH);
        diaI = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Dialog,
                fechaNacimiento,
                anioI,mesI,diaI);
        //Mandar llamar al datePicker con el tema Transparen
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
        Spinner spinner = findViewById(R.id.registro_sexo_paciente);
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
}
