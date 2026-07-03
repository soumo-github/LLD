package org.example.Channel;

import org.example.Enums.ChannelMode;

public class EmailChannel implements MethodChannel{
    @Override
    public boolean send(int userId, String message) {
        System.out.println("Message sent to userID: " + userId +
                " -> " + message + " via Email");
        return true;
    }

    @Override
    public ChannelMode getChannelMode() {
        return ChannelMode.EMAIL;
    }
}
