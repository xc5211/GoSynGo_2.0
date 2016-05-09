package edu.scu.model.enumeration;

/**
 * Created by chuanxu on 4/13/16.
 */
public enum StatusEvent {

    Pending(0), Ready(1), Cancelled(2), Tentative(3), Past(4);

    private int status;

    StatusEvent (int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
