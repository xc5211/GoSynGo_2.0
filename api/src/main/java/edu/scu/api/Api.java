package edu.scu.api;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by chuanxu on 4/13/16.
 */
public interface Api {

    // As user
    public ApiResponse<String> register(String userEmail, String password, String name);
    public ApiResponse<Void> login(String userEmail, String password, boolean stayLoggedIn);
    public ApiResponse<Void> isGoogleCalendarImported(String userId);
    public ApiResponse<Void> importGoogleCalendar(String userId);
    public ApiResponse<Void> getMonthlyScheduledDates(String userId);
    public ApiResponse<Void> getEventsAsLeader(String userId);
    public ApiResponse<Void> getEventsAsMember(String userId);

    // As leader
    public ApiResponse<Void> proposeEvent(String leaderId);
    public ApiResponse<Void> addEventMember(String leaderId, String eventId, String memberEmail);
    public ApiResponse<Void> removeEventMember(String leaderId, String eventId, String memberEmail);
    public ApiResponse<Void> sendEventInvitation(String leaderId, String name, int durationInMin, boolean hasReminder, int reminderInMin, Text location, List<Timestamp> proposedTimestamps);
    public ApiResponse<Void> initiateEvent(String leaderId, String eventId);
    public ApiResponse<Void> cancelEvent(String leaderId, String eventId);
    public ApiResponse<Void> getAllEventMembersStatusAndEstimate(String leaderId, String eventId);
    public ApiResponse<Void> getAllEventMembers(String leaderId, String eventId);

    // As member
    public ApiResponse<Void> proposeEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps);
    public ApiResponse<Void> selectEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps);
    public ApiResponse<Void> acceptEvent(String memberId, String eventId);
    public ApiResponse<Void> declineEvent(String memberId, String eventId);
    public ApiResponse<Void> checkInEvent(String memberId, String eventId);
    public ApiResponse<Void> estimateEvent(String memberId, String eventId, int estimateInMin);

    // Event - Shared to both leader and member
    public ApiResponse<Void> getEventStatus(String userId, String eventId);
    public ApiResponse<Void> getEventLeader(String userId, String eventId);
    public ApiResponse<Void> getEventLocation(String userId, String eventId);
    public ApiResponse<Void> getEventDurationInMin(String userId, String eventId);

}
