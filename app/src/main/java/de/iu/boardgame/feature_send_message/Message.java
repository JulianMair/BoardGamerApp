package de.iu.boardgame.feature_send_message;

public class Message {
    private String sender;
    private String text;
    private long timestamp;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSender() { return sender; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
}
