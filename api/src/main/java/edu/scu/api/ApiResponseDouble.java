package edu.scu.api;

import java.util.List;

/**
 * Created by Hairong on 4/20/16.
 */
public class ApiResponseDouble<T, E> {

    private String event;
    private String msg;
    private T obj;
    private List<T> objLists;
    private E obj2;
    private List<E> objLists2;

    public ApiResponseDouble(String event, String msg) {
        this.event = event;
        this.msg = msg;
        this.obj = null;
        this.objLists = null;
    }

    public ApiResponseDouble(String event, String msg, T obj) {
        this(event, msg);
        this.obj = obj;
    }

    public ApiResponseDouble(String event, String msg, List<T> objLists) {
        this(event, msg);
        this.objLists = objLists;
    }

    public ApiResponseDouble(String event, String msg, T obj, E obj2) {
        this(event, msg);
        this.obj = obj;
        this.obj2 = obj2;
    }

    public ApiResponseDouble(String event, String msg, T obj, List<T> objLists) {
        this(event, msg);
        this.obj = obj;
        this.objLists = objLists;
    }

    public ApiResponseDouble(String event, String msg, List<T> objLists, E obj2) {
        this(event, msg);
        this.objLists = objLists;
        this.obj2 = obj2;
    }

    public ApiResponseDouble(String event, String msg, List<T> objLists, List<E>objLists2) {
        this(event, msg);
        this.objLists = objLists;
        this.objLists2 = objLists2;
    }

    public ApiResponseDouble(String event, String msg, T obj, List<T> objLists, E obj2) {
        this(event, msg);
        this.obj = obj;
        this.objLists = objLists;
        this.obj2 = obj2;
    }

    public ApiResponseDouble(String event, String msg, List<T> objLists, E obj2, List<E> objLists2) {
        this(event, msg);
        this.objLists = objLists;
        this.obj2 = obj2;
        this.objLists2 = objLists2;
    }
    
    public ApiResponseDouble(String event, String msg, T obj, List<T> objLists, E obj2, List<E> objLists2) {
        this(event, msg);
        this.obj = obj;
        this.objLists = objLists;
        this.obj2 = obj2;
        this.objLists2 = objLists2;
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

    public  E getObj2() {return this.obj2; }

    public List<E> getObjLists2() {return this.objLists2; }


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

    public  void setObj2(E obj2) { this.obj2 = obj2; }

    public  void setObjLists2(List<E> objLists2) { this.objLists2 = objLists2; }
}