package edu.scu.model.enumeration;

/**
 * Created by chuanxu on 5/9/16.
 */
public enum BroadcastEventChannelArgValueName {

    PROPOSE_EVENT_TIME_MESSAGE("Please propose event time for all members to select."),

    SELECT_EVENT_FINAL_TIME_MESSAGE("All members have voted event time. Please make the final time decision.");


    private String valueName;

    BroadcastEventChannelArgValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueName() {
        return valueName;
    }

}
