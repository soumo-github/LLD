package org.example.Channel;

import org.example.Enums.ChannelMode;

public class TelegramChannel implements MethodChannel{
    @Override
    public boolean send(int userId, String message) {
        System.out.println("Message sent to userID: " + userId +
                " -> " + message + " via Telegram");
        return true;
    }

    @Override
    public ChannelMode getChannelMode() {
        return ChannelMode.TELEGRAM;
    }
}
