package ru.mastkey.fj_2024.lesson5.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BlackList {
    private final Set<String> blackList = new HashSet<>();

    public void addToBlackList(String token) {
        blackList.add(token);
    }

    public void removeFromBlackList(String token) {
        blackList.remove(token);
    }

    public boolean isBlackListed(String token) {
        return blackList.contains(token);
    }
}
