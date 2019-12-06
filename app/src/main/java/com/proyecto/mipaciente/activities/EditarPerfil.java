package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.modelos.Paciente;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class EditarPerfil extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String idPaciente;

    //Variables globales
    private TextInputLayout nombre;
    private TextInputLayout apellidos;
    private TextInputLayout email;
    private TextInputLayout telefono;
    private TextInputLayout edad;
    private TextInputLayout direccion;
    private TextInputLayout estadoCivil;
    private TextInputLayout parentesco;
    private TextInputLayout redSocial;
    private TextInputLayout ocupacion;
    private TextView fechaNacimientoText;
    private ImageView imagenPacienteImageView;
    private DatePickerDialog.OnDateSetListener fechaNacimiento;

    private String fecha;
    private String sexo;
    private int anioI;
    private int mesI;
    private int diaI;
    private int tamanoAdapter;
    private boolean sexoSeleccionado = false;

    private String telefonoS;

    String emailPatron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Spinner spinner;


    private ProgressDialog progressDialog;
    private Uri imagenPacienteUri;

    String urlFoto;

    private FirebaseFirestore bd;
    private FirebaseStorage referenciaImagen;
    private StorageTask evitarSpamTask;
    private StorageReference referenciaArchivo;
    private FirebaseAuth autentificarUsuario;
    private DocumentReference documentReference;
    private DocumentSnapshot documentSnapshot;


    private static final int ESCOGER_IMAGEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        //Para añadir el toolbar su nombre y demas cosas
        Toolbar toolbar = findViewById(R.id.toolbarInclude);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);

        nombre = findViewById(R.id.editar_nombre_paciente);
        apellidos = findViewById(R.id.editar_apellidos_paciente);
        email = findViewById(R.id.editar_correo_paciente);
        telefono = findViewById(R.id.editar_telefono_paciente);
        edad = findViewById(R.id.editar_edad_paciente);
        direccion = findViewById(R.id.editar_direccion_paciente);
        estadoCivil = findViewById(R.id.editar_estado_civil_paciente);
        parentesco = findViewById(R.id.editar_parentesco_paciente);
        redSocial = findViewById(R.id.editar_red_social_paciente);
        ocupacion = findViewById(R.id.editar_ocupacion_paciente);
        fechaNacimientoText = findViewById(R.id.editar_fecha_nacimiento_paciente);
        imagenPacienteImageView = findViewById(R.id.registro_imagen_paciente);

        spinner= findViewById(R.id.editar_sexo_paciente);
        bd=FirebaseFirestore.getInstance();
        autentificarUsuario=FirebaseAuth.getInstance();
        referenciaImagen= FirebaseStorage.getInstance();
        referenciaArchivo=FirebaseStorage.getInstance().getReference("pacientes");
        obtenerDatosSpinner();

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty())
        {
            idPaciente= getIntent().getExtras().getString("pacienteID");
            urlFoto = getIntent().getExtras().getString("urlImagen");
            String documento = idPaciente;
            System.out.println("ID PACIENTE: "+documento);
            System.out.println("ID PACIENTE: "+documento);
            System.out.println("ID PACIENTE: "+documento);
            System.out.println("ID PACIENTE: "+documento);
            documentReference=bd.collection("paciente").document(documento);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        documentSnapshot = task.getResult();
                        if (documentSnapshot.exists())
                        {
                            obtenerDatos();
                        }else
                        {
                            Log.d("HOLa","No hay datos obtenidos");
                        }
                    }
                }
            });
        }
        findViewById(R.id.btn_guardar_editar_paciente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Cargando...");
                progressDialog.show();
                //Si selecciono foto, imagenUri no sera null, entonces eliminamos la vieja foto
                //Si no selecciono nada Uri sera null, entonces solo cargamos datos
                //Enviandole un booleano si cargo o no cargo 1 y 0
                if (validarDatos())
                {
                    if (imagenPacienteUri !=null)
                    {
                        verificarTelefono(true);
                    }else
                    {
                        verificarTelefono(false);
                    }
                }

            }
        });

        imagenPacienteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirFileChooser();
            }
        });

        findViewById(R.id.btn_cancelar_editar_paciente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fechaNacimientoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFecha();
            }
        });
        ponerFecha();
    }

    private boolean validarDatos()
    {
        if (nombre.getEditText().getText().toString().isEmpty()
                ||apellidos.getEditText().getText().toString().isEmpty()
                ||email.getEditText().getText().toString().isEmpty()
                ||telefono.getEditText().getText().toString().isEmpty()
                ||edad.getEditText().getText().toString().isEmpty()
                ||direccion.getEditText().getText().toString().isEmpty()
                ||estadoCivil.getEditText().getText().toString().isEmpty()
                ||parentesco.getEditText().getText().toString().isEmpty()
                ||redSocial.getEditText().getText().toString().isEmpty())
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

    private boolean validarTelefono()
    {
        return telefono.getEditText().getText().toString().length() == 10;
    }

    private boolean validarEmail()
    {
        return email.getEditText().getText().toString().matches(emailPatron);
    }

    private int posicionSpinner(String sexoS)
    {
        String munMinusculas=sexoS.toLowerCase();
        Log.d("hola","MUNICIPIO: "+sexoS);
        Log.d("hola","MUNICIPIOMINUSCULAS: "+munMinusculas);
        int i;
        //Obtener el tamaño del Spinner
        for (i=0; i<tamanoAdapter;i++)
        {
            if (munMinusculas.equals(spinner.getItemAtPosition(i).toString().toLowerCase()))
            {
                Log.d("hola","POSICION: "+i);
                return i;
            }
            sexoS=spinner.getItemAtPosition(i).toString();
        }
        Log.d("hola","MUNICIPIO2: "+sexoS);
        return 0;
    }

    private void obtenerDatos()
    {
        nombre.getEditText().setText(documentSnapshot.getString("nombre"));
        apellidos.getEditText().setText(documentSnapshot.getString("apellidos"));
        email.getEditText().setText(documentSnapshot.getString("email"));
        telefono.getEditText().setText(documentSnapshot.getString("telefono"));
        edad.getEditText().setText(""+documentSnapshot.getData().get("edad"));
        direccion.getEditText().setText(documentSnapshot.getString("direccion"));
        estadoCivil.getEditText().setText(documentSnapshot.getString("estadoCivil"));
        parentesco.getEditText().setText(documentSnapshot.getString("parentesco"));
        redSocial.getEditText().setText(documentSnapshot.getString("redSocial"));
        ocupacion.getEditText().setText(documentSnapshot.getString("ocupacion"));
        fechaNacimientoText.setText(documentSnapshot.getString("fechaNacimiento"));
        spinner.setSelection(posicionSpinner(documentSnapshot.getString("sexo")));
        fecha=documentSnapshot.getString("fechaNacimiento");
        telefonoS=documentSnapshot.getString("telefono");
        Picasso.get().load(documentSnapshot.getString("imagenPaciente")).fit().centerCrop().into(imagenPacienteImageView);
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
                fechaNacimientoText.setText(fecha);
                anioI=anio;
                mesI=mes;
                diaI=dia;
            }
        };
    }

    private void obtenerDatosSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sexo,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tamanoAdapter=adapter.getCount();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    private void abrirFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,ESCOGER_IMAGEN);
    }

    private void subirFoto(boolean fotoSeleccionada)
    {

        if (imagenPacienteUri != null)
        {
            referenciaArchivo = referenciaArchivo.
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
                                    EditarPerfil.this,
                                    "No se cargo la imagen",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
        }
        else
        {
            Log.d("Hola","NO CARGO FOTO");
        }
    }

    private void verificarTelefono(final boolean fotoSeleccionada)
    {
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        if (!telefonoS.equals(telefono.getEditText().getText().toString())) {
            //Obtener el numero de telefono y validar si existe del Doctor logeando actualemente
            bd.collection("paciente")
                    .whereEqualTo("idDelDoctor", autentificarUsuario.getUid())
                    .whereEqualTo("telefono", telefono.getEditText().getText().toString())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean isExisting = false;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Log.d("Datos", document.getId() + " => " + document.getData());
                                Log.d("Datos", document.getId() + " => " + document.getData().get("telefono"));
                                String tel = document.getString("telefono");
                                System.out.println("Obtener datos");
                                System.out.println("Telefono: " + tel);
                                if (tel.equals(telefono.getEditText().getText().toString())) {
                                    isExisting = true;
                                }
                            }
                            if (isExisting) {
                                Toast.makeText(
                                        EditarPerfil.this,
                                        "El número de telefono ya esta asociado a otro paciente",
                                        Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                if (fotoSeleccionada) {
                                    subirFoto(fotoSeleccionada);
                                } else {
                                    registrarPaciente(urlFoto);
                                }

                                //registrarPaciente();
                            }

                        }
                    });
        }else
        {
            if (fotoSeleccionada) {
                subirFoto(fotoSeleccionada);
            } else {
                registrarPaciente(urlFoto);
            }
        }
        System.out.println("Returning");
    }

    private void registrarPaciente(String urlPaciente)
    {

        String uidDoctor=autentificarUsuario.getUid();
        String nombreS = nombre.getEditText().getText().toString();
        String apellidosS = apellidos.getEditText().getText().toString();
        String emailS = email.getEditText().getText().toString();
        String telefonoS = telefono.getEditText().getText().toString();
        String edadS = edad.getEditText().getText().toString();
        String direccionS = direccion.getEditText().getText().toString();
        String estadoCivilS = estadoCivil.getEditText().getText().toString();
        String parentescoS = parentesco.getEditText().getText().toString();
        String ocupacionS = ocupacion.getEditText().getText().toString();
        String redSocialS = estadoCivil.getEditText().getText().toString();
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
        bd.collection("paciente").document(idPaciente)
                .set(paciente).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(
                        EditarPerfil.this,
                        "Registro exitoso",
                        Toast.LENGTH_LONG).show();
                obtenerDatos();
                progressDialog.dismiss();
                recreate();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(
                        EditarPerfil.this,
                        "No se pudo realizar el registro",
                        Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private String getExtensionArchivo(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void eliminarDatos()
    {
        bd.collection("paciente").document(idPaciente)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditarPerfil.this,"Paciente eliminado",Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfil.this,"Error al eliminar el paciente",Toast.LENGTH_LONG).show();
            }
        });

    }

    //Colocar el menu el el Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_v1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.eliminar_icono:
                final AlertDialog.Builder pop = new AlertDialog.Builder(this);
                pop.setMessage("¿Estas seguro de eliminar el registro?\n" +
                        "Esta acción no puede revertirse");
                pop.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarDatos();
                    }
                });
                pop.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                pop.show();
                break;*/
            case R.id.cerrar_icono_normal:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
