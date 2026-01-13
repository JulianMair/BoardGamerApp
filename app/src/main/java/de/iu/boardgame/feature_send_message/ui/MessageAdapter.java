package de.iu.boardgame.feature_send_message.ui;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_send_message.data.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private final String currentUser = "Ich";

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
        Message m = messages.get(position);
        holder.messageText.setText(m.getText());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.messageTime.setText(sdf.format(new Date(m.getTimestamp())));

        LinearLayout container = holder.messageContainer;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getLayoutParams();
        if (m.getSender().equals(currentUser)) {
            params.gravity = Gravity.END;
            container.setBackgroundColor(0xFFDCF8C6);
        } else {
            params.gravity = Gravity.START;
            container.setBackgroundColor(0xFFFFFFFF);
        }
        container.setLayoutParams(params);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;
        LinearLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }
}
