package de.iu.boardgame;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.iu.boardgame.feature_evaluate.RatingList;
import de.iu.boardgame.feature_evaluate.RatingPage;
import de.iu.boardgame.feature_termine.Meeting;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private List<Meeting> events;
    private Context context;

    public MeetingAdapter(Context context, List<Meeting> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting event = events.get(position);
        holder.tvDate.setText("📅 " + event.getDate());
        holder.tvLocation.setText("📍 " + event.getLocation());
        holder.tvHost.setText("👤 " + event.getHost_id());
        holder.tvTime.setText(""+ event.getTime());

        // Buttons
        holder.btnEdit.setOnClickListener(v -> {
            // Öffne EditEventActivity mit eventId
        });

        holder.btnSuggestGame.setOnClickListener(v -> {
            // Öffne SuggestGameActivity für diesen Termin
        });

        holder.btnVote.setOnClickListener(v -> {
            // Öffne VoteActivity für diesen Termin
        });

        holder.btnRate.setOnClickListener(v -> {
            Intent intent = new Intent(context, RatingList.class);
            intent.putExtra("eventId", event.getMeeting_id());
            context.startActivity(intent);
        });

        holder.btnMessage.setOnClickListener(v -> {
            // Öffne MessageActivity für diesen Termin
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvLocation, tvHost, tvTime;
        ImageButton btnEdit, btnSuggestGame, btnVote, btnRate, btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvEventDate);
            tvTime = itemView.findViewById(R.id.tvEventTime);
            tvLocation = itemView.findViewById(R.id.tvEventLocation);
            tvHost = itemView.findViewById(R.id.tvEventHost);

            btnEdit = itemView.findViewById(R.id.btnEditEvent);
            btnSuggestGame = itemView.findViewById(R.id.btnSuggestGame);
            btnVote = itemView.findViewById(R.id.btnVote);
            btnRate = itemView.findViewById(R.id.btnRate);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }
}
