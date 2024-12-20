package es.unizar.eina.reservapad.ui.reservas;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.reservapad.database.reservas.Reserva;

public class ReservaListAdapter extends ListAdapter<Reserva, ReservaViewHolder> {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ReservaListAdapter(@NonNull DiffUtil.ItemCallback<Reserva> diffCallback) {
        super(diffCallback);
    }

    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ReservaViewHolder.create(parent);
    }

    public Reserva getCurrent() {
        return getItem(getPosition());
    }

    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {

        Reserva current = getItem(position);
        holder.bind(String.valueOf(current.getNombreCliente()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    static class ReservaDiff extends DiffUtil.ItemCallback<Reserva> {

        @Override
        public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            android.util.Log.d ( "ReservaDiff" , "areItemsTheSame " + oldItem.getID() + " vs " + newItem.getID() + " " +  (oldItem.getID().equals(newItem.getID())));
            return oldItem.getID().equals(newItem.getID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            //android.util.Log.d ( "ReservaDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getFechaEntrada().equals(newItem.getFechaEntrada())
                    && oldItem.getFechaSalida().equals(newItem.getFechaSalida())
                    && oldItem.getNombreCliente().equals(newItem.getNombreCliente())
                    && oldItem.getTlfCliente().equals(newItem.getTlfCliente());
        }
    }
}
