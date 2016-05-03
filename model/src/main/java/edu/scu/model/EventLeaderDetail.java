package edu.scu.model;

import java.util.Date;
import java.util.List;

public class EventLeaderDetail
{
    private String ownerId;
    private Integer minsToArrive;
    private Date created;
    private Date updated;
    private Person leader;
    private Boolean isCheckedIn;
    private String objectId;
    private List<LeaderProposedTimestamp> proposedTimestamps;
//    private GeoPoint location;

    public String getOwnerId()
    {
        return ownerId;
    }

    public Integer getMinsToArrive()
    {
        return minsToArrive;
    }

    public void setMinsToArrive( Integer minsToArrive )
    {
        this.minsToArrive = minsToArrive;
    }

    public Date getCreated()
    {
        return created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public Person getLeader()
    {
        return leader;
    }

    public void setLeader( Person leader )
    {
        this.leader = leader;
    }

    public Boolean getIsCheckedIn()
    {
        return isCheckedIn;
    }

    public void setIsCheckedIn( Boolean isCheckedIn )
    {
        this.isCheckedIn = isCheckedIn;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public List<LeaderProposedTimestamp> getProposedTimestamps()
    {
        return proposedTimestamps;
    }

    public void setProposedTimestamps( List<LeaderProposedTimestamp> proposedTimestamps )
    {
        this.proposedTimestamps = proposedTimestamps;
    }

//    public GeoPoint getLocation()
//    {
//        return location;
//    }
//
//    public void setLocation( GeoPoint location )
//    {
//        this.location = location;
//    }
//
//
//    public EventLeaderDetail save()
//    {
//        return Backendless.Data.of( EventLeaderDetail.class ).save( this );
//    }
//
//    public Long remove()
//    {
//        return Backendless.Data.of( EventLeaderDetail.class ).remove( this );
//    }
//
//    public static EventLeaderDetail findById( String id )
//    {
//        return Backendless.Data.of( EventLeaderDetail.class ).findById( id );
//    }
//
//    public static EventLeaderDetail findFirst()
//    {
//        return Backendless.Data.of( EventLeaderDetail.class ).findFirst();
//    }
//
//    public static EventLeaderDetail findLast()
//    {
//        return Backendless.Data.of( EventLeaderDetail.class ).findLast();
//    }
//
//    public static void findLastAsync( AsyncCallback<EventLeaderDetail> callback )
//    {
//        Backendless.Data.of( EventLeaderDetail.class ).findLast( callback );
//    }
//
//    public static BackendlessCollection<EventLeaderDetail> find( BackendlessDataQuery query )
//    {
//        return Backendless.Data.of( EventLeaderDetail.class ).find( query );
//    }

}
