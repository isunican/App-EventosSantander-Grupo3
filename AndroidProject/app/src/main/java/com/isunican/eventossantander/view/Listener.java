package com.isunican.eventossantander.view;

public interface Listener<T> {

    void onSuccess(T data);

    void onFailure();

}
