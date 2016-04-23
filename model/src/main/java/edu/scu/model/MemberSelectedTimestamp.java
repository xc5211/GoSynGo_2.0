package edu.scu.model;

import java.util.Date;

public class MemberSelectedTimestamp
{
    private Date updated;
    private Date timestamp;
    private Date created;
    private String objectId;
    private String ownerId;

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

    public Date getCreated()
    {
        return created;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

//    public MemberSelectedTimestamp save()
//    {
//        return Backendless.Data.of( MemberSelectedTimestamp.class ).save( this );
//    }
//
//    public Long remove()
//    {
//        return Backendless.Data.of( MemberSelectedTimestamp.class ).remove( this );
//    }
//
//    public static MemberSelectedTimestamp findById( String id )
//    {
//        return Backendless.Data.of( MemberSelectedTimestamp.class ).findById( id );
//    }
//
//    public static MemberSelectedTimestamp findFirst()
//    {
//        return Backendless.Data.of( MemberSelectedTimestamp.class ).findFirst();
//    }
//
//    public static MemberSelectedTimestamp findLast()
//    {
//        return Backendless.Data.of( MemberSelectedTimestamp.class ).findLast();
//    }
//
//    public static BackendlessCollection<MemberSelectedTimestamp> find( BackendlessDataQuery query )
//    {
//        return Backendless.Data.of( MemberSelectedTimestamp.class ).find( query );
//    }

}