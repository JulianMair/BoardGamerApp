package de.iu.boardgame.feature_evaluate.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_evaluate.data.MeetingRating;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private List<MeetingRating> daten = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /** Wird vom ViewModel-Observer aufgerufen */
    public void setData(List<MeetingRating> neueDaten) {
        this.daten = neueDaten != null ? neueDaten : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingRating rating = daten.get(position);

        // Datum formatieren (LocalDateTime)
        if (rating.getTimestamp() != null) {
            holder.txtDatum.setText("📅 " + rating.getTimestamp().format(formatter));
        } else {
            holder.txtDatum.setText("📅 Kein Datum angegeben");
        }

        // Ratings (1–5 Sterne)
        holder.ratingGastgeber.setRating(rating.getHostRating());
        holder.ratingEssen.setRating(rating.getFoodRating());
        holder.ratingAbend.setRating(rating.getEveningRating());

        // Kommentar
        String comment = rating.getComment();
        holder.txtKommentar.setText(
                (comment == null || comment.trim().isEmpty())
                        ? "– Kein Kommentar –"
                        : "„" + comment + "“"
        );
    }

    @Override
    public int getItemCount() {
        return daten != null ? daten.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDatum, txtKommentar;
        RatingBar ratingGastgeber, ratingEssen, ratingAbend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDatum = itemView.findViewById(R.id.txtDatum);
            ratingGastgeber = itemView.findViewById(R.id.ratingGastgeber);
            ratingEssen = itemView.findViewById(R.id.ratingEssen);
            ratingAbend = itemView.findViewById(R.id.ratingAbend);
            txtKommentar = itemView.findViewById(R.id.txtKommentar);
        }
    }
}
