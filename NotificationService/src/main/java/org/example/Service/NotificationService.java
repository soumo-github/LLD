package org.example.Service;

import org.example.Db.MessageQueue;
import org.example.Model.NotificationMessage;

import java.util.*;

public class NotificationService {

    private final MessageQueue kafkaTemplate;

    public NotificationService(MessageQueue kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(int userId, String msg){

        NotificationMessage message = new NotificationMessage(userId, msg);
        kafkaTemplate.send(message);
        System.out.println("Message added to queue with id : " + message.getId());
    }

}
