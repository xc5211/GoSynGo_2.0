package edu.scu.api;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.List;

import edu.scu.model.Event;

/**
 * Created by chuanxu on 4/13/16.
 */
public interface Api {

    // no response, event/message,can be used for more than one api: ApiResponseVoid
    // return Boolean,ApiResponseBoolean
    // ApiResponseRegister
    // ApiResponseLogin


    // As user
    // String: userId
    public ApiResponse<String> register(String userEmail, String password, String name);
    // Boolean: True/False
    public ApiResponse<String> login(String userEmail, String password, boolean stayLoggedIn);
    //Boolean:True/False
    public ApiResponse<Void> logout();
    //Boolean:True/False
    public ApiResponse<Void> isGoogleCalendarImported(String userId);
    //
    public ApiResponse<Void> importGoogleCalendar(String userId);
    //List<T>:MonthlyScheduledDatesLists
    public ApiResponse<Void> getMonthlyScheduledDates(String userId);
    //List<T>:EventAsLeaderLists
    public ApiResponse<List<Event>> getEventsAsLeader(String userId);
    //List<T>:EventAsMemberLists
    public ApiResponse<List<Event>> getEventsAsMember(String userId);

    // As leader
    //String:eventId, List<T>: leaderAvailableTimeStampList, List<T>
    public ApiResponse<String> proposeEvent(String leaderId);
    //String:memberName, T: memberImage, List<T>:availableTimestampsList
    public ApiResponse<Void> addEventMember(String leaderId, String eventId, String memberEmail);
    //Boolean:True/False
    public ApiResponse<Void> removeEventMember(String leaderId, String eventId, String memberEmail);
    //Boolean: True/Flase
    public ApiResponse<Void> sendEventInvitation(String leaderId, String name, int durationInMin, boolean hasReminder, int reminderInMin, Text location, List<Timestamp> proposedTimestamps);
    //confirm and start the event
    public ApiResponse<Void> initiateEvent(String leaderId, String eventId);
    //Boolean:True/False
    public ApiResponse<Void> cancelEvent(String leaderId, String eventId);
    //List<T>:evenMemberStatus, List<E>:estimateList
    public ApiResponse<Void> getAllEventMembersStatusAndEstimate(String leaderId, String eventId);
    //List<T>: eventMemberList
    public ApiResponse<Void> getAllEventMembers(String leaderId, String eventId);

    // As member
    //Boolean:True/False
    public ApiResponse<Void> proposeEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps);
    //Boolean:True/False
    public ApiResponse<Void> selectEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps);
    //List<T>:selectEventList
    public ApiResponse<Void> acceptEvent(String memberId, String eventId);
    //Boolean:True/False
    public ApiResponse<Void> declineEvent(String memberId, String eventId);
    //Boolean:True/False,String: estimateTimestamp
    public ApiResponse<Void> checkInEvent(String memberId, String eventId);
    //(estimate arriving time) Boolean:True/False
    public ApiResponse<Void> estimateEvent(String memberId, String eventId, int estimateInMin);

    // Event - Shared to both leader and member
    //String:eventStatus
    public ApiResponse<Void> getEventStatus(String userId, String eventId);
    //String:leaderName, T:leaderImage
    public ApiResponse<Void> getEventLeader(String userId, String eventId);
    //String:eventLocation
    public ApiResponse<Void> getEventLocation(String userId, String eventId);
    //String: eventDuration
    public ApiResponse<Void> getEventDurationInMin(String userId, String eventId);

}
