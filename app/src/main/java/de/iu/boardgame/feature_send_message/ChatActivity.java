package de.iu.boardgame.feature_send_message;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;

public class ChatActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<String> displayMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView chatList = findViewById(R.id.chatList);
        EditText input = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        displayMessages = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayMessages
        );
        chatList.setAdapter(adapter);

        // vorhandene Nachrichten laden
        loadMessages();

        sendButton.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return;

            // Eigene Nachricht
            MockMessageRepository.getInstance()
                    .addMessage(new Message("Ich", text));

            input.setText("");
            loadMessages();

            // Fake-Antwort simulieren
            new Handler().postDelayed(() -> {
                MockMessageRepository.getInstance()
                        .addMessage(new Message("Alex", "Alles klar 👍"));

                Toast.makeText(
                        this,
                        "Neue Nachricht von Alex",
                        Toast.LENGTH_SHORT
                ).show();

                loadMessages();
            }, 2000);
        });
    }

    private void loadMessages() {
        displayMessages.clear();
        for (Message m : MockMessageRepository.getInstance().getMessages()) {
            displayMessages.add(m.getSender() + ": " + m.getText());
        }
        adapter.notifyDataSetChanged();
    }
}
