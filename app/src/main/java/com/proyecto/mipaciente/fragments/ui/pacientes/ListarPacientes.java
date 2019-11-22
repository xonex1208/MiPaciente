/**
 * @ListarPacientes.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase para ver los pacientes del Doctor
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.1
 */

package com.proyecto.mipaciente.fragments.ui.pacientes;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.activities.PerfilPaciente;
import com.proyecto.mipaciente.activities.RegistrarPaciente;
import com.proyecto.mipaciente.adaptadores.PacienteAdaptador;
import com.proyecto.mipaciente.modelos.Paciente;


public class ListarPacientes extends Fragment implements PacienteAdaptador.OnItemClickListener {

    private FirebaseFirestore bd;
    private CollectionReference pacienteReferencia;
    private PacienteAdaptador adaptador;
    public ListarPacientes()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_listar_pacientes, container, false);
        bd=FirebaseFirestore.getInstance();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_pacientes);
        pacienteReferencia=bd.collection("paciente");
        //Ordenar a los pacientes por edad
        Query query = pacienteReferencia.orderBy("edad",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Paciente> datosPacientes = new FirestoreRecyclerOptions.Builder<Paciente>()
                .setQuery(query,Paciente.class)
                .build();
        adaptador = new PacienteAdaptador(datosPacientes);
        //Inicilizacion del recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Se pasa el adaptador al recyclerView para mostrar a los pacientes
        recyclerView.setAdapter(adaptador);
        //Se le envia la clase al adapatador
        adaptador.setOnItemClickListener(this);
        //Eliminar paciente con un swipe a la izquierda
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                //Habilitar swipe
                //adaptador.borrarItem(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(),"Posicion View"+viewHolder.getAdapterPosition(),Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);
        //Se manda llamar a la interfaz, ya previamente creada, para hacer una accion al realizar un clic en ella
        /*adaptador.setOnItemClickListener(new PacienteAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                Paciente paciente = documentSnapshot.toObject(Paciente.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(getContext(),"Posicion: "+posicion+" ID: "+id,Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(),Inicio.class));
            }
        });*/
        FloatingActionButton botonAgregarPaciente= root.findViewById(R.id.boton_agregar_paciente);
        botonAgregarPaciente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(),RegistrarPaciente.class));
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int posicion)
    {
        //Se activa click en el CardView
        Paciente paciente = documentSnapshot.toObject(Paciente.class);
        String id = documentSnapshot.getId();
        String telefono=documentSnapshot.getString("telefono");
        String nombre=documentSnapshot.getString("nombre")
                +" "
                +documentSnapshot.getString("apellidos");
        String edad=documentSnapshot.getData().get("edad").toString();
        String sexo=documentSnapshot.getString("sexo");
        String fechaNacimiento=documentSnapshot.getString("fechaNacimiento");
        String email=documentSnapshot.getString("email");
        String urlAvatar=documentSnapshot.getString("imagenPaciente");
        String path = documentSnapshot.getReference().getPath();
        Toast.makeText(
                getContext(),
                "Posicion: "+posicion+" ID: "+paciente.getApellidos(),
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(),PerfilPaciente.class);
        intent.putExtra("telefono",telefono);
        intent.putExtra("nombre",nombre);
        intent.putExtra("edad",edad);
        intent.putExtra("sexo",sexo);
        intent.putExtra("fechaNacimiento",fechaNacimiento);
        intent.putExtra("email",email);
        intent.putExtra("url",urlAvatar);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int posicion)
    {
        Toast.makeText(getContext(),"ItemClick: "+posicion,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLlamarClick(int posicion)
    {
        Toast.makeText(getContext(),"Llamar: "+posicion,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEnviarSmsClick(int posicion)
    {
        Toast.makeText(getContext(),"EnviarSMS: "+posicion,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBorrarClick(int posicion)
    {
        adaptador.borrarItem(posicion);
        Toast.makeText(getContext(),"Eliminado: "+posicion,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        adaptador.startListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adaptador.stopListening();
    }
}
