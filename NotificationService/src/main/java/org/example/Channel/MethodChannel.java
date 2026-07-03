package org.example.Channel;

import org.example.Enums.ChannelMode;

public interface MethodChannel {

    boolean send(int userId, String message);

    ChannelMode getChannelMode();

}
