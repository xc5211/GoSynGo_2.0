package edu.scu.model;

import java.io.Serializable;
import java.util.Date;

public class MemberProposedTimestamp implements Serializable {

    public static String SERIALIZE_KEY = "memberProposedTimestamp";

    private Date timestamp;
    private Date created;
    private Date updated;
    private String objectId;
    private String ownerId;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

//    public MemberProposedTimestamp save() {
//        return Backendless.Data.of(MemberProposedTimestamp.class).save(this);
//    }
//
//    public Long remove() {
//        return Backendless.Data.of(MemberProposedTimestamp.class).remove(this);
//    }
//
//    public static MemberProposedTimestamp findById(String id) {
//        return Backendless.Data.of(MemberProposedTimestamp.class).findById(id);
//    }
//
//    public static MemberProposedTimestamp findFirst() {
//        return Backendless.Data.of(MemberProposedTimestamp.class).findFirst();
//    }
//
//    public static MemberProposedTimestamp findLast() {
//        return Backendless.Data.of(MemberProposedTimestamp.class).findLast();
//    }
//
//    public static BackendlessCollection<MemberProposedTimestamp> find(BackendlessDataQuery query) {
//        return Backendless.Data.of(MemberProposedTimestamp.class).find(query);
//    }

}