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

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.feature_evaluate.ui.RatingListActivity;
import de.iu.boardgame.feature_send_message.ui.ChatActivity;
import de.iu.boardgame.feature_termine.data.Meeting;

public class MeetingAdaptertest extends RecyclerView.Adapter<MeetingAdaptertest.ViewHolder> {

    private List<Meeting> events;
    private Context context;

    public MeetingAdaptertest(Context context, List<Meeting> events) {
        this.context = context;
        this.events = events;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting event = events.get(position);
        holder.tvDate.setText("📅 " + event.getFormatedDate());
        holder.tvLocation.setText("📍 " + event.getLocation());
        holder.tvHost.setText("👤 " + event.getHost_id());
        holder.tvTime.setText(""+ event.getFormatedTime());

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
            Intent intent = new Intent(context, RatingListActivity.class);
            intent.putExtra("meeting_id", event.getMeeting_id());
            context.startActivity(intent);
        });

        holder.btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("meeting_id", event.getMeeting_id());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public void setData(List<Meeting> newMeetings) {
        this.events = newMeetings != null ? newMeetings : new ArrayList<>();
        notifyDataSetChanged();
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
