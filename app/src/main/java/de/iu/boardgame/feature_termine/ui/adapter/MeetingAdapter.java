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

/**
 * Der Adapter ist der "Vermittler" zwischen den rohen Daten (Liste von Meetings)
 * und der Anzeige (RecyclerView).
 * Er entscheidet:
 * 1. Wie sieht eine einzelne Zeile aus? (onCreateViewHolder)
 * 2. Welche Daten kommen in welche Zeile? (onBindViewHolder)
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    // Die Datenquelle. Wir initialisieren sie leer, um NullPointerExceptions zu vermeiden.
    private List<Meeting> meetingList = new ArrayList<>();

    // --- INTERFACE FÜR KLICKS ---
    // Der Adapter selbst soll nicht entscheiden, was beim Klick passiert (z.B. Activity wechseln).
    // Er meldet den Klick nur nach "draußen" an die Activity/Fragment.
    public interface OnItemClickListener {
        void onItemClick(Meeting meeting);
    }

    // Methode, damit die Activity sich als Zuhörer registrieren kann
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // --- VIEWHOLDER ---
    // Der ViewHolder ist ein "Cache". Er merkt sich die Textfelder einer Zeile,
    // damit Android nicht bei jedem Scrollen neu nach 'findViewById' suchen muss.
    // Das macht die Liste flüssig (Performance).
    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;

        private TextView tvTitle;
        public MeetingViewHolder(View view) {
            super(view);
            // Hier verbinden wir die Variablen mit den IDs aus der XML (termine_item_meeting.xml)
            this.tvDate = view.findViewById(R.id.tvItemDateTime);
            this.tvTitle = view.findViewById(R.id.tvTitle);
        }
    }

    // --- 1. LAYOUT ERSTELLEN ---
    // Diese Methode wird aufgerufen, wenn der RecyclerView eine NEUE Zeile braucht.
    // Sie "bläst" das XML-Layout auf (Inflation) und erstellt einen ViewHolder.
    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.termine_item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    // --- 2. DATEN REINSCHREIBEN ---
    // Diese Methode wird STÄNDIG aufgerufen, wenn eine Zeile ins Bild gescrollt wird.
    // Hier verbinden wir das Java-Objekt (Meeting) mit der Anzeige (ViewHolder).
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
        Log.d("CRASH", "2");
    }

    // Sagt dem RecyclerView, wie viele Elemente die Liste insgesamt hat.
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
}
