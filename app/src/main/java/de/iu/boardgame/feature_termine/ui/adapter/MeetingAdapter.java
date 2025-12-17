package de.iu.boardgame.feature_termine.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.data.Meeting;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private List<Meeting> meetingList = new ArrayList<>();

    // Methode zur definition des onCLickListeners
    public interface OnItemClickListener {
        void onItemClick(Meeting meeting);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvlocation;
        public MeetingViewHolder(View view) {
            super(view);
            this.tvDate = view.findViewById(R.id.tvItemDate);
            this.tvlocation = view.findViewById(R.id.tvItemLocation);
        }
    }

    //Layout für die Zeile
    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    // Schreibt die Daten in die Zeile
    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        Meeting currentMeeting = meetingList.get(position);

        //Daten in die Textfelder schreiben (U M W A N D EL time?`?????)
        holder.tvDate.setText("Datum: " + currentMeeting.getDate());
        holder.tvlocation.setText(currentMeeting.getLocation());

        //Klicks auf das Item anfangen
        holder.itemView.setOnClickListener(view -> {
            if(listener != null){
                listener.onItemClick(currentMeeting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    public void setMeetings(List<Meeting> meetings){
        this.meetingList = meetings;
        // Logge die Größe der neuen Liste
        android.util.Log.d("ADAPTER_CHECK", "Neue Liste erhalten! Anzahl: " + meetings.size());
        notifyDataSetChanged();
    }
}
