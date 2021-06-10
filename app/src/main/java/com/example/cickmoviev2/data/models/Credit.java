package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credit {
    @SerializedName("cast")
    @Expose
    private final List<Cast> casts;

    public Credit(List<Cast> casts) {
        this.casts = casts;
    }

    public List<Cast> getCast() {
        return casts;
    }
}
