package com.example.cickmoviev2.data.models;

public class Video {
    private final String site;
    private final String key;

    public Video(String key, String site) {
        this.key = key;
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }
}
