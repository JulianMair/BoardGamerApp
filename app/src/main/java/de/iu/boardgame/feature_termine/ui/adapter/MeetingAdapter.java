package de.iu.boardgame.feature_termine.ui.adapter;

import android.util.Log;
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

/** RecyclerView-Adapter für die Darstellung der Meeting-Liste. */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    // Die Datenquelle. Wir initialisieren sie leer, um NullPointerExceptions zu vermeiden.
    private List<Meeting> meetingList = new ArrayList<>();

    // --- INTERFACE FÜR KLICKS ---

    // Interface für Click-Events zur Kommunikation mit der Activity.
    public interface OnItemClickListener {
        void onItemClick(Meeting meeting);
    }

    // Methode, damit die Activity sich als Zuhörer registrieren kann
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // --- VIEWHOLDER ---
    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTitle;
        public MeetingViewHolder(View view) {
            super(view);
            // Verbinden der Variablen mit den IDs des XML (termine_item_meeting.xml)
            this.tvDate = view.findViewById(R.id.tvItemDateTime);
            this.tvTitle = view.findViewById(R.id.tvTitle);
        }
    }

    // --- 1. LAYOUT ERSTELLEN ---

    // Flexibles Layout für Listen-Elemente.
    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.termine_item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    // --- 2. DATEN REINSCHREIBEN ---

    // Bindet Daten an den ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        // Welches Meeting sind wir gerade? (Basierend auf der Position in der Liste)
        Meeting currentMeeting = meetingList.get(position);

        // Daten formatieren und anzeigen
        holder.tvDate.setText(currentMeeting.getFormatedDate() + "\n" + currentMeeting.getFormatedTime() + " Uhr");
        holder.tvTitle.setText(currentMeeting.getTitle());

        // Klick-Listener auf die GANZE Zeile (itemView) setzen
        holder.itemView.setOnClickListener(view -> {
            // Wir prüfen, ob jemand zuhört (Listener nicht null)
            if(listener != null){
                // Wir feuern das Event und geben das angeklickte Meeting mit
                listener.onItemClick(currentMeeting);
            }
        });
    }

    // gibt die Anzahl der Elemente in der Liste zurück
    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    /**
     * Diese Methode wird vom ViewModel/Fragment aufgerufen,
     * wenn neue Daten aus der Datenbank kommen.
     * @param meetings Die neue Liste mit Meetings
     */
    public void setMeetings(List<Meeting> meetings){
        this.meetingList = meetings;

        // WICHTIG: Sagt dem RecyclerView, das die Daten sich geändert haben und neu gezeichnet werden müssen"
        notifyDataSetChanged();
    }

    public List<Meeting> getMeetings(){
        return this.meetingList;
    }
}
