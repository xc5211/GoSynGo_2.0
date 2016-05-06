package edu.scu.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/13/16.
 */
public interface Api {

    // no response, event/message,can be used for more than one api: ApiResponseVoid
    // return Boolean,ApiResponseBoolean
    // ApiResponseRegister
    // ApiResponseLogin

    /**
     * As user
      */
    // String: userId
    public ApiResponse<Person> register(String userEmail, String password, String firstName, String lastName);
    // Boolean: True/False
    public ApiResponse<String> login(String userEmail, String password, boolean stayLoggedIn);
    //Boolean:True/False
    public ApiResponse<Void> logout();
    //Boolean:True/False
    public ApiResponse<Boolean> isGoogleCalendarImported(String personId);
    //
    public ApiResponse<Void> importGoogleCalendar(String personId);

    public ApiResponse<List<Date>> getScheduledDates(String personId);

    public ApiResponse<List<Event>> getEventsAsLeader(String personId);

    public ApiResponse<List<Event>> getEventsAsMember(String personId);

    /**
     * As leader
     */
    // Returns a newly created empty Event object
    public ApiResponse<Event> proposeEvent(String leaderId);

    public ApiResponse<Person> addEventMember(String eventId, String memberEmail);

    public ApiResponse<Event> removeEventMember(String eventId, String memberId);

    public ApiResponse<Event> sendEventInvitation(String eventId, String title, String location, int durationInMin, boolean hasReminder, int reminderInMin, List<LeaderProposedTimestamp> proposedTimestamps);

    public ApiResponse<Event> initiateEvent(String eventId, Date eventTimestamp);

    public ApiResponse<Event> cancelEvent(String eventId);

    public ApiResponse<Map<Person, Integer>> getAllEventMembersStatusAndEstimate(String eventId);

    public ApiResponse<List<Person>> getAllEventMembers(String eventId);

    public ApiResponse<EventLeaderDetail> proposeEventTimestampsAsLeader(String leaderId, String eventId, List<LeaderProposedTimestamp> proposedEventTimestamps);

    /**
     * As member
     */
    public ApiResponse<EventMemberDetail> proposeEventTimestampsAsMember(String memberId, String eventId, List<MemberProposedTimestamp> proposedEventTimestamps);

    public ApiResponse<EventMemberDetail> selectEventTimestamps(String memberId, String eventId, List<MemberSelectedTimestamp> selectedEventTimestamps);

    public ApiResponse<EventMemberDetail> acceptEvent(String memberId, String eventId);

    public ApiResponse<EventMemberDetail> declineEvent(String memberId, String eventId);

    public ApiResponse<EventMemberDetail> checkInEvent(String memberId, String eventId);

    public ApiResponse<EventMemberDetail> setMinsToArriveAsMember(String memberId, String eventId, int estimateInMin);

    /**
     * Event - Shared to both leader and member
     */
    public ApiResponse<Integer> getEventStatus(String eventId);

    public ApiResponse<Person> getEventLeader(String eventId);

    public ApiResponse<String> getEventLocation(String eventId);

    public ApiResponse<Integer> getEventDurationInMin(String eventId);

    /**
     * Device
     */
    public ApiResponse<Void> registerDevice();

    public ApiResponse<Void> unregisterDevice();

}
