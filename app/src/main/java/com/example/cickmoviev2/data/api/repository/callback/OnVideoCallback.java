package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.VideoResponse;

public interface OnVideoCallback {
    void onSuccess(VideoResponse videoResponse, String message);

    void onFailure(String message);
}
