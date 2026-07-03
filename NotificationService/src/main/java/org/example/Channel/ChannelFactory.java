package org.example.Channel;

import org.example.Enums.ChannelMode;

import java.util.HashMap;
import java.util.List;

public class ChannelFactory {

    private final HashMap<ChannelMode, MethodChannel> channelFactory;

    public ChannelFactory(List<MethodChannel> methodChannels) {
        this.channelFactory = new HashMap<>();
        for (MethodChannel channel : methodChannels)
            channelFactory.put(channel.getChannelMode(), channel);
    }

    public MethodChannel getChannel(ChannelMode mode){
        return channelFactory.get(mode);
    }
}
