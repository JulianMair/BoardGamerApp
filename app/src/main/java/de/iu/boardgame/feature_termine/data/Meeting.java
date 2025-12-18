package de.iu.boardgame.feature_termine.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


@Entity(tableName = "meeting_table")
public class Meeting {
    @PrimaryKey(autoGenerate = true)
    int meeting_id;
    @ColumnInfo(name="timestamp")
    long timestamp;
    @ColumnInfo(name="location")
    String location;
    //@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "host_id")
    @ColumnInfo(name="host_id")
    long host_id;
    @ColumnInfo(name="status")
    String status;

    @ColumnInfo(name="Title")
    String title;

    @ColumnInfo(name="evaluation_id")
    int evaluation_id;


    /**
     * Die Daten werden in der MeetingCreateForm eingegeben
     * Ausnahme: HOST_id muss vom User Objekt übergeben werden.
     * @param title
     * @param timestamp
     * @param location
     * @param host_id
     * @param status
     */
    public Meeting(String title, long timestamp, String location, long host_id, String status) {
        this.title = title;
        this.timestamp = timestamp;
        this.location = location;
        this.host_id = host_id;
        this.status = status;
    }

    public String getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getHost_id() {
        return this.host_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHost_id(long host_id) {
        this.host_id = host_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMeeting_id() {
        return this.meeting_id;
    }

    public void setEvaluation_id(int evaluation_id){this.evaluation_id = evaluation_id;}

    public int getEvaluation_id(){return this.evaluation_id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
