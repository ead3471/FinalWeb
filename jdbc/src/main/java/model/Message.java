package model;

import java.time.Instant;

/**
 * Created by Freemind on 2016-11-07.
 */
public class Message {
    private int fromUserId;
    private int toUserId;
    private Instant timeStamp;
    private String text;

    public Message(int fromUserId, int toUserId,String text ,Instant timeStamp ) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.timeStamp = timeStamp;
        this.text=text;
    }

    public Message(int fromUserId, int toUserId,String text) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;

        this.text=text;
    }



    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", timeStamp=" + timeStamp +
                ", text='" + text + '\'' +
                '}';
    }
}
