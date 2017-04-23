package com.company;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhinav on 16/04/17.
 */
public class LinkStore {
    private static Map<String, Boolean> links;

    static void init() {
        links = new HashMap<>();
    }

    static Boolean isPresent(String url) {
        return links.get(url) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    static void addToStore(String url) {
        links.put(url, true);
    }

    public static void printStore() {
        for(Map.Entry entry: links.entrySet()) {
            System.out.println(entry.getKey() + "\n");
        }
    }
}
