package edu.scu.model;

import java.io.Serializable;
import java.util.Date;

public class LeaderProposedTimestamp implements Serializable {

    private String leaderId;
    private String eventId;
    private Date timestamp;
    private Date created;
    private Date updated;
    private String ownerId;
    private String objectId;

    public Date getCreated()
    {
        return created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( Date timestamp )
    {
        this.timestamp = timestamp;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

//    public LeaderProposedTimestamp save()
//    {
//        return Backendless.Data.of( LeaderProposedTimestamp.class ).save( this );
//    }
//
//    public Long remove()
//    {
//        return Backendless.Data.of( LeaderProposedTimestamp.class ).remove( this );
//    }
//
//    public static LeaderProposedTimestamp findById( String id )
//    {
//        return Backendless.Data.of( LeaderProposedTimestamp.class ).findById( id );
//    }
//
//    public static LeaderProposedTimestamp findFirst()
//    {
//        return Backendless.Data.of( LeaderProposedTimestamp.class ).findFirst();
//    }
//
//    public static LeaderProposedTimestamp findLast()
//    {
//        return Backendless.Data.of( LeaderProposedTimestamp.class ).findLast();
//    }
//
//    public static BackendlessCollection<LeaderProposedTimestamp> find( BackendlessDataQuery query )
//    {
//        return Backendless.Data.of( LeaderProposedTimestamp.class ).find( query );
//    }

}
