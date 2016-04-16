package edu.scu.model;

import java.io.Serializable;

/**
 * Created by chuanxu on 4/13/16.
 */
public enum StatusEvent implements Serializable {
    Pending(0), Ready(1), Cancelled(2);

    private int status;

    StatusEvent (int status) {
        this.status = status;
    }

    int getStatus() {
        return status;
    }
}
