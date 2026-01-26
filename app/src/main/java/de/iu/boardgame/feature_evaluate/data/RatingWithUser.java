package de.iu.boardgame.feature_evaluate.data;

import java.time.LocalDateTime;


/** Datenklasse um zu einem Rating den User zu speichern der das Rating erstellt hat */
public class RatingWithUser extends MeetingRating{

    public int id;
    public int meetingId;
    public long userId;

    public float hostRating;
    public float foodRating;
    public float eveningRating;
    public String comment;
    public LocalDateTime timestamp;

    public String username; // 👈 aus users.name

    public RatingWithUser(int meetingId, long userId, float hostRating, float foodRating, float eveningRating, String comment, LocalDateTime timestamp, String username) {
        super(meetingId, userId, hostRating, foodRating, eveningRating, comment, timestamp);
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
}

