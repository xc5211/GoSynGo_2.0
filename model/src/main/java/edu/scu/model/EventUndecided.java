package edu.scu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chuanxu on 5/27/16.
 */
public class EventUndecided implements Serializable {

    public static String SERIALIZE_KEY = "eventUndecided";

    public String title;
    public String leaderName;
    public String eventId;
    public String leaderId;
    private String objectId;
    private String ownerId;
    private Date updated;
    private Date created;

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

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
