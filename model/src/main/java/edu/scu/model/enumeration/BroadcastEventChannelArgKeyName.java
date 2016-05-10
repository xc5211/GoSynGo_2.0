package edu.scu.model.enumeration;

/**
 * Created by chuanxu on 5/9/16.
 */
public enum BroadcastEventChannelArgKeyName {

    CHANNEL_NAME("channelName"),

    EVENT_ID("eventId"),

    LEADER_ID("leaderId"),

    EVENT_MANAGEMENT_STATE("eventManagementState"),

    MESSAGE("message"),

    EVENT_TITLE("eventTitle"),

    EVENT_NOTE("eventNote"),

    EVENT_LOCATION("eventLocation"),

    EVENT_LEADER("eventLeader"),

    MEMBER_ID("memberId");


    private String keyName;

    BroadcastEventChannelArgKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

}
