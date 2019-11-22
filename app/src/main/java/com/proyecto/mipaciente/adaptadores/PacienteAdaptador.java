/**
 * @PacienteAdaptador.java 8/noviembre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase adapatadora para obtener datos de Firebase
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 1.0.0
 */

package com.proyecto.mipaciente.adaptadores;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.modelos.Paciente;
import com.squareup.picasso.Picasso;

public class PacienteAdaptador extends FirestoreRecyclerAdapter<Paciente,PacienteAdaptador.PacienteHolder>
{
    private OnItemClickListener listener;
    private String urlPaciente;
    private FirebaseStorage firebaseStorage;

    public PacienteAdaptador(@NonNull FirestoreRecyclerOptions<Paciente> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PacienteHolder holder, int position, @NonNull Paciente model)
    {
        //Se obtienen los datos del modelo, que seran mostrados
        holder.textViewNombre.setText(model.getNombre());
        holder.textViewTelefono.setText(model.getTelefono());
        holder.textViewEdad.setText("Edad: "+ model.getEdad());
        urlPaciente= model.getImagenPaciente();
        Picasso.get().load(model.getImagenPaciente()).fit().centerCrop().into(holder.imageViewAvatar);
    }

    @NonNull
    @Override
    public PacienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente,parent,false);
        return new PacienteHolder(vista);
    }

    public void borrarItem(final int posicion)
    {
        //Borrar documento y fotografia del Paciente de la BD
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imagenReferencia = firebaseStorage.getReferenceFromUrl(urlPaciente);
        imagenReferencia.delete().addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                getSnapshots().getSnapshot(posicion).getReference().delete();
            }
        });
        //Eliminar paciente con delete, y se le envia la posicion del documento a eliminar

    }




    class PacienteHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener
    {
        TextView textViewNombre;
        TextView textViewTelefono;
        TextView textViewEdad;
        ImageView imageViewAvatar;

        public PacienteHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewNombre=itemView.findViewById(R.id.nombre_paciente_item);
            textViewTelefono=itemView.findViewById(R.id.telefono_paciente_item);
            textViewEdad=itemView.findViewById(R.id.edad_paciente_item);
            imageViewAvatar=itemView.findViewById(R.id.imagen_paciente_item);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            //Habiliar clic en el item
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int posicion = getAdapterPosition();
                    if (posicion!= RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(posicion),posicion);
                    }
                }
            });
        }

        @Override
        public void onClick(View view)
        {
            if (listener!=null)
            {
                int posicion = getAdapterPosition();
                if (posicion!= RecyclerView.NO_POSITION)
                {
                    listener.onItemClick(posicion);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
        {
            //Mostrar menu con long click
            contextMenu.setHeaderTitle("Escoge una opción");
            MenuItem llamar = contextMenu.add(Menu.NONE,1,1,"Llamar");
            MenuItem enviarSms = contextMenu.add(Menu.NONE,2,2,"Enviar SMS");
            MenuItem borrar = contextMenu.add(Menu.NONE,3,3,"Eliminar paciente");
            llamar.setOnMenuItemClickListener(this);
            enviarSms.setOnMenuItemClickListener(this);
            borrar.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if (listener!=null)
            {
                //Escoger metodo en base al click dado por el usuario
                int posicion = getAdapterPosition();
                if (posicion!= RecyclerView.NO_POSITION)
                {
                  switch (menuItem.getItemId())
                  {
                      case 1:
                          listener.onLlamarClick(posicion);
                          return  true;
                      case 2:
                          listener.onEnviarSmsClick(posicion);
                          return true;
                      case 3:
                          listener.onBorrarClick(posicion);
                          return true;
                  }
                }
            }
            return false;
        }
    }

    //Se crea una interfaz del item

    public interface OnItemClickListener
    {
        void onItemClick(DocumentSnapshot documentSnapshot, int posicion);
        void onItemClick(int posicion);
        //Metodos para long click
        void onLlamarClick(int posicion);
        void onEnviarSmsClick(int posicion);
        void onBorrarClick(int posicion);
    }



    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
