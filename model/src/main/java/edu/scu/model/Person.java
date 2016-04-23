package edu.scu.model;

import java.util.Date;
import java.util.List;

public class Person
{
    private String email;
    private String name;
    private String objectId;
    private java.util.Date created;
    private java.util.Date updated;
    private String ownerId;
    private Boolean isGoogleCalendarImported;
    private java.util.List<Event> eventsAsLeader;
    private java.util.List<Event> eventsAsMember;
    private java.util.List<Person> contacts;
    private String userId;

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

    public String getObjectId()
    {
        return objectId;
    }

    public Date getCreated()
    {
        return created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public String getOwnerId()
    {
        return ownerId;
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

    public List<Person> getContacts()
    {
        return contacts;
    }

    public void setContacts( List<Person> contacts )
    {
        this.contacts = contacts;
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

}