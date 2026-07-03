package org.example.Model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IdempotencyStore {

    private final Set<Integer> processedIds = ConcurrentHashMap.newKeySet();

    public boolean isProcessed(int id){
        return processedIds.contains(id);
    }

    public boolean markIfNotProcessed(int id){
        return processedIds.add(id);
    }
}
