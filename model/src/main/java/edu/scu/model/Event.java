package edu.scu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Event implements Serializable {

    public final static String SERIALIZE_KEY = "event";

    private String title;
    private String note;
    private String location;
    //    private GeoPoint locationGeo;
    private Integer durationInMin;
    private Integer statusEvent;
    private Boolean hasReminder;
    private Integer reminderInMin;
    private Date timestamp;
    private EventLeaderDetail eventLeaderDetail;
    private List<EventMemberDetail> eventMemberDetail;
    private String objectId;
    private String ownerId;
    private Date updated;
    private Date created;

    public Integer getReminderInMin() {
        return reminderInMin;
    }

    public void setReminderInMin(Integer reminderInMin) {
        this.reminderInMin = reminderInMin;
    }

    public Integer getStatusEvent() {
        return statusEvent;
    }

    public void setStatusEvent(Integer statusEvent) {
        this.statusEvent = statusEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(Boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public Integer getDurationInMin() {
        return durationInMin;
    }

    public void setDurationInMin(Integer durationInMin) {
        this.durationInMin = durationInMin;
    }

    public java.util.Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<EventMemberDetail> getEventMemberDetail() {
        return eventMemberDetail;
    }

    public void setEventMemberDetail(List<EventMemberDetail> eventMemberDetail) {
        this.eventMemberDetail = eventMemberDetail;
    }

    public EventLeaderDetail getEventLeaderDetail() {
        return eventLeaderDetail;
    }

    public void setEventLeaderDetail(EventLeaderDetail eventLeaderDetail) {
        this.eventLeaderDetail = eventLeaderDetail;
    }

//    public GeoPoint getLocationGeo() {
//        return locationGeo;
//    }
//
//    public /*void setLocationGeo(GeoPoint locationGeo) {
//        this.locationGeo = locationGeo;
//    }
//
//
//    public Event save() {
//        return Backendless.Data.of(Event.class).save(this);
//    }
//
//    public Long remove() {
//        return Backendless.Data.of(Event.class).remove(this);
//    }
//
//    public static Event findById(String id) {
//        return Backendless.Data.of(Event.class).findById(id);
//    }
//
//    public static Event findFirst() {
//        return Backendless.Data.of(Event.class).findFirst();
//    }
//
//    public static Event findLast() {
//        return Backendless.Data.of(Event.class).findLast();
//    }
//
//    public static BackendlessCollection<Event> find(BackendlessDataQuery query) {
//        return Backendless.Data.of(Event.class).find(query);
//    }*/

    private boolean hasEventMemberDetail(EventMemberDetail eventMemberDetail) {
        for (EventMemberDetail memberDetail : this.eventMemberDetail) {
            if (memberDetail.getObjectId().equals(eventMemberDetail.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public boolean addEventMemberDetail(EventMemberDetail eventMemberDetail) {
        if (!hasEventMemberDetail(eventMemberDetail)) {
            return this.eventMemberDetail.add(eventMemberDetail);
        }
        return false;
    }

    public boolean removeEventMemberDetail(EventMemberDetail eventMemberDetail) {
        if (hasEventMemberDetail(eventMemberDetail)) {
            return this.eventMemberDetail.remove(eventMemberDetail);
        }
        return true;
    }

    public boolean updateEventMemberDetail(EventMemberDetail eventMemberDetail) {
        if (!hasEventMemberDetail(eventMemberDetail)) {
            return false;
        }

        for (int i = 0; i < this.eventMemberDetail.size(); i++) {
            EventMemberDetail currentMemberDetail = this.eventMemberDetail.get(i);
            if (currentMemberDetail.getObjectId().equals(eventMemberDetail.getObjectId())) {
                this.eventMemberDetail.set(i, currentMemberDetail);
                return true;
            }
        }
        assert false;
        return false;
    }

    public void updateEventLeaderDetail(EventLeaderDetail updatedEventLeaderDetail) {
        this.eventLeaderDetail = updatedEventLeaderDetail;
    }

    public EventMemberDetail getEventMemberDetail(String memberId) {
        for (EventMemberDetail memberDetail : this.eventMemberDetail) {
            if (memberDetail.getMember().getObjectId().equals(memberId)) {
                return memberDetail;
            }
        }
        assert false;
        return null;
    }

    public boolean hasEventMember(String memberEmail) {
        if (this.eventMemberDetail == null) {
            return false;
        }

        for (EventMemberDetail memberDetail : this.eventMemberDetail) {
            if (memberDetail.getMember().getEmail().trim().equals(memberEmail)) {
                return true;
            }
        }
        return false;
    }
}
