package es.unizar.eina.parcelapad.ui.parcelas;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.notepad.R;


class ParcelaViewHolder extends RecyclerView.ViewHolder {
    private final TextView mParcelaItemView;

    private ParcelaViewHolder(View itemView) {
        super(itemView);
        mParcelaItemView = itemView.findViewById(R.id.textView);

        // Registra el menú contextual para este elemento
        itemView.setOnLongClickListener(v -> {
            v.showContextMenu();
            return true; // Indica que el evento fue manejado
        });
    }

    public void bind(String text) {
        mParcelaItemView.setText(text);
    }

    static ParcelaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ParcelaViewHolder(view);
    }
}

