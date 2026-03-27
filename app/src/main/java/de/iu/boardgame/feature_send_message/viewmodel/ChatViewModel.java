package de.iu.boardgame.feature_send_message.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_send_message.data.Message;
import de.iu.boardgame.feature_send_message.data.MessageRepository;
import de.iu.boardgame.feature_send_message.data.MessageWithUser;

public class ChatViewModel extends AndroidViewModel {

    private final MessageRepository repository;
    private LiveData<List<MessageWithUser>> messages;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = new MessageRepository(application);
    }

    public void setMeetingId(int meetingId) {
        messages = repository.getMessagesForMeeting(meetingId);
    }

    public LiveData<List<MessageWithUser>> getMessagesForMeeting(int meetingId) {
        return repository.getMessagesForMeeting(meetingId);
    }

    public void sendMessage(Message m) {
        if (m.getMeetingId() == 0) return;
        repository.insert(m);
    }
}
