package es.unizar.eina.reservapad.ui.reservas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.notepad.R;


class ReservaViewHolder extends RecyclerView.ViewHolder {
    private final TextView mReservaItemView;

    private ReservaViewHolder(View itemView) {
        super(itemView);
        mReservaItemView = itemView.findViewById(R.id.textView);

        // Registra el menú contextual para este elemento
        itemView.setOnLongClickListener(v -> {
            v.showContextMenu();
            return true; // Indica que el evento fue manejado
        });
    }

    public void bind(String text) {
        mReservaItemView.setText(text);
    }

    static ReservaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ReservaViewHolder(view);
    }
}

