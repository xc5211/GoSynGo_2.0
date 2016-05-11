package edu.scu.model.enumeration;

/**
 * Created by chuanxu on 5/10/16.
 */
public enum PublishEventChannelArgKeyName {

    MEMBER_STATUS("memberStatus"),

    MEMBER_CALENDAR("memberCalendar"),

    MEMBER_SELECTED_TIME("memberSelectedTime"),

    MEMBER_PROPOSED_TIME("memberProposedTime"),

    MEMBER_MINS_TO_ARRIVE("memberMinsToArrive");


    private String keyName;

    PublishEventChannelArgKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

}
