package edu.scu.model.query;

/**
 * Created by chuanxu on 5/3/16.
 */
public class BackendlessQueryHelper {

    public static String queryEventMemberDetail(String eventId, String memberId) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("member");
        whereClause.append(".objectId = '").append(memberId).append("'");
        whereClause.append(" and ");
        whereClause.append("member");
        whereClause.append(".eventsAsMember");
        whereClause.append(".objectId = '").append(eventId).append("'");
        return whereClause.toString();
    }

    public static String queryEventMemberDetails(String eventId) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("member");
        whereClause.append(".eventsAsMember");
        whereClause.append(".objectId = '").append(eventId).append("'");
        return whereClause.toString();
    }

    public static String queryEventsAsMember(String memberId) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventMemberDetail");
        whereClause.append(".member");
        whereClause.append(".objectId = '").append(memberId).append("'");
        return whereClause.toString();
    }

    public static String queryEventsAsLeader(String leaderId) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventLeaderDetail");
        whereClause.append(".leader");
        whereClause.append(".objectId = '").append(leaderId).append("'");
        return whereClause.toString();
    }

    public static String queryPerson(String email) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("email = '").append(email).append("'");
        return whereClause.toString();
    }

    public static String queryEventMembers(String eventId) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventsAsMember");
        whereClause.append(".objectId = '").append(eventId).append("'");
        return whereClause.toString();
    }

}
