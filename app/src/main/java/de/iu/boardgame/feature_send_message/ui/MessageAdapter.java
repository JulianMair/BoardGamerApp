package de.iu.boardgame.feature_send_message.ui;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_send_message.data.MessageWithUser;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private long currentUserId;
    private List<MessageWithUser> messages;

    public MessageAdapter(long userid){
        this.currentUserId = userid;
    }

    public void setMessages(List<MessageWithUser> messages) {
        this.messages = messages != null ? messages : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_message, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageWithUser messageWithUser = messages.get(position);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.messageText.setText(messageWithUser.getText());

        // Sicher Name holen
        holder.messageSender.setText(messageWithUser.getUsername());

        holder.messageTime.setText(messageWithUser.getTimestamp().format(formatter));

        // Hier werden die Parameter des Layouts geholt um sie dann Userabhänhig zu verändern
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();

        /** Bestimmung ob die Nachricht von dem angemeldeten User ist oder von einem anderen
         *  Aufgrund dessen ändert sich die Farbe der Nachricht und auch die Anordnung links oder rechts
         */
        if (messageWithUser.getSenderUserId() == currentUserId) {
            params.gravity = Gravity.END;
            holder.messageContainer.setBackgroundResource(R.drawable.bg_message_me);
        } else {
            params.gravity = Gravity.START;
            holder.messageContainer.setBackgroundResource(R.drawable.bg_message_other);
        }

        holder.messageContainer.setLayoutParams(params);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime, messageSender;
        LinearLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
            messageSender = itemView.findViewById(R.id.messageSender);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }
}
