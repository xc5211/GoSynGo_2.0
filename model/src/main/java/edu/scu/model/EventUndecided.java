package edu.scu.model;

/**
 * Created by chuanxu on 5/27/16.
 */
public class EventUndecided {

    public String title;
    public String leaderName;
    public String eventId;
    public String leaderId;

    public EventUndecided(String title, String leaderName, String eventId, String leaderId) {
        this.title = title;
        this.leaderName = leaderName;
        this.eventId = eventId;
        this.leaderId = leaderId;
    }

    public String eventId() {
        return this.eventId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
