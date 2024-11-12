package es.unizar.eina.parcelapad.ui.parcelas;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;

public class ParcelaListAdapter extends ListAdapter<Parcela, ParcelaViewHolder> {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ParcelaListAdapter(@NonNull DiffUtil.ItemCallback<Parcela> diffCallback) {
        super(diffCallback);
    }

    @Override
    public ParcelaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ParcelaViewHolder.create(parent);
    }

    public Parcela getCurrent() {
        return getItem(getPosition());
    }

    @Override
    public void onBindViewHolder(ParcelaViewHolder holder, int position) {

        Parcela current = getItem(position);
        holder.bind(current.getNombre());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    static class ParcelaDiff extends DiffUtil.ItemCallback<Parcela> {

        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            android.util.Log.d ( "ParcelaDiff" , "areItemsTheSame " + oldItem.getNombre() + " vs " + newItem.getNombre() + " " +  (oldItem.getNombre().equals(newItem.getNombre())));
            return oldItem.getNombre() == newItem.getNombre();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            //android.util.Log.d ( "ParcelaDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getDesc().equals(newItem.getDesc()); //puesto para descripción... añadir el resto si ncesario TODO
        }
    }
}
