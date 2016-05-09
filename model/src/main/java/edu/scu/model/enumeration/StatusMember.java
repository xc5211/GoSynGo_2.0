package edu.scu.model.enumeration;

/**
 * Created by chuanxu on 4/13/16.
 */
public enum StatusMember {

    Pending(0), Accept(1), Declined(2);

    private int status;

    StatusMember(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
