package com.proyecto.mipaciente.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.proyecto.mipaciente.R;
import com.proyecto.mipaciente.modelos.Paciente;

public class PacienteAdaptador extends FirestoreRecyclerAdapter<Paciente,PacienteAdaptador.PacienteHolder>
{
    private OnItemClickListener listener;

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
        holder.textViewEdad.setText(String.valueOf(model.getEdad()));
    }

    @NonNull
    @Override
    public PacienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente,parent,false);
        return new PacienteHolder(vista);
    }

    public void borrarItem(int posicion)
    {
        //Eliminar paciente con delete, y se le envia la posicion
        getSnapshots().getSnapshot(posicion).getReference().delete();
    }

    class PacienteHolder extends  RecyclerView.ViewHolder
    {
        TextView textViewNombre;
        TextView textViewTelefono;
        TextView textViewEdad;

        public PacienteHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewNombre=itemView.findViewById(R.id.nombre_paciente_item);
            textViewTelefono=itemView.findViewById(R.id.telefono_paciente_item);
            textViewEdad=itemView.findViewById(R.id.edad_paciente_item);
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
    }

    //Se crea una interfaz del item

    public interface OnItemClickListener
    {
        void onItemClick(DocumentSnapshot documentSnapshot, int posicion);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
