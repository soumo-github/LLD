package org.example.Db;

import org.example.Model.NotificationMessage;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {

    Queue<NotificationMessage> queue = new LinkedList<>();

    public void send(NotificationMessage message){
        queue.add(message);
    }

    public NotificationMessage poll(){
        return queue.peek();
    }

    public void acknowledge(){
        queue.poll();
    }

    public boolean isQueueEmpty(){
        return queue.isEmpty();
    }

}
