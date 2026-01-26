package de.iu.boardgame.feature_send_message.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_evaluate.viewmodel.RatingViewModel;
import de.iu.boardgame.feature_send_message.data.Message;
import de.iu.boardgame.feature_send_message.viewmodel.ChatViewModel;
import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_user.helpers.SessionManager;
import de.iu.boardgame.feature_user.data.UserDao;

public class ChatActivity extends AppCompatActivity {

    private ChatViewModel viewModel;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int meetingId = getIntent().getIntExtra("meeting_id", -1);
        long currentUserId = SessionManager.getCurrentUserId(this);

        recyclerView = findViewById(R.id.chatRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(currentUserId);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // Messages mit User aus Datenbank holen
        viewModel.getMessagesForMeeting(meetingId).observe(this, messages -> {
            adapter.setMessages(messages);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        //Adapter mit Daten befüllen
        recyclerView.setAdapter(adapter);

        EditText input = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        //Send button um Nachricht abzuschicken
        sendButton.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return;

            Message m = new Message(currentUserId,text,meetingId, LocalDateTime.now());

            //Nachricht in Datenbank speichern
            viewModel.sendMessage(m);
        });
    }
}
