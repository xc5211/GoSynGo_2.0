package edu.scu.model;

import java.util.Date;
import java.util.List;

public class Event {

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
    @Deprecated
    private List<LeaderProposedTimestamp> proposedTimestamps;
    private String ownerId;
    private String objectId;
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

    public String getObjectId() {
        return objectId;
    }

    public java.util.Date getUpdated() {
        return updated;
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

    @Deprecated
    public List<LeaderProposedTimestamp> getProposedTimestamps() {
        return proposedTimestamps;
    }

    @Deprecated
    public void setProposedTimestamps(List<LeaderProposedTimestamp> proposedTimestamps) {
        this.proposedTimestamps = proposedTimestamps;
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

    public boolean hasProposedTimestamp(LeaderProposedTimestamp timestamp) {
        for (LeaderProposedTimestamp proposedTimestamp : this.proposedTimestamps) {
            if (proposedTimestamp.getObjectId().equals(timestamp.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public boolean addProposedTimestamp(LeaderProposedTimestamp timestamp) {
        if (!hasProposedTimestamp(timestamp)) {
            return this.proposedTimestamps.add(timestamp);
        }
        return false;
    }

    public boolean removeProposedTimestamp(LeaderProposedTimestamp timestamp) {
        if (hasProposedTimestamp(timestamp)) {
            return this.proposedTimestamps.remove(timestamp);
        }
        return false;
    }

    public boolean updateProposedTimestamp(LeaderProposedTimestamp timestamp) {
        if (!hasProposedTimestamp(timestamp)) {
            return false;
        }

        for (int i = 0; i < this.proposedTimestamps.size(); i++) {
            LeaderProposedTimestamp currentProposedTimestamp = this.proposedTimestamps.get(i);
            if (currentProposedTimestamp.getObjectId().equals(timestamp.getObjectId())) {
                this.proposedTimestamps.set(i, currentProposedTimestamp);
                return true;
            }
        }
        assert false;
        return false;
    }

}
