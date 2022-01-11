package com.brightsight.magisk.net;

public interface ResponseListener<T> {
    void onResponse(T response);
}
