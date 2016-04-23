package edu.scu.model;

import java.util.Date;
import java.util.List;

public class Event {

    private Integer reminderInMin;
    private Integer statusEvent;
    private String title;
    private String ownerId;
    private String objectId;
    private Date updated;
    private String note;
    private Date created;
    private Boolean hasReminder;
    private Integer durationInMin;
    private Date timestamp;
    private String location;
    private List<EventMemberDetail> eventMemberDetail;
    private List<LeaderProposedTimestamp> proposedTimestamps;
    private Person leader;
//    private GeoPoint locationGeo;

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

    public List<LeaderProposedTimestamp> getProposedTimestamps() {
        return proposedTimestamps;
    }

    public void setProposedTimestamps(List<LeaderProposedTimestamp> proposedTimestamps) {
        this.proposedTimestamps = proposedTimestamps;
    }

    public Person getLeader() {
        return leader;
    }

    public void setLeader(Person leader) {
        this.leader = leader;
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

}
