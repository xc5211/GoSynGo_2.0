package edu.scu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EventMemberDetail implements Serializable {

    public static String SERIALIZE_KEY = "eventMemberDetail";

    private Person member;
    private Integer statusMember;
    private Boolean isCheckedIn;
    private Integer minsToArrive;
    private List<MemberSelectedTimestamp> selectedTimestamps;
    private List<MemberProposedTimestamp> proposedTimestamps;
    private String eventId;
    private String leaderId;
    //    private GeoPoint location;
    private String ownerId;
    private Date created;
    private Date updated;
    private String objectId;

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

    public Person getMember()
    {
        return member;
    }

    public void setMember( Person member )
    {
        this.member = member;
    }

    public Boolean getIsCheckedIn()
    {
        return isCheckedIn;
    }

    public void setIsCheckedIn( Boolean isCheckedIn )
    {
        this.isCheckedIn = isCheckedIn;
    }

    public Integer getStatusMember()
    {
        return statusMember;
    }

    public void setStatusMember( Integer statusMember )
    {
        this.statusMember = statusMember;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public List<MemberSelectedTimestamp> getSelectedTimestamps()
    {
        return selectedTimestamps;
    }

    public void setSelectedTimestamps( List<MemberSelectedTimestamp> selectedTimestamps )
    {
        this.selectedTimestamps = selectedTimestamps;
    }

    public List<MemberProposedTimestamp> getProposedTimestamps()
    {
        return proposedTimestamps;
    }

    public void setProposedTimestamps( List<MemberProposedTimestamp> proposedTimestamps )
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
//    public EventMemberDetail save()
//    {
//        return Backendless.Data.of( EventMemberDetail.class ).save( this );
//    }
//
//    public Long remove()
//    {
//        return Backendless.Data.of( EventMemberDetail.class ).remove( this );
//    }
//
//    public static EventMemberDetail findById( String id )
//    {
//        return Backendless.Data.of( EventMemberDetail.class ).findById( id );
//    }
//
//    public static EventMemberDetail findFirst()
//    {
//        return Backendless.Data.of( EventMemberDetail.class ).findFirst();
//    }
//
//    public static EventMemberDetail findLast()
//    {
//        return Backendless.Data.of( EventMemberDetail.class ).findLast();
//    }
//
//    public static void findLastAsync( AsyncCallback<EventMemberDetail> callback )
//    {
//        Backendless.Data.of( EventMemberDetail.class ).findLast( callback );
//    }
//
//    public static BackendlessCollection<EventMemberDetail> find( BackendlessDataQuery query )
//    {
//        return Backendless.Data.of( EventMemberDetail.class ).find( query );
//    }

    private boolean hasProposedTimestamp(MemberProposedTimestamp timestamp) {
        for (MemberProposedTimestamp proposedTimestamp : this.proposedTimestamps) {
            if (proposedTimestamp.getObjectId().equals(timestamp.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public boolean addProposedTimestamp(MemberProposedTimestamp timestamp) {
        if (!hasProposedTimestamp(timestamp)) {
            return this.proposedTimestamps.add(timestamp);
        }
        return false;
    }

    public boolean removeProposedTimestamp(MemberProposedTimestamp timestamp) {
        if (hasProposedTimestamp(timestamp)) {
            return this.proposedTimestamps.remove(timestamp);
        }
        return false;
    }

    public boolean updateProposedTimestamp(MemberProposedTimestamp timestamp) {
        if (!hasProposedTimestamp(timestamp)) {
            return false;
        }

        for (int i = 0; i < this.proposedTimestamps.size(); i++) {
            MemberProposedTimestamp currentProposedTimestamp = this.proposedTimestamps.get(i);
            if (currentProposedTimestamp.getObjectId().equals(timestamp.getObjectId())) {
                this.proposedTimestamps.set(i, currentProposedTimestamp);
                return true;
            }
        }
        assert false;
        return false;
    }

    private boolean hasSelectedTimestamp(MemberSelectedTimestamp timestamp) {
        for (MemberSelectedTimestamp selectedTimestamp : this.selectedTimestamps) {
            if (selectedTimestamp.getObjectId().equals(timestamp.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public boolean addSelectedTimestamp(MemberSelectedTimestamp timestamp) {
        if (!hasSelectedTimestamp(timestamp)) {
            return this.selectedTimestamps.add(timestamp);
        }
        return false;
    }

    public boolean removeSelectedTimestamp(MemberSelectedTimestamp timestamp) {
        if (hasSelectedTimestamp(timestamp)) {
            return this.selectedTimestamps.remove(timestamp);
        }
        return false;
    }

    public boolean updateSelectedTimestamp(MemberSelectedTimestamp timestamp) {
        if (!hasSelectedTimestamp(timestamp)) {
            return false;
        }

        for (int i = 0; i < this.selectedTimestamps.size(); i++) {
            MemberSelectedTimestamp currentSelectedTimestamp = this.selectedTimestamps.get(i);
            if (currentSelectedTimestamp.getObjectId().equals(timestamp.getObjectId())) {
                this.selectedTimestamps.set(i, currentSelectedTimestamp);
                return true;
            }
        }
        assert false;
        return false;
    }

}
