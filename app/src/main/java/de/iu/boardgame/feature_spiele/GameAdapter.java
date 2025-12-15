package de.iu.boardgame.feature_spiele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameVH> {

    private final List<Game> items = new ArrayList<>();

    public void setItems(List<Game> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_card, parent, false);
        return new GameVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GameVH holder, int position) {
        Game g = items.get(position);
        holder.tvName.setText(g.name);

        String meta = g.category + " • " + g.durationMinutes + " min";
        holder.tvMeta.setText(meta);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GameVH extends RecyclerView.ViewHolder {
        TextView tvName, tvMeta;

        GameVH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvGameName);
            tvMeta = itemView.findViewById(R.id.tvGameMeta);
        }
    }
}
