package com.back.office.entity;

public class Message {

    private int message_id;
    private String message_from;
    private String message_to;
    private String message;
    private boolean read_un;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getMessage_from() {
        return message_from;
    }

    public void setMessage_from(String message_from) {
        this.message_from = message_from;
    }

    public String getMessage_to() {
        return message_to;
    }

    public void setMessage_to(String message_to) {
        this.message_to = message_to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead_un() {
        return read_un;
    }

    public void setRead_un(boolean read_un) {
        this.read_un = read_un;
    }
}
