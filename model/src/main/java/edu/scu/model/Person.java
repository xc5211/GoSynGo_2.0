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
    private List<Person> contacts;

    public Person() {
        //this.eventsAsLeader = new ArrayList<>();
        //this.eventsAsMemeber = new ArrayList<>();
        //this.contacts = new ArrayList<>();
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

    public List<Person> getContacts() {
        return this.contacts;
    }

    public List<Event> getLeaderEventsAsPending() {
        List<Event> pendingEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus() == StatusEvent.Pending.getStatus()) {
                pendingEvents.add(event);
            }
        }
        return pendingEvents;
    }

    public List<Event> getLeaderEventsAsReady() {
        List<Event> readyEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus() == StatusEvent.Ready.getStatus()) {
                readyEvents.add(event);
            }
        }
        return readyEvents;
    }

    public List<Event> getLeaderEventsAsCancelled() {
        List<Event> declinedEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus() == StatusEvent.Cancelled.getStatus()) {
                declinedEvents.add(event);
            }
        }
        return declinedEvents;
    }

    public List<Event> getLeaderEventsAsTentative() {
        List<Event> tentativeEvents = new ArrayList<>();
        for (Event event : this.getEventsAsLeader()) {
            if (event.getStatus() == StatusEvent.Tentative.getStatus()) {
                tentativeEvents.add(event);
            }
        }
        return tentativeEvents;
    }

    public List<Event> getMemberEventsAsPending() {
        List<Event> pendingEvents = new ArrayList<>();
        for (Event event : this.getEventsAsMemeber()) {
            if (event.getStatus() == StatusEvent.Pending.getStatus()) {
                pendingEvents.add(event);
            }
        }
        return pendingEvents;
    }

    public List<Event> getMemberEventsAsReady() {
        List<Event> readyEvents = new ArrayList<>();
        for (Event event : this.getEventsAsMemeber()) {
            if (event.getStatus() == StatusEvent.Ready.getStatus()) {
                readyEvents.add(event);
            }
        }
        return readyEvents;
    }

    public List<Event> getMemberEventsAsCancelled() {
        List<Event> declinedEvents = new ArrayList<>();
        for (Event event : this.getEventsAsMemeber()) {
            if (event.getStatus() == StatusEvent.Cancelled.getStatus()) {
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

    public void setEventsAsLeader(List<Event> eventsAsLeader) {
        this.eventsAsLeader = eventsAsLeader;
    }

    public void setEventsAsMemeber(List<Event> eventsAsMemeber) {
        this.eventsAsMemeber = eventsAsMemeber;
    }

    public void setContacts(List<Person> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Person contact) {
        this.contacts.add(contact);
    }

    public boolean removeContact(Person person) {
        if (!this.contacts.contains(person)) {
            this.contacts.remove(this.contacts.indexOf(person));
        }
        return true;
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
