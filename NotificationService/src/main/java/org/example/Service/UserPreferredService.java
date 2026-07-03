package org.example.Service;

import org.example.Enums.ChannelMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserPreferredService {

    private final HashMap<Integer, List<ChannelMode>> userPreferredMode = new HashMap<>();

    public void setPreference(int userId, ChannelMode mode){
        List<ChannelMode> modes =
                userPreferredMode.getOrDefault(userId, new ArrayList<>());
        modes.add(mode);
        userPreferredMode.put(userId, modes);
    }

    public List<ChannelMode> getUserPreferredMode(int userId) {
        return userPreferredMode.get(userId);
    }
}