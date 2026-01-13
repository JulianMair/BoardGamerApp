package de.iu.boardgame.feature_send_message.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.iu.boardgame.feature_send_message.data.Message;
import de.iu.boardgame.feature_send_message.data.MessageRepository;
import de.iu.boardgame.feature_send_message.MockMessageRepository;

public class ChatViewModel extends AndroidViewModel {

    private final MessageRepository repository;
    private final MutableLiveData<Integer> meetingIdLive = new MutableLiveData<>();
    private LiveData<List<Message>> messages;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = new MessageRepository(application);
    }

    // Meeting ID setzen
    public void setMeetingId(int meetingId) {
        meetingIdLive.setValue(meetingId);
        messages = repository.getMessagesForMeeting(meetingId);
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void sendMessage(String sender, String text) {
        Integer meetingId = meetingIdLive.getValue();
        if (meetingId == null) return;

        Message message = new Message(sender, text, meetingId);

        // Mock speichern
        MockMessageRepository.getInstance().addMessage(message);

        // Room speichern
        repository.insert(message);
    }
}
