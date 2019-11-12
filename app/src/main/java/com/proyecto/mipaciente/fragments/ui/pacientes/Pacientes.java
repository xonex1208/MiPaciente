package com.proyecto.mipaciente.fragments.ui.pacientes;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.activities.RegistroDoctor;
import com.proyecto.mipaciente.modelos.Paciente;

import java.util.Calendar;

public class Pacientes extends Fragment implements AdapterView.OnItemSelectedListener
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
    private String correoExiste="hola";
    private int anioI;
    private int mesI;
    private int diaI;
    private boolean sexoSeleccionado=false;
    private boolean fechaSeleccionada=false;
    private boolean correoExistenteB=false;

    FirebaseFirestore bd;
    String emailPatron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ModeloPacientes galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        galleryViewModel =
                ViewModelProviders.of(this).get(ModeloPacientes.class);
        View root = inflater.inflate(R.layout.fragment_pacientes, container, false);
        Button guardarBtn;
        Button cancelarBtn;

        nombre = root.findViewById(R.id.registro_nombre_paciente);
        apellidos = root.findViewById(R.id.registro_apellidos_paciente);
        email = root.findViewById(R.id.registro_correo_paciente);
        telefono = root.findViewById(R.id.registro_telefono_paciente);
        edad = root.findViewById(R.id.registro_edad_paciente);
        direccion = root.findViewById(R.id.registro_direccion_paciente);
        estadoCivil = root.findViewById(R.id.registro_estado_civil_paciente);
        parentesco = root.findViewById(R.id.registro_parentesco_paciente);
        redSocial = root.findViewById(R.id.registro_red_social_paciente);
        ocupacion = root.findViewById(R.id.registro_ocupacion_paciente);
        guardarBtn = root.findViewById(R.id.btn_guardar_registro_paciente);
        cancelarBtn = root.findViewById(R.id.btn_cancelar_registro_paciente);
        fechaNacimientoTextView= root.findViewById(R.id.registro_fecha_nacimiento_paciente);
        //Inicializacion de la base de datos
        bd = FirebaseFirestore.getInstance();

        obtenerDatosSpinner(root);

        fechaNacimientoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFecha();
            }
        });
        ponerFecha();
        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
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
                if (validarDatos())
                {
                    obtenerEmail();
                    //Guardar los datos en el modelo
                    if (!correoExistenteB){
                        Paciente paciente = new Paciente(
                                nombreS,
                                apellidosS,
                                emailS,
                                Integer.parseInt(telefonoS),
                                fecha,
                                Integer.parseInt(edadS),
                                sexo,
                                direccionS,
                                estadoCivilS,
                                parentescoS,
                                ocupacionS,
                                redSocialS
                        );
                        dbPaciente.add(paciente)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference)
                                    {
                                        Toast.makeText(getContext(), "Registro existoso",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                    }else {
                        Toast.makeText(getContext(), "Registro no exitoso",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
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
            Toast.makeText(getContext(),"Llene todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }else if (validarEmail())
            {
                if(sexoSeleccionado)
                {
                    if(fechaSeleccionada)
                    {
                        if(!validarTelefono()){
                            Toast.makeText(
                                    getContext(),
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
                                getContext(),
                                "Por favor selecciona una fecha de nacimiento",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else
                {
                    Toast.makeText(
                            getContext(),
                            "Por favor selecciona tu sexo",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }else
            {
                Toast.makeText(
                        getContext(),
                        "Escriba una dirección de correo correcta",
                        Toast.LENGTH_LONG).show();
                return false;
            }
    }
    private boolean validarExistenciaEmail()
    {
        return true;
    }
    private void obtenerEmail()
    {
        bd.collection("paciente").whereEqualTo("email",email.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            boolean existe = false;
                            for (QueryDocumentSnapshot document : task.getResult()){
                                /*Log.d("Datos",document.getId() + " => "+ document.getData());
                                Log.d("Datos",document.getId() + " => "+ document.getData().get("email"));
                                correoExiste = document.getString("email");*/
                                String correoExistente = document.getString("email");
                                if (correoExiste.equals(email.getText().toString()))
                                {
                                    existe=true;
                                }
                                if (existe){
                                    Toast.makeText(
                                            getContext(),
                                            "El correo ya ha sido registrado, intente con otro",
                                            Toast.LENGTH_LONG).show();
                                    correoExistenteB=true;
                                }
                                System.out.println("OBtener datos");

                            }
                        }else
                        {
                            Toast.makeText(getContext(), "Error al obtener los datos",Toast.LENGTH_LONG).show();
                        }
                    }
        });
        System.out.println("Returning");
    }

    private boolean validarEmail()
    {
        return email.getText().toString().matches(emailPatron);
    }

    private boolean validarTelefono(){
        return telefono.getText().toString().length() == 10;
    }

    private void mostrarFecha()
    {
        Calendar calendar= Calendar.getInstance();
        anioI = calendar.get(Calendar.YEAR);
        mesI = calendar.get(Calendar.MONTH);
        diaI = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
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

    private void obtenerDatosSpinner(View root)
    {
        Spinner spinner = root.findViewById(R.id.registro_sexo_paciente);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.sexo, android.R.layout.simple_spinner_dropdown_item);
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