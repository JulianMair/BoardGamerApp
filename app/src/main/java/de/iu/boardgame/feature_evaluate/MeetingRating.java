package de.iu.boardgame.feature_evaluate;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "meeting_rating")
public class MeetingRating {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private float hostRating;      // Gastgeber
    private float foodRating;      // Essen
    private float eveningRating;   // Abend allgemein
    private String comment;        // Freitext

    private LocalDateTime timestamp;        // Wann wurde bewertet

    public MeetingRating(float hostRating, float foodRating, float eveningRating, String comment, LocalDateTime timestamp) {
        this.hostRating = hostRating;
        this.foodRating = foodRating;
        this.eveningRating = eveningRating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // Getter + Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

