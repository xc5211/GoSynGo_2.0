package edu.scu.api;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import edu.scu.model.Event;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;

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
    public ApiResponse<Person> register(String userEmail, String password, String name);
    // Boolean: True/False
    public ApiResponse<String> login(String userEmail, String password, boolean stayLoggedIn);
    //Boolean:True/False
    public ApiResponse<Void> logout();
    //Boolean:True/False
    public ApiResponse<Boolean> isGoogleCalendarImported(String personId);
    //
    public ApiResponse<Void> importGoogleCalendar(String personId);

    public ApiResponse<List<Date>> getMonthlyScheduledDates(String personId);

    public ApiResponse<List<Event>> getEventsAsLeader(String personId);

    public ApiResponse<List<Event>> getEventsAsMember(String personId);

    /**
     * As leader
     */
    // Returns a newly created empty Event object
    public ApiResponse<Event> proposeEvent(String leaderId);

    public ApiResponse<Event> addEventMember(String leaderId, String eventId, String memberEmail);

    public ApiResponse<Event> removeEventMember(String leaderId, String eventId, String memberId);

    public ApiResponse<Event> sendEventInvitation(String leaderId, String title, int durationInMin, boolean hasReminder, int reminderInMin, Text location, List<Timestamp> proposedTimestamps);

    public ApiResponse<Event> initiateEvent(String leaderId, String eventId);

    public ApiResponse<Void> cancelEvent(String leaderId, String eventId);

    public ApiResponse<Integer> getAllEventMembersStatusAndEstimate(String leaderId, String eventId);

    public ApiResponse<List<Person>> getAllEventMembers(String eventId);

    public ApiResponse<Event> proposeEventTimestampsAsLeader(String eventId, List<Timestamp> proposedEventTimestamps);

    /**
     * As member
     */
    public ApiResponse<Event> proposeEventTimestampsAsMember(String memberId, String eventId, List<Timestamp> proposedEventTimestamps);

    public ApiResponse<Event> selectEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps);

    public ApiResponse<Event> acceptEvent(String memberId, String eventId);

    public ApiResponse<Event> declineEvent(String memberId, String eventId);

    public ApiResponse<Event> checkInEvent(String memberId, String eventId);

    public ApiResponse<Event> setMinsToArriveAsMember(String memberId, String eventId, int estimateInMin);

    /**
     * Event - Shared to both leader and member
     */
    public ApiResponse<Integer> getEventStatus(String eventId);

    public ApiResponse<Person> getEventLeader(String eventId);

    public ApiResponse<String> getEventLocation(String eventId);

    public ApiResponse<Integer> getEventDurationInMin(String eventId);

}
