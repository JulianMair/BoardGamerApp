package de.iu.boardgame.feature_evaluate.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import de.iu.boardgame.feature_termine.data.Meeting;

@Entity(
        tableName = "meeting_ratings",
        foreignKeys = @ForeignKey(
                entity = Meeting.class,
                parentColumns = "meeting_id",
                childColumns = "meeting_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class MeetingRating {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "meeting_id", index = true)
    private int meetingId; //Beziehung zu Meeting

    private float hostRating;      // Gastgeber
    private float foodRating;      // Essen
    private float eveningRating;   // Abend allgemein
    private String comment;        // Freitext

    private LocalDateTime timestamp; // Wann wurde bewertet

    public MeetingRating(int meetingId, float hostRating, float foodRating, float eveningRating, String comment, LocalDateTime timestamp) {
        this.meetingId = meetingId;
        this.hostRating = hostRating;
        this.foodRating = foodRating;
        this.eveningRating = eveningRating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // --- Getter & Setter ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public float getHostRating() {
        return hostRating;
    }

    public void setHostRating(float hostRating) {
        this.hostRating = hostRating;
    }

    public float getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(float foodRating) {
        this.foodRating = foodRating;
    }

    public float getEveningRating() {
        return eveningRating;
    }

    public void setEveningRating(float eveningRating) {
        this.eveningRating = eveningRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
