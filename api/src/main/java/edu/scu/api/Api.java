package edu.scu.api;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
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
    public ApiResponse<Event> proposeEvent(Event newEvent);

    public ApiResponse<EventMemberDetail> addEventMember(Event event, String memberEmail);

    public ApiResponse<Event> removeEventMember(Event event);

    public ApiResponse<Event> sendEventInvitation(Event event);

    public ApiResponse<Event> initiateEvent(Event event);

    public ApiResponse<Event> cancelEvent(Event event);

    public ApiResponse<Map<Person, Integer>> getAllEventMembersStatusAndEstimate(String eventId);

    public ApiResponse<List<Person>> getAllEventMembers(String eventId);

    public ApiResponse<EventLeaderDetail> proposeEventTimestampsAsLeader(EventLeaderDetail eventLeaderDetail);

    /**
     * As member
     */
    public ApiResponse<EventMemberDetail> proposeEventTimestampsAsMember(EventMemberDetail eventMemberDetail);

    public ApiResponse<EventMemberDetail> selectEventTimestampsAsMember(EventMemberDetail eventMemberDetail, List<MemberSelectedTimestamp> selectedEventTimestamps);

    public ApiResponse<Event> acceptEvent(String eventId, String memberId);

    public ApiResponse<EventMemberDetail> declineEvent(EventMemberDetail eventMemberDetail);

    public ApiResponse<EventMemberDetail> checkInEvent(EventMemberDetail eventMemberDetail);

    public ApiResponse<EventMemberDetail> setMinsToArriveAsMember(EventMemberDetail eventMemberDetail);

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

    /**
     * Sync
     */
    public ApiResponse<Person> syncHostInformation(String userId);

    /**
     * Messaging
     */
    public void registerEventChannelMessaging(String channelName);

    public void subscribeEventChannelAsLeader(String channelName, String leaderId, AsyncCallback<List<Message>> memberMsgResponder);

    public void subscribeEventChannelAsMember(String channelName, String memberId, AsyncCallback<List<Message>> leaderMsgResponder);

    public void publishEventChannelMessageAsLeader(String channelName, String leaderId, List<String> memberIds);

    public void publishEventChannelMessageAsMember(String channelName, String memberId, String leaderId);

    public void broadcastEventChannel(String channelName, String eventId, String leaderId, String eventManagementState);

}
