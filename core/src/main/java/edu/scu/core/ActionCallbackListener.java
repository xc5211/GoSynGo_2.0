package edu.scu.core;

/**
 * Created by chuanxu on 4/14/16.
 */
public interface ActionCallbackListener<T> {

    public void onSuccess(T data);

    public void onFailure(String message);

}
