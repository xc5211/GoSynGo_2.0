package edu.scu.api;

import java.util.List;

/**
 * Created by chuanxu on 5/5/16.
 */
public interface Responsable<T> {

    public String getEvent();
    
    public boolean isSuccess();

    public T getObj();
    
    public List<T> getObjLists();

    public String getMsg();

    public void setEvent(String event);

    public void setMsg(String msg);

}
