package de.iu.boardgame.feature_spiele.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * RecyclerView Adapter für die Anzeige von Games in einer Liste.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    /**
     * Interne Liste der aktuell angezeigten Games.
     * Nur der Adapter verändert diese Liste.
     */
    private final List<Game> games = new ArrayList<>();

    /**
     * Aktualisiert die Liste mittels DiffUtil (kein komplettes Neuladen).
     *
     * @param newGames neue Daten (darf null sein)
     */
    public void submitList(List<Game> newGames) {
        List<Game> incoming = (newGames == null) ? Collections.emptyList() : new ArrayList<>(newGames);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GameDiffCallback(games, incoming));

        games.clear();
        games.addAll(incoming);

        // informiert RecyclerView, was sich geändert hat
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_card, parent, false);

        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        // Element holen und an ViewHolder binden
        Game game = games.get(position);
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    /**
     * ViewHolder hält Referenzen auf Views, um findViewById nicht ständig aufzurufen.
     */
    static class GameViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView subTextView;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvGameName);
            subTextView = itemView.findViewById(R.id.tvGameMeta);
        }

        /**
         * Bindet ein Game an die UI-Elemente.
         */
        void bind(@NonNull Game game) {
            // Titel anzeigen
            nameTextView.setText(game.gameTitle);

            // Kategorie • Dauer
            String subText = buildSubText(game);
            subTextView.setText(subText);
        }

        private String buildSubText(@NonNull Game game) {
            String category = game.category;
            int durationMinutes = game.gameDuration;

            // z.B. "Party • 45 min"
            return category + " \u2022 " + durationMinutes + " min";
        }
    }

    /**
     * DiffUtil Callback:
     * - areItemsTheSame: identifiziert dasselbe Item (per ID)
     * - areContentsTheSame: prüft, ob sich sichtbarer Inhalt geändert hat
     */
    static class GameDiffCallback extends DiffUtil.Callback {

        private final List<Game> oldList;
        private final List<Game> newList;

        GameDiffCallback(List<Game> oldList, List<Game> newList) {
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
            // Identifiziert, ob es sich um dasselbe DB-Item handelt
            // Vergleich über Primary Key ID
            return oldList.get(oldItemPosition).id
                    == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Game oldGame = oldList.get(oldItemPosition);
            Game newGame = newList.get(newItemPosition);

            // Prüft, ob sich sichtbarer Inhalt geändert hat
            // (nur Felder, die in der UI angezeigt werden)
            return Objects.equals(oldGame.gameTitle, newGame.gameTitle)
                    && Objects.equals(oldGame.category, newGame.category)
                    && oldGame.gameDuration == newGame.gameDuration;
        }

    }
}
