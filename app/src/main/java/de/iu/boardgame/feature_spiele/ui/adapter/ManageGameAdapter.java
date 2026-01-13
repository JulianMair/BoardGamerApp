package de.iu.boardgame.feature_spiele.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_spiele.data.Game;

/**
 * Adapter für den "Manage Games" Screen (Ändern oder Löschen eines Eintrags).
 */
public class ManageGameAdapter extends RecyclerView.Adapter<ManageGameAdapter.ManageGameViewHolder> {

    /**
     * Callback für User-Aktionen auf einem Game (Edit/Delete).
     */
    public interface Listener {
        void onEdit(@NonNull Game game);
        void onDelete(@NonNull Game game);
    }

    private final Listener listener;

    /**
     * Interne Liste der aktuell angezeigten Spiele.
     * Wird nur im Adapter verändert.
     */
    private final List<Game> games = new ArrayList<>();

    public ManageGameAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }

    /**
     * Aktualisiert die Liste mittels DiffUtil (dadurch kein komplettes Neuladen).
     * - Nutzt Game.id als stabile Identität (Primary Key).
     */
    public void submitList(List<Game> newGames) {
        List<Game> incoming = (newGames == null) ? Collections.emptyList() : new ArrayList<>(newGames);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new GameDiffCallback(games, incoming)
        );

        games.clear();
        games.addAll(incoming);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ManageGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_manage_card, parent, false);

        return new ManageGameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageGameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.bind(game, listener);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    /**
     * ViewHolder hält View-Referenzen und bindet Game-Daten an die UI.
     */
    static class ManageGameViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView metaTextView;
        private final Button editButton;
        private final Button deleteButton;

        ManageGameViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvGameName);
            metaTextView = itemView.findViewById(R.id.tvGameMeta);
            editButton = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }

        /**
         * Bindet Daten + Click-Callbacks.
         */
        void bind(@NonNull Game game, @NonNull Listener listener) {
            nameTextView.setText(game.gameTitle);
            metaTextView.setText(buildMetaText(game));

            editButton.setOnClickListener(v -> listener.onEdit(game));
            deleteButton.setOnClickListener(v -> listener.onDelete(game));
        }

        private String buildMetaText(@NonNull Game game) {
            String category = (game.category == null) ? "" : game.category;
            int durationMinutes = game.gameDuration;
            return category + " \u2022 " + durationMinutes + " min";
        }
    }

    /**
     * DiffUtil Callback:
     * - areItemsTheSame: identifiziert dasselbe DB-Item über die ID
     * - areContentsTheSame: vergleicht UI-relevante Felder
     */
    static class GameDiffCallback extends DiffUtil.Callback {

        private final List<Game> oldList;
        private final List<Game> newList;

        GameDiffCallback(@NonNull List<Game> oldList, @NonNull List<Game> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // Stabile Identität: Room Primary Key
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Game oldGame = oldList.get(oldItemPosition);
            Game newGame = newList.get(newItemPosition);

            // Nur das vergleichen, was sichtbar ist
            return Objects.equals(oldGame.gameTitle, newGame.gameTitle)
                    && Objects.equals(oldGame.category, newGame.category)
                    && oldGame.gameDuration == newGame.gameDuration;
        }
    }
}
