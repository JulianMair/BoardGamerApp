package de.iu.boardgame.feature_spiele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;

public class ManageGameAdapter extends RecyclerView.Adapter<ManageGameAdapter.VH> {

    public interface Listener {
        void onEdit(Game game);
        void onDelete(Game game);
    }

    private final Listener listener;
    private final List<Game> items = new ArrayList<>();

    public ManageGameAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<Game> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_manage_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Game g = items.get(position);

        holder.tvName.setText(g.name);
        holder.tvMeta.setText(g.category + " • " + g.durationMinutes + " min");

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(g));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(g));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvMeta;
        Button btnEdit, btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvGameName);
            tvMeta = itemView.findViewById(R.id.tvGameMeta);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
