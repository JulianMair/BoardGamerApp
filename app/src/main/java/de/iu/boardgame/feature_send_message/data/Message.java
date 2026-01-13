package de.iu.boardgame.feature_send_message.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

import de.iu.boardgame.feature_termine.data.Meeting;

@Entity(
        tableName = "messages",
        foreignKeys = @ForeignKey(
                entity = Meeting.class,
                parentColumns = "meeting_id",
                childColumns = "meeting_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("meeting_id")}  // Index auf die Foreign Key-Spalte
)
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sender;
    private String text;
    private long timestamp;

    @ColumnInfo(name = "meeting_id")
    private int meetingId;  // Verbindung zum Meeting

    public Message(String sender, String text, int meetingId) {
        this.sender = sender;
        this.text = text;
        this.meetingId = meetingId;
        this.timestamp = System.currentTimeMillis();
    }

    // --- Getter + Setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }
}
