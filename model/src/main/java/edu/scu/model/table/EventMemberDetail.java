package edu.scu.model.table;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.Date;
import java.util.List;

public class EventMemberDetail
{
    private String ownerId;
    private Integer minsToArrive;
    private Date created;
    private Date updated;
    private String memberId;
    private Boolean isCheckedIn;
    private Integer statusMember;
    private String objectId;
    private List<MemberSelectedTimestamp> selectedTimestamps;
    private List<MemberProposedTimestamp> proposedTimestamps;
    private GeoPoint location;

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

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId( String memberId )
    {
        this.memberId = memberId;
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

    public GeoPoint getLocation()
    {
        return location;
    }

    public void setLocation( GeoPoint location )
    {
        this.location = location;
    }


    public EventMemberDetail save()
    {
        return Backendless.Data.of( EventMemberDetail.class ).save( this );
    }

    public Long remove()
    {
        return Backendless.Data.of( EventMemberDetail.class ).remove( this );
    }

    public static EventMemberDetail findById( String id )
    {
        return Backendless.Data.of( EventMemberDetail.class ).findById( id );
    }

    public static EventMemberDetail findFirst()
    {
        return Backendless.Data.of( EventMemberDetail.class ).findFirst();
    }

    public static EventMemberDetail findLast()
    {
        return Backendless.Data.of( EventMemberDetail.class ).findLast();
    }

    public static void findLastAsync( AsyncCallback<EventMemberDetail> callback )
    {
        Backendless.Data.of( EventMemberDetail.class ).findLast( callback );
    }

    public static BackendlessCollection<EventMemberDetail> find( BackendlessDataQuery query )
    {
        return Backendless.Data.of( EventMemberDetail.class ).find( query );
    }

}
