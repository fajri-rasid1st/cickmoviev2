package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.SerializedName;

public class Cast {
    private final String name;
    private final String character;

    @SerializedName("profile_path")
    private final String profileImgUrl;

    public Cast(String name, String character, String profileImgUrl) {
        this.name = name;
        this.character = character;
        this.profileImgUrl = profileImgUrl;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }
}
