package de.iu.boardgame.feature_send_message;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.feature_send_message.Message;

public class MockMessageRepository {

    private static MockMessageRepository instance;
    private final List<Message> messages = new ArrayList<>();

    private MockMessageRepository() {}

    public static MockMessageRepository getInstance() {
        if (instance == null) {
            instance = new MockMessageRepository();
        }
        return instance;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }
}
