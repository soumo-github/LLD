package org.example;

import org.example.Channel.*;
import org.example.Db.MessageQueue;
import org.example.Enums.ChannelMode;
import org.example.Model.DeadLetterQueue;
import org.example.Model.IdempotencyStore;
import org.example.Service.NotificationProcessor;
import org.example.Service.NotificationService;
import org.example.Service.UserPreferredService;

import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<MethodChannel> channels = List.of(
                new EmailChannel(),
                new SMSChannel(),
                new WhatsAppChannel(),
                new TelegramChannel()
        );
        ChannelFactory factory = new ChannelFactory(channels);
        UserPreferredService userPreferredService = new UserPreferredService();
        userPreferredService.setPreference(2, ChannelMode.TELEGRAM);
        userPreferredService.setPreference(12, ChannelMode.SMS);
        userPreferredService.setPreference(23, ChannelMode.EMAIL);
//        userPreferredService.setPreference(21, ChannelMode.WHATSAPP);
        MessageQueue queue = new MessageQueue();
        DeadLetterQueue dlq = new DeadLetterQueue();
        IdempotencyStore idempotencyStore = new IdempotencyStore();
        NotificationService notificationService = new NotificationService(queue);
        NotificationProcessor processor =
                new NotificationProcessor(queue, userPreferredService,
                        factory, dlq, idempotencyStore);
        notificationService.publish(2, "Code XX215FG!!");
        notificationService.publish(21, "Code RED56!!");
        notificationService.publish(23, "TEST Running!!");
        notificationService.publish(12, "CODE Running!!");
        processor.start();

    }
}
/* OUTPUT
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.1.1\lib\idea_rt.jar=61754" -Dfile.encoding=UTF-8 -classpath C:\Users\dharc\IdeaProjects\NS2\target\classes org.example.Main
Message added to queue with id : 1
Message added to queue with id : 2
Message added to queue with id : 3
Message added to queue with id : 4
Message sent to userID: 2 -> Code XX215FG!! via Telegram
Error processing message: java.lang.NullPointerException: Cannot invoke "java.util.List.iterator()" because the return value of "org.example.Service.UserPreferredService.getUserPreferredMode(int)" is null
Message added to DLQ with message id: 2
Message sent to userID: 23 -> TEST Running!! via Email
Message sent to userID: 12 -> CODE Running!! via SMS

*/
