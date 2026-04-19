package org.example.Model;

public class NotificationMessage {

    private static int counter = 1;
    private int id = 1;
    private final int userId;
    private final String message;

    public NotificationMessage(int userId, String message){
        this.userId = userId;
        this.message = message;
        id = counter;
        counter++;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
