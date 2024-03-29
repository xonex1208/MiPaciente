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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private TextView direccion;
    private ImageView avatarPaciente;



    private String idPaciente;
    private String urlAvatar;

    private String telefonoS;

    private FirebaseFirestore bd;
    private DocumentReference documentReference;


    private static final int PERMISO_DADO = 1;


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
        direccion= findViewById(R.id.perfil_paciente_direccion);
        bd=FirebaseFirestore.getInstance();
        Bundle bundle= getIntent().getExtras();


        if(!bundle.isEmpty())
        {
            telefonoS=getIntent().getExtras().getString("telefono");
            idPaciente=getIntent().getExtras().getString("idPaciente");
            /*telefono.setText(getIntent().getExtras().getString("telefono"));
            nombre.setText(getIntent().getExtras().getString("nombre"));
            email.setText(getIntent().getExtras().getString("email"));
            edad.setText(getIntent().getExtras().getString("edad"));
            fechaNacimiento.setText(getIntent().getExtras().getString("fechaNacimiento"));
            sexo.setText(getIntent().getExtras().getString("sexo"));
            direccion.setText(getIntent().getExtras().getString("direccion"));
            urlAvatar=getIntent().getExtras().getString("url");*/
            //Toast.makeText(this,"ID: "+getIntent().getExtras().get("idPaciente"),Toast.LENGTH_LONG).show();

            documentReference = bd.collection("paciente").document(idPaciente);
            /*documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        documentSnapshot=task.getResult();
                        if (documentSnapshot.exists())
                        {
                            obtenerDatos();
                        }
                        else
                        {
                            Log.d("HOLA","ERROR AL OBTENER LOS DATOS");
                        }
                    }
                }
            });
*/
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e !=null)
                    {
                        Log.d("HOLA","ERRO EN EL LISTENER");
                        return;
                    }
                    if (documentSnapshot!=null && documentSnapshot.exists())
                    {
                        obtenerDatos(documentSnapshot);
                    }
                    else
                    {
                        Log.d("ERROR","ERROR AL OBTENER LOS DATOS");
                    }
                }
            });
        }
        else
            {
            Toast.makeText(this,"No se recibieron datos",Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.perfil_paciente_historialBoton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PerfilPaciente.this,ExpedienteClinico.class));
            }
        });

        findViewById(R.id.perfil_paciente_editarPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PerfilPaciente.this,EditarPerfil.class);
                intent.putExtra("pacienteID",idPaciente);
                intent.putExtra("urlImagen",urlAvatar);
                startActivity(intent);
            }
        });

        findViewById(R.id.perfil_paciente_cerrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.perfil_paciente_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPaciente();
            }
        });

        findViewById(R.id.perfil_paciente_historialBoton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilPaciente.this,ExpedienteClinico.class);
                intent.putExtra("idPaciente",idPaciente);
                startActivity(intent);
            }
        });

        findViewById(R.id.enviar_smsBoton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", telefonoS);
                smsIntent.putExtra("sms_body","Hola, buen día: ");
                startActivity(smsIntent);
            }
        });

        findViewById(R.id.llamar_Boton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hacerLlamada();
            }
        });
    }

    private void obtenerDatos(DocumentSnapshot documentSnapshot)
    {
        urlAvatar=documentSnapshot.getString("imagenPaciente");
        Picasso.get().load(urlAvatar).fit().centerCrop().into(avatarPaciente);
        String nombreCompleto = documentSnapshot.getString("nombre")+" "+documentSnapshot.getString("apellidos");
        nombre.setText(nombreCompleto);
        email.setText(documentSnapshot.getString("email"));
        edad.setText(""+documentSnapshot.getData().get("edad"));
        sexo.setText(documentSnapshot.getString("sexo"));
        fechaNacimiento.setText(documentSnapshot.getString("fechaNacimiento"));
        direccion.setText(documentSnapshot.getString("direccion"));
        telefonoS=documentSnapshot.getString("telefono");
        telefono.setText(telefonoS);
    }

    private void hacerLlamada()
    {
        if (ContextCompat.checkSelfPermission(PerfilPaciente.this,
                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PerfilPaciente.this,
                    new String[]{Manifest.permission.CALL_PHONE},PERMISO_DADO);
        }else {
            //El tel: va por defecto y sin espacios
            String dial = "tel:"+telefonoS;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISO_DADO)
        {
            if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                hacerLlamada();
            }
            else
            {
                Toast.makeText(this,"Permiso denegado",Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void eliminarDatos()
    {
        bd.collection("paciente").document(idPaciente)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PerfilPaciente.this,"Paciente eliminado",Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PerfilPaciente.this,"Error al eliminar el paciente",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void eliminarPaciente()
    {
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
    }
}
