package org.example.Service;

import org.example.Channel.ChannelFactory;
import org.example.Channel.MethodChannel;
import org.example.Config.CustomThreadPool;
import org.example.Db.MessageQueue;
import org.example.Enums.ChannelMode;
import org.example.Model.DeadLetterQueue;
import org.example.Model.IdempotencyStore;
import org.example.Model.NotificationMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NotificationProcessor {
    private final MessageQueue kafkaConsumer;
    private final UserPreferredService userPreferredService;
    private final ChannelFactory factory;
    private final DeadLetterQueue dlq;
    private final IdempotencyStore markQueue;
    private static final int MAX_RETRIES = 3;

    public NotificationProcessor(MessageQueue kafkaConsumer, UserPreferredService userPreferredService, ChannelFactory factory, DeadLetterQueue dlq, IdempotencyStore markQueue) {
        this.kafkaConsumer = kafkaConsumer;
        this.userPreferredService = userPreferredService;
        this.factory = factory;
        this.dlq = dlq;
        this.markQueue = markQueue;
    }
    List<CompletableFuture<Void>> futures = new ArrayList<>();
    public void start(){
        while (true){
            NotificationMessage message = kafkaConsumer.poll();
            if (message == null)
                break;
            if (!markQueue.markIfNotProcessed(message.getId()))
                continue;
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> processMessage(message),
                            CustomThreadPool.executor).
                            thenAccept(success -> {
                                if (success)
                                    kafkaConsumer.acknowledge();
                                else {
                                    dlq.add(message);
                                    kafkaConsumer.acknowledge();
                                }
                            }).
                            exceptionally( ex -> {
                                    System.out.println("Error processing message: " + ex.getMessage());
                                    dlq.add(message);
                                    kafkaConsumer.acknowledge();
                                    return null;});
            futures.add(future);
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }
    private boolean processMessage(NotificationMessage message){
        int userId = message.getUserId();
        String msg = message.getMessage();
        for (ChannelMode mode : userPreferredService.getUserPreferredMode(userId)){
            MethodChannel channel = factory.getChannel(mode);
            int attempt = 0;
            while (attempt < MAX_RETRIES){
                try {
                    boolean flag = channel.send(message.getUserId(), message.getMessage());
                    if (flag)
                        return true;
                } catch (Exception e) {
                    attempt++;
                    System.out.println("Logging error in processing message with id: " + message.getId() + " attempt " + attempt);
                }
            }
        }
        return false;
    }
}
