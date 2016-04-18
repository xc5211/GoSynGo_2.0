package edu.scu.api;

import java.util.List;

/**
 * Created by chuanxu on 4/13/16.
 */
public class ApiResponse<T> {

    private String event;
    private String msg;
    private T obj;
    private List<T> objLists;

    public ApiResponse(String event, String msg) {
        this.event = event;
        this.msg = msg;
        this.obj = null;
        this.objLists = null;
    }

    public ApiResponse(String event, String msg, T obj) {
        this(event, msg);
        this.obj = obj;
    }

    public ApiResponse(String event, String msg, List<T> objLists) {
        this(event, msg);
        this.objLists = objLists;
    }

    public ApiResponse(String event, String msg, T obj, List<T> objLists) {
        this(event, msg);
        this.obj = obj;
        this.objLists = objLists;
    }

    public String getEvent() {
        return this.event;
    }

    public String getMsg() {
        return this.msg;
    }

    public boolean isSuccess() {
        return this.event.equals("0");
    }

    public T getObj() {
        return this.obj;
    }

    public List<T> getObjLists() {
        return this.objLists;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public void setObjLists(List<T> objLists) {
        this.objLists = objLists;
    }

}
