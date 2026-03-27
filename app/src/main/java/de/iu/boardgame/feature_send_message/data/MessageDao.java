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

    @Query("SELECT * FROM message WHERE meeting_id = :meetingId ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesForMeeting(int meetingId);



    /** Join mit der User Tabelle, um für die Nachricht den Absender zu bekommen */
    @Query("""
        SELECT
            m.id,
            m.senderUserId,
            m.meeting_id AS meetingId,
            m.text,
            m.timestamp,
            u.name AS username
        FROM message m
        INNER JOIN users u ON m.senderUserId = u.id
        WHERE m.meeting_id = :meetingId
        ORDER BY m.timestamp ASC
    """)
    LiveData<List<MessageWithUser>> getMessagesForMeetingWithUser(int meetingId);
}
