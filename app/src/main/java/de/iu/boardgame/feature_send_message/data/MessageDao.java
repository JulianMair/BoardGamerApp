package de.iu.boardgame.feature_send_message.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface MessageDao {

    @Insert
    long insert(Message message);

    @Query("SELECT * FROM messages WHERE meeting_id = :meetingId ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesForMeeting(int meetingId);


}
