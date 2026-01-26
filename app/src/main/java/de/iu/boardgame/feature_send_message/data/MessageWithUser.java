package de.iu.boardgame.feature_send_message.data;

import java.time.LocalDateTime;

//Datenklasse ist nur für UI in Verwendung
public class MessageWithUser extends Message{
    public int meetingId;
    public String text;
    public LocalDateTime timestamp;

    public String username;

    public MessageWithUser(long senderUserId,String username, String text, int meetingId,LocalDateTime timestamp) {
        super(senderUserId, text, meetingId,timestamp);
        this.username = username;
    }

    public String getUsername(){
        return username;

    }
    public void setUsername(String username){
        this.username = username;
    }
}

