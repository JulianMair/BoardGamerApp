package de.iu.boardgame.feature_send_message.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

import java.time.LocalDateTime;

import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_user.data.User;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Meeting.class,
                        parentColumns = "meeting_id",
                        childColumns = "meeting_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "senderUserId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("meeting_id"),
                @Index("senderUserId")
        }// Index auf die Foreign Key-Spalte
)
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long senderUserId;
    private String text;
    private LocalDateTime timestamp;

    @ColumnInfo(name = "meeting_id")
    private int meetingId;  // Verbindung zum Meeting

    public Message(long senderUserId, String text, int meetingId,LocalDateTime timestamp) {
        this.senderUserId = senderUserId;
        this.text = text;
        this.meetingId = meetingId;
        this.timestamp = timestamp;
    }

    // --- Getter + Setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getSenderUserId() { return senderUserId; }
    public void setSenderUserId(long senderUserId) { this.senderUserId = senderUserId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }
}
