package com.proyecto.mipaciente.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.modelos.ExpedientePaciente;

public class ExpedienteClinico extends AppCompatActivity {

    private LinearLayout botones;
    private Switch alergias;
    private Switch embarazo;
    private Switch extraccionDental;
    private Switch medicamento;
    private Switch hospitalizado;
    private TextInputLayout enfermedades;
    private String idPaciente;

    private boolean alergiasB;
    private boolean embarazoB;
    private boolean extraccionDentalB;
    private boolean medicamentoB;
    private boolean hospitalizadoB;

    private  String enfermedadesS;
    private ProgressDialog progressDialog;


    private FirebaseFirestore bd;
    private DocumentReference documentReference;
    private DocumentSnapshot documentSnapshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expediente_clinico);
        Toolbar toolbar = findViewById(R.id.toolbarExpedienteClinico);
        toolbar.setTitle("Expediente clínico");
        setSupportActionBar(toolbar);

        botones=findViewById(R.id.expediente_layout_botones);
        alergias=findViewById(R.id.expediente_alergias);
        embarazo=findViewById(R.id.expediente_embarazo);
        extraccionDental=findViewById(R.id.expediente_extraccion_dental);
        medicamento=findViewById(R.id.expediente_medicamento);
        hospitalizado=findViewById(R.id.expediente_hospitalizado);

        enfermedades=findViewById(R.id.expediente_enfermedades);
        progressDialog = new ProgressDialog(this);

        alergias.setChecked(false);
        embarazo.setChecked(false);
        extraccionDental.setChecked(false);
        medicamento.setChecked(false);
        hospitalizado.setChecked(false);

        obtenerDatosSwitch();

        bd=FirebaseFirestore.getInstance();

        findViewById(R.id.expediente_guardarDatosBoton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarVacios();
                registrarDatos();
                botones.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.expediente_cancelarBoton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();

        if (!bundle.isEmpty())
        {
            idPaciente=getIntent().getExtras().getString("idPaciente");
            documentReference=bd.collection("expedienteClinico").document(idPaciente);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    documentSnapshot=task.getResult();
                    if (task.isSuccessful())
                    {
                        obtenerDatos();
                    }else
                    {
                        Log.d("HOLa","No hay DATOS OBTENIDOS");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("HOLa","No hay datos obtenidos");
                }
            });
        }
        else
        {
            Toast.makeText(this,"No se recibieron datos",Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerDatos()
    {
        enfermedades.getEditText().setText(documentSnapshot.getString("enfermedades"));

        if (documentSnapshot.getBoolean("alergias"))
        {
            alergias.setChecked(true);
        }else
        {
            alergias.setChecked(false);
        }
        if (documentSnapshot.getBoolean("embarazo"))
        {
            embarazo.setChecked(true);
        }else
        {
            embarazo.setChecked(false);
        }
        if (documentSnapshot.getBoolean("extraccionDental"))
        {
            extraccionDental.setChecked(true);
        }else
        {
            extraccionDental.setChecked(false);
        }
        if (documentSnapshot.getBoolean("medicamento"))
        {
            medicamento.setChecked(true);
        }else
        {
            medicamento.setChecked(false);
        }
        if (documentSnapshot.getBoolean("hospitalizado"))
        {
            hospitalizado.setChecked(true);
        }else
        {
            hospitalizado.setChecked(false);
        }
    }

    private void obtenerDatosSwitch()
    {
        alergias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaActivado) {
                if (estaActivado)
                {
                    alergias.setChecked(true);
                    alergiasB=true;
                    alergias.setText(R.string.si);
                } else {
                    alergias.setChecked(false);
                    alergiasB=false;
                    alergias.setText(R.string.no);
                }
            }
        });

        embarazo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaActivado) {
                if (estaActivado)
                {
                    alergias.setChecked(true);
                    embarazoB=true;
                    alergias.setText(R.string.si);
                } else {
                    alergias.setChecked(false);
                    embarazoB=false;
                    alergias.setText(R.string.no);
                }
            }
        });

        extraccionDental.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaActivado) {
                if (estaActivado)
                {
                    alergias.setChecked(true);
                    extraccionDentalB=true;
                    alergias.setText(R.string.si);
                } else {
                    alergias.setChecked(false);
                    extraccionDentalB=false;
                    alergias.setText(R.string.no);
                }
            }
        });

        medicamento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaActivado) {
                if (estaActivado)
                {
                    alergias.setChecked(true);
                    medicamentoB=true;
                    alergias.setText(R.string.si);
                } else {
                    alergias.setChecked(false);
                    medicamentoB=false;
                    alergias.setText(R.string.no);
                }
            }
        });

        hospitalizado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaActivado) {
                if (estaActivado)
                {
                    alergias.setChecked(true);
                    hospitalizadoB=true;
                    alergias.setText(R.string.si);
                } else {
                    alergias.setChecked(false);
                    hospitalizadoB=false;
                    alergias.setText(R.string.no);
                }
            }
        });

    }

    private void registrarDatos()
    {
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        ExpedientePaciente expedientePaciente = new ExpedientePaciente(
                alergiasB,
                embarazoB,
                extraccionDentalB,
                medicamentoB,
                hospitalizadoB,
                enfermedadesS,
                idPaciente
        );

        bd.collection("expedienteClinico").document(idPaciente)
                .set(expedientePaciente).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(
                        ExpedienteClinico.this,
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
                        ExpedienteClinico.this,
                        "Fallo al guardar el expediente clínico",
                        Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void validarVacios()
    {
        if (enfermedades.getEditText().getText().toString().isEmpty())
        {
            enfermedadesS="";
        }else
        {
            enfermedadesS=enfermedades.getEditText().getText().toString();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_v2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.editar_icono:
                botones.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Ya puedes modificar",Toast.LENGTH_LONG).show();
                break;
            case R.id.cerrar_icono_normal:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
