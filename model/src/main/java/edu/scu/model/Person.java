package edu.scu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Person implements Serializable {

    public final static String SERIALIZE_KEY = "person";

    private String userId;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private Boolean isGoogleCalendarImported;
    private List<Event> eventsAsLeader;
    private List<Event> eventsAsMember;
    private List<EventUndecided> eventsUndecided;
    private List<Person> contacts;
    private String objectId;
    private java.util.Date created;
    private java.util.Date updated;
    private String ownerId;

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId(String objectId) {
       this.objectId = objectId;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getIsGoogleCalendarImported()
    {
        return isGoogleCalendarImported;
    }

    public void setIsGoogleCalendarImported( Boolean isGoogleCalendarImported )
    {
        this.isGoogleCalendarImported = isGoogleCalendarImported;
    }

    public List<Event> getEventsAsLeader()
    {
        return eventsAsLeader;
    }

    public void setEventsAsLeader( List<Event> eventsAsLeader )
    {
        this.eventsAsLeader = eventsAsLeader;
    }

    public List<Event> getEventsAsMember()
    {
        return eventsAsMember;
    }

    public void setEventsAsMember( List<Event> eventsAsMember )
    {
        this.eventsAsMember = eventsAsMember;
    }

    public List<EventUndecided> getEventsUndecided() {
        return eventsUndecided;
    }

    public void setEventsUndecided(List<EventUndecided> eventsUndecided) {
        this.eventsUndecided = eventsUndecided;
    }

    public String getUserId() {
        return userId;
    }

    public List<Person> getContacts()
    {
        return contacts;
    }

    public void setContacts( List<Person> contacts )
    {
        this.contacts = contacts;
    }

    public void setUserId( String userId ) {
        this.userId = userId;
    }

//    public BackendlessUser getUser()
//    {
//        return user;
//    }
//
//    public void setUser( BackendlessUser user )
//    {
//        this.user = user;
//    }
//
//    public Person save()
//    {
//        return Backendless.Data.of( Person.class ).save( this );
//    }
//
//    public Long remove()
//    {
//        return Backendless.Data.of( Person.class ).remove( this );
//    }
//
//    public static Person findById( String id )
//    {
//        return Backendless.Data.of( Person.class ).findById( id );
//    }
//
//    public static Person findFirst()
//    {
//        return Backendless.Data.of( Person.class ).findFirst();
//    }
//
//    public static Person findLast()
//    {
//        return Backendless.Data.of( Person.class ).findLast();
//    }
//
//    public static BackendlessCollection<Person> find( BackendlessDataQuery query )
//    {
//        return Backendless.Data.of( Person.class ).find( query );
//    }

    private boolean hasEventAsLeader(Event event) {
        for (Event eventAsLeader : this.eventsAsLeader) {
            if (eventAsLeader.getObjectId().equals(event.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public boolean addEventAsLeader(Event event) {
        if (!hasEventAsLeader(event)) {
            return this.eventsAsLeader.add(event);
        }
        return true;
    }

    public boolean removeEventAsLeader(Event event) {
        if (hasEventAsLeader(event)) {
            return this.eventsAsLeader.remove(event);
        }
        return false;
    }

    public boolean updateEventAsLeader(Event event) {
        if (!hasEventAsLeader(event)) {
            return false;
        }

        for (int i = 0; i < this.eventsAsLeader.size(); i++) {
            Event currentEvent = this.eventsAsLeader.get(i);
            if (currentEvent.getObjectId().equals(event.getObjectId())) {
                this.eventsAsLeader.set(i, event);
                return true;
            }
        }
        assert false;
        return false;
    }

    private boolean hasEventAsMember(Event event) {
        for (Event eventAsMember : this.eventsAsMember) {
            if (eventAsMember.getObjectId().equals(event.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasEventUndecided(EventUndecided event) {
        for (EventUndecided eventUndecided : this.eventsUndecided) {
            if (eventUndecided.eventId.equals(event.eventId)) {
                return true;
            }
        }
        return false;
    }

    public boolean addEventAsMember(Event event) {
        if (!hasEventAsMember(event)) {
            return this.eventsAsMember.add(event);
        }
        return false;
    }

    public boolean removeEventAsMember(Event event) {
        if (hasEventAsMember(event)) {
            return this.eventsAsMember.remove(event);
        }
        return false;
    }

    public boolean addEventUndecided(EventUndecided event) {
        if (!hasEventUndecided(event)) {
            return this.eventsUndecided.add(event);
        }
        return false;
    }

    public boolean removeEventUndecided(EventUndecided event) {
        if (hasEventUndecided(event)) {
            return this.eventsUndecided.remove(event);
        }
        return false;
    }

    public boolean updateEventAsMember(Event event) {
        if (!hasEventAsMember(event)) {
            return false;
        }

        for (int i = 0; i < this.eventsAsMember.size(); i++) {
            Event currentEvent = this.eventsAsMember.get(i);
            if (currentEvent.getObjectId().equals(event.getObjectId())) {
                this.eventsAsMember.set(i, event);
                return true;
            }
        }
        assert false;
        return false;
    }

    private boolean hasContact(Person person) {
        for (Person contact : this.contacts) {
            if (contact.getObjectId().equals(person.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public boolean addContact(Person person) {
        if (!hasContact(person)) {
            return this.contacts.add(person);
        }
        return false;
    }

    public boolean removeContact(Person person) {
        if (hasContact(person)) {
            return this.contacts.remove(person);
        }
        return false;
    }

    public boolean updateContact(Person person) {
        if (!hasContact(person)) {
            return false;
        }

        for (int i = 0; i < this.contacts.size(); i++) {
            if (this.contacts.get(i).getObjectId().equals(person.getObjectId())) {
                this.contacts.set(i, person);
                return true;
            }
        }
        assert false;
        return false;
    }

    public EventMemberDetail getEventMemberMemberDetail(String eventId, String memberId) {
        Event targetEvent = null;
        for (Event event : eventsAsLeader) {
            if (event.getObjectId().equals(eventId)) {
                targetEvent = event;
                break;
            }
        }

        for (EventMemberDetail memberDetail : targetEvent.getEventMemberDetail()) {
            if (memberDetail.getMember().getObjectId().equals(memberId)) {
                return memberDetail;
            }
        }
        return null;
    }

    public Event getEventAsLeader(String eventId) {
        for (Event eventAsLeader : this.eventsAsLeader) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                return eventAsLeader;
            }
        }
        return null;
    }

    public Event getEventAsMember(String eventId) {
        for (Event eventAsMember : this.eventsAsMember) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                return eventAsMember;
            }
        }
        assert false;
        return null;
    }

    public EventUndecided getEventUndecided(String eventId) {
        for (EventUndecided eventUndecided : this.eventsUndecided) {
            if (eventUndecided.eventId.equals(eventId)) {
                return eventUndecided;
            }
        }
        assert false;
        return null;
    }

}
