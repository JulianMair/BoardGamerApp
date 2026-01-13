package de.iu.boardgame.feature_send_message.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_send_message.viewmodel.ChatViewModel;
import de.iu.boardgame.feature_send_message.MockMessageRepository;

public class ChatActivity extends AppCompatActivity {

    private ChatViewModel viewModel;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private int meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        meetingId = getIntent().getIntExtra("meeting_id", -1);
        if (meetingId == -1) {
            Toast.makeText(this, "Kein Meeting ausgewählt!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.setMeetingId(meetingId);

        recyclerView = findViewById(R.id.chatRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Startet Chat unten
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(MockMessageRepository.getInstance().getMessages());
        recyclerView.setAdapter(adapter);

        EditText input = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        // LiveData beobachten
        adapter = new MessageAdapter(new ArrayList<>()); // leere Liste
        recyclerView.setAdapter(adapter);

        viewModel.getMessages().observe(this, messages -> {
            adapter.setMessages(messages);       // Liste aktualisieren
            adapter.notifyDataSetChanged();      // RecyclerView neu zeichnen
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        viewModel.getMessages().observe(this, messages -> {
            adapter.notifyDataSetChanged(); // <- nur notify, aber die Liste im Adapter wird nicht aktualisiert
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        sendButton.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return;

            viewModel.sendMessage("Ich", text);
            input.setText("");


            new Handler().postDelayed(() -> {
                viewModel.sendMessage("Alex", "Alles klar 👍");
                Toast.makeText(this, "Neue Nachricht von Alex", Toast.LENGTH_SHORT).show();
            }, 2000);
        });
    }
}
