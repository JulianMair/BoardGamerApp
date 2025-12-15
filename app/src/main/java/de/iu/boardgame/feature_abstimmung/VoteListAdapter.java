package de.iu.boardgame.feature_abstimmung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;

public class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.VH> {

    public interface Listener {
        void onToggleVote(GameVoteInfo game);
    }

    private final Listener listener;
    private final List<GameVoteInfo> items = new ArrayList<>();
    private int myCount = 0;

    public VoteListAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<GameVoteInfo> newItems, int myCount) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        this.myCount = myCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_void_game, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        GameVoteInfo g = items.get(position);

        h.tvName.setText(g.name);
        h.tvMeta.setText(g.category + " • " + g.durationMinutes + " min");
        h.tvVotes.setText(g.voteCount + " Votes");

        // Wichtig: Listener beim setChecked nicht auslösen
        h.cbMine.setOnCheckedChangeListener(null);
        h.cbMine.setChecked(g.isVotedByMe());

        // Optional: wenn max erreicht, nicht-gewählte deaktivieren
        boolean disable = (myCount >= 3) && !g.isVotedByMe();
        h.cbMine.setEnabled(!disable);

        h.cbMine.setOnCheckedChangeListener((btn, checked) -> listener.onToggleVote(g));
        h.itemView.setOnClickListener(v -> listener.onToggleVote(g)); // Card klickbar
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvMeta, tvVotes;
        CheckBox cbMine;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            tvVotes = itemView.findViewById(R.id.tvVotes);
            cbMine = itemView.findViewById(R.id.cbMine);
        }
    }
}
