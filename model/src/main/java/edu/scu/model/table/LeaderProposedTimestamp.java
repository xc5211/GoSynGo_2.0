package edu.scu.model.table;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.Date;

public class LeaderProposedTimestamp
{
    private Date created;
    private Date updated;
    private Date timestamp;
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

    public LeaderProposedTimestamp save()
    {
        return Backendless.Data.of( LeaderProposedTimestamp.class ).save( this );
    }

    public Long remove()
    {
        return Backendless.Data.of( LeaderProposedTimestamp.class ).remove( this );
    }

    public static LeaderProposedTimestamp findById( String id )
    {
        return Backendless.Data.of( LeaderProposedTimestamp.class ).findById( id );
    }

    public static LeaderProposedTimestamp findFirst()
    {
        return Backendless.Data.of( LeaderProposedTimestamp.class ).findFirst();
    }

    public static LeaderProposedTimestamp findLast()
    {
        return Backendless.Data.of( LeaderProposedTimestamp.class ).findLast();
    }

    public static BackendlessCollection<LeaderProposedTimestamp> find( BackendlessDataQuery query )
    {
        return Backendless.Data.of( LeaderProposedTimestamp.class ).find( query );
    }

}