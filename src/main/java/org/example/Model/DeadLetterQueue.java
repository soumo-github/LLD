package org.example.Model;

import java.util.LinkedList;
import java.util.Queue;

public class DeadLetterQueue {

    private final Queue<NotificationMessage> dlq = new LinkedList<>();

    public void add(NotificationMessage message){
        dlq.add(message);
        System.out.println("Message added to DLQ with message id: " + message.getId());
    }

    public Queue<NotificationMessage> getDlq() {
        return dlq;
    }
}
