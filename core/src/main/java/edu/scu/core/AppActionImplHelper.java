package edu.scu.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/18/16.
 */
public class AppActionImplHelper {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    public static List<LeaderProposedTimestamp> proposeEventTimestampsAsLeader(String eventId, String leaderId, List<String> datesInString) {

        List<LeaderProposedTimestamp> leaderProposedTimestamps = new ArrayList<>();
        try {
            Date date;
            for (String dateInString : datesInString) {
                date = sdf.parse(dateInString);
                LeaderProposedTimestamp leaderProposedTimestamp = new LeaderProposedTimestamp();
                leaderProposedTimestamp.setEventId(eventId);
                leaderProposedTimestamp.setLeaderId(leaderId);
                leaderProposedTimestamp.setTimestamp(date);
                leaderProposedTimestamps.add(leaderProposedTimestamp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return leaderProposedTimestamps;
    }

    public static List<MemberProposedTimestamp> proposeEventTimestampsAsMember(String eventId, String leaderId, List<String> datesInString) {

        List<MemberProposedTimestamp> memberProposedTimestamps = new ArrayList<>();
        try {
            Date date;
            for (String dateInString : datesInString) {
                date = sdf.parse(dateInString);
                MemberProposedTimestamp memberProposedTimestamp = new MemberProposedTimestamp();
                memberProposedTimestamp.setTimestamp(date);
                memberProposedTimestamps.add(memberProposedTimestamp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return memberProposedTimestamps;
    }

    public static List<MemberSelectedTimestamp> selectEventTimestampsAsMember(String eventId, String leaderId, List<String> datesInString) {

        List<MemberSelectedTimestamp> memberSelectedTimestamps = new ArrayList<>();
        try {
            Date date;
            for (String dateInString : datesInString) {
                date = sdf.parse(dateInString);
                MemberSelectedTimestamp memberSelectedTimestamp = new MemberSelectedTimestamp();
                memberSelectedTimestamp.setTimestamp(date);
                memberSelectedTimestamps.add(memberSelectedTimestamp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return memberSelectedTimestamps;
    }



    public static Person getBasePerson(Person hostPerson) {
        Person basePerson = new Person();
        basePerson.setUserId(hostPerson.getUserId());
        basePerson.setEmail(hostPerson.getEmail());
        basePerson.setName(hostPerson.getName());
        basePerson.setFirstName(hostPerson.getFirstName());
        basePerson.setLastName(hostPerson.getLastName());
        basePerson.setIsGoogleCalendarImported(hostPerson.getIsGoogleCalendarImported());
        basePerson.setObjectId(hostPerson.getObjectId());
        basePerson.setCreated(hostPerson.getCreated());
        basePerson.setUpdated(hostPerson.getUpdated());
        basePerson.setOwnerId(hostPerson.getOwnerId());
        basePerson.setContacts(new ArrayList<Person>());
        basePerson.setEventsAsLeader(new ArrayList<Event>());
        basePerson.setEventsAsMember(new ArrayList<Event>());
        return basePerson;
    }

    public static Event getBaseEvent(Event event) {
        Event baseEvent = new Event();
        baseEvent.setTitle(event.getTitle());
        baseEvent.setLocation(event.getLocation());
        baseEvent.setNote(event.getNote());
        baseEvent.setDurationInMin(event.getDurationInMin());
        baseEvent.setStatusEvent(event.getStatusEvent());
        baseEvent.setHasReminder(event.getHasReminder());
        baseEvent.setTimestamp(event.getTimestamp());
        baseEvent.setObjectId(event.getObjectId());
        baseEvent.setOwnerId(event.getOwnerId());
        baseEvent.setUpdated(event.getUpdated());
        baseEvent.setCreated(event.getCreated());
        baseEvent.setEventMemberDetail(new ArrayList<EventMemberDetail>());
        return baseEvent;
    }

    public static EventLeaderDetail getBaseEventLeaderDetail(EventLeaderDetail eventLeaderDetail) {
        EventLeaderDetail baseEventLeaderDetail = new EventLeaderDetail();
        baseEventLeaderDetail.setIsCheckedIn(eventLeaderDetail.getIsCheckedIn());
        baseEventLeaderDetail.setMinsToArrive(eventLeaderDetail.getMinsToArrive());
        baseEventLeaderDetail.setObjectId(eventLeaderDetail.getObjectId());
        baseEventLeaderDetail.setOwnerId(eventLeaderDetail.getOwnerId());
        baseEventLeaderDetail.setUpdated(eventLeaderDetail.getUpdated());
        baseEventLeaderDetail.setCreated(eventLeaderDetail.getCreated());
        baseEventLeaderDetail.setProposedTimestamps(new ArrayList<LeaderProposedTimestamp>());
        return baseEventLeaderDetail;
    }

    public static EventMemberDetail getBaseEventMemberDetail(EventMemberDetail eventMemberDetail) {
        EventMemberDetail baseEventMemberDetail = new EventMemberDetail();
        baseEventMemberDetail.setStatusMember(eventMemberDetail.getStatusMember());
        baseEventMemberDetail.setIsCheckedIn(eventMemberDetail.getIsCheckedIn());
        baseEventMemberDetail.setMinsToArrive(eventMemberDetail.getMinsToArrive());
        baseEventMemberDetail.setEventId(eventMemberDetail.getObjectId());
        baseEventMemberDetail.setLeaderId(eventMemberDetail.getLeaderId());
        baseEventMemberDetail.setObjectId(eventMemberDetail.getObjectId());
        baseEventMemberDetail.setOwnerId(eventMemberDetail.getOwnerId());
        baseEventMemberDetail.setUpdated(eventMemberDetail.getUpdated());
        baseEventMemberDetail.setCreated(eventMemberDetail.getCreated());
        baseEventMemberDetail.setProposedTimestamps(new ArrayList<MemberProposedTimestamp>());
        baseEventMemberDetail.setSelectedTimestamps(new ArrayList<MemberSelectedTimestamp>());
        return baseEventMemberDetail;
    }

}
