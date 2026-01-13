package de.iu.boardgame.feature_send_message.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;
import de.iu.boardgame.feature_termine.data.AppDatabase;

public class MessageRepository {

    private MessageDao messageDao;

    public MessageRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        messageDao = db.messageDao();
    }

    // Nachrichten als LiveData
    public LiveData<List<Message>> getMessagesForMeeting(int meetingId) {
        return messageDao.getMessagesForMeeting(meetingId);
    }

    // Neue Nachricht einfügen
    public void insert(Message message) {
        Executors.newSingleThreadExecutor().execute(() -> messageDao.insert(message));
    }
}
