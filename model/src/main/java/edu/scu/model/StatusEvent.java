package edu.scu.model;

/**
 * Created by chuanxu on 4/13/16.
 */
public enum StatusEvent {
    Pending(0), Ready(1), Cancelled(2), Tentative(3);

    private int status;

    StatusEvent (int status) {
        this.status = status;
    }

    int getStatus() {
        return status;
    }
}
