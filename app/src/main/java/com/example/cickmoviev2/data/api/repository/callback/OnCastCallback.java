package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.Credit;

public interface OnCastCallback {
    void onSuccess(Credit credit, String message);

    void onFailure(String message);
}
