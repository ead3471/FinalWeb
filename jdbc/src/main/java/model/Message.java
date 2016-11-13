package model;

import java.time.Instant;

/**
 * Created by Freemind on 2016-11-07.
 */
public class Message {
    private User fromUser;
    private User toUser;
    private Instant timeStamp;
    private String text;

    public Message(User fromUser, User toUser, String text , Instant timeStamp ) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.timeStamp = timeStamp;
        this.text=text;
    }

    public Message(User fromUser, User toUser, String text) {
        this.fromUser = fromUser;
        this.toUser = toUser;

        this.text=text;
    }



    public User getFromUser() {
        return fromUser;
    }

    public User getToUserId() {
        return toUser;
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
                "fromUser=" + fromUser.getLogin() +
                ", toUserId=" + toUser.getLogin() +
                ", timeStamp=" + timeStamp +
                ", text='" + text + '\'' +
                '}';
    }
}
