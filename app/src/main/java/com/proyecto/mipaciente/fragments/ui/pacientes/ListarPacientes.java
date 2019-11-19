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
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.activities.RegistrarPaciente;
import com.proyecto.mipaciente.adaptadores.PacienteAdaptador;
import com.proyecto.mipaciente.fragments.Inicio;
import com.proyecto.mipaciente.modelos.Paciente;


public class ListarPacientes extends Fragment {

    private FirebaseFirestore bd;
    private CollectionReference pacienteReferencia;
    private PacienteAdaptador adaptador;
    public ListarPacientes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listar_pacientes, container, false);
        bd=FirebaseFirestore.getInstance();
        pacienteReferencia=bd.collection("paciente");
        Query query = pacienteReferencia.orderBy("edad",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Paciente> options = new FirestoreRecyclerOptions.Builder<Paciente>()
                .setQuery(query,Paciente.class)
                .build();
        adaptador = new PacienteAdaptador(options);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_pacientes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptador);
        //Eliminar paciente con un swipe a la izquierda
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Habilitar swipe
                adaptador.borrarItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        //Se manda llamar a la interfaz, ya previamente creada, para hacer una accion al realizar un clic en ella
        adaptador.setOnItemClickListener(new PacienteAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int posicion) {
                Paciente paciente = documentSnapshot.toObject(Paciente.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(getContext(),"Posicion: "+posicion+" ID: "+id,Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(),Inicio.class));
            }
        });

        FloatingActionButton botonAgregarPaciente= root.findViewById(R.id.boton_agregar_paciente);
        botonAgregarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RegistrarPaciente.class));
            }
        });
        // Inflate the layout for this fragment
        return root;
    }



    @Override
    public void onStart() {
        super.onStart();
        adaptador.startListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptador.stopListening();
    }
}
