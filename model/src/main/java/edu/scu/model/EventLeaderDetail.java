package edu.scu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EventLeaderDetail implements Serializable {

    public static String SERIALIZE_KEY = "eventLeaderDetail";

    private Person leader;
    private Boolean isCheckedIn;
    private Integer minsToArrive;
    private List<LeaderProposedTimestamp> proposedTimestamps;
    //    private GeoPoint location;
    private String ownerId;
    private Date created;
    private Date updated;
    private String objectId;

    public EventLeaderDetail getBaseEventLeaderDetail() {
        EventLeaderDetail baseEventLeaderDetail = new EventLeaderDetail();
        baseEventLeaderDetail.setIsCheckedIn(isCheckedIn);
        baseEventLeaderDetail.setMinsToArrive(minsToArrive);
        baseEventLeaderDetail.setObjectId(objectId);
        baseEventLeaderDetail.setOwnerId(ownerId);
        baseEventLeaderDetail.setUpdated(updated);
        baseEventLeaderDetail.setCreated(created);
        return baseEventLeaderDetail;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    private boolean hasProposedTimestamp(LeaderProposedTimestamp timestamp) {
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
