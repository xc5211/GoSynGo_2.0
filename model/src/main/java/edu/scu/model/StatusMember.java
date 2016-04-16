package edu.scu.model;

import java.io.Serializable;

/**
 * Created by chuanxu on 4/13/16.
 */
public enum StatusMember implements Serializable {
    Pending(0), Accept(1), Declined(2);

    private int status;

    StatusMember(int status) {
        this.status = status;
    }

    int getStatus() {
        return status;
    }
}
