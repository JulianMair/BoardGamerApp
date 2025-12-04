package de.iu.boardgame.feature_termine;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import  java.time.LocalDate;
import java.time.LocalTime;


@Entity(tableName = "meeting")
public class Meeting {
    @PrimaryKey(autoGenerate = true)
    int meeting_id;
    @ColumnInfo(name="date")
    LocalDate date;
    @ColumnInfo(name="time")
    LocalTime time;
    @ColumnInfo(name="location")
    String location;
    //@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "host_id")
    @ColumnInfo(name="host_id")
    long host_id;
    @ColumnInfo(name="status")
    String status;

    @ColumnInfo(name="title")
    String title;

    public Meeting(String title, LocalDate date, LocalTime time, String location, long host_id, String status) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.host_id = host_id;
        this.status = status;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public long getHost_id() {
        return this.host_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setDate(LocalDate date) {
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

}
