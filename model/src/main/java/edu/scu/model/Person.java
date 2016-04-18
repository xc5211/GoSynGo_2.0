package edu.scu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanxu on 4/13/16.
 */
public class Person {

    private String objectId;
    private String email;
    private String name;
    private boolean hasGoogleCalendarImported;
    private List<Event> eventsAsLeader;
    private List<Event> eventsAsMemeber;

    public Person(String objectId, String email, String name, boolean hasGoogleCalendarImported) {
        this.objectId = objectId;
        this.email = email;
        this.name = name;
        this.hasGoogleCalendarImported = hasGoogleCalendarImported;
        this.eventsAsLeader = new ArrayList<>();
        this.eventsAsMemeber = new ArrayList<>();
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public List<Event> getEventsAsLeader() {
        return this.eventsAsLeader;
    }

    public List<Event> getEventsAsMemeber() {
        return this.eventsAsMemeber;
    }

    public boolean hasGoogleCalendarImported() {
        return this.hasGoogleCalendarImported;
    }

    public List<Event> getLeaderEventsAsPending() {
        List<Event> pendingEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus().equals(StatusEvent.Pending)) {
                pendingEvents.add(event);
            }
        }
        return pendingEvents;
    }

    public List<Event> getLeaderEventsAsReady() {
        List<Event> readyEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus().equals(StatusEvent.Ready)) {
                readyEvents.add(event);
            }
        }
        return readyEvents;
    }

    public List<Event> getLeaderEventsAsCancelled() {
        List<Event> declinedEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus().equals(StatusEvent.Cancelled)) {
                declinedEvents.add(event);
            }
        }
        return declinedEvents;
    }

    public List<Event> getLeaderEventsAsTentative() {
        List<Event> tentativeEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus().equals(StatusEvent.Tentative)) {
                tentativeEvents.add(event);
            }
        }
        return tentativeEvents;
    }

    public List<Event> getMemberEventsAsPending() {
        List<Event> pendingEvents = new ArrayList<>();
        for (Event event : this.getEventsAsMemeber()) {
            if (event.getStatus().equals(StatusEvent.Pending)) {
                pendingEvents.add(event);
            }
        }
        return pendingEvents;
    }

    public List<Event> getMemberEventsAsReady() {
        List<Event> readyEvents = new ArrayList<>();
        for (Event event : this.getEventsAsMemeber()) {
            if (event.getStatus().equals(StatusEvent.Ready)) {
                readyEvents.add(event);
            }
        }
        return readyEvents;
    }

    public List<Event> getMemberEventsAsCancelled() {
        List<Event> declinedEvents = new ArrayList<>();
        for (Event event : this.getEventsAsMemeber()) {
            if (event.getStatus().equals(StatusEvent.Cancelled)) {
                declinedEvents.add(event);
            }
        }
        return declinedEvents;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoogleCalendarImported(Boolean imported) {
        this.hasGoogleCalendarImported = imported;
    }

    public boolean addEventAsLeader(Event event) {
        if (!this.getEventsAsLeader().contains(event)) {
            boolean personChange = this.getEventsAsLeader().add(event);
            boolean eventCheck = event.isLeader(this);
            return personChange && eventCheck;
        }
        return true;
    }

    public boolean addEventAsMember(Event event) {
        if (!this.getEventsAsMemeber().contains(event)) {
            boolean personChange = this.getEventsAsMemeber().add(event);
            boolean eventChange = event.addEventMember(this);
            return personChange && eventChange;
        }
        return true;
    }

    public boolean removeEventAsLeader(Event event) {
        if (this.getEventsAsLeader().contains(event)) {
            return this.getEventsAsLeader().remove(event);
        }
        return true;
    }

    public boolean removeEventAsMember(Event event) {
        if (this.getEventsAsMemeber().contains(event)) {
            boolean personChange = this.getEventsAsMemeber().remove(event);
            boolean eventChange = event.removeEventMember(this);
            return personChange && eventChange;
        }
        return true;
    }

}
