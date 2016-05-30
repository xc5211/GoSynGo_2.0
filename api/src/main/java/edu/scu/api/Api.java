package edu.scu.api;

import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

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

    public ApiResponse<Void> importGoogleCalendar(String personId);

    /**
     * As leader
     */
    public ApiResponse<Person> proposeEvent(Person person);

    public ApiResponse<Event> addEventMember(Event event, String leaderId, String memberEmail);

    public ApiResponse<Event> removeEventMember(Event event);

    public ApiResponse<Event> sendEventInvitation(Event event);

    public ApiResponse<Event> initiateEvent(Event event);

    public ApiResponse<Event> cancelEvent(Event event);

    public ApiResponse<EventLeaderDetail> proposeEventTimestampsAsLeader(EventLeaderDetail eventLeaderDetail);

    /**
     * As member
     */
    public ApiResponse<EventMemberDetail> proposeEventTimestampsAsMember(EventMemberDetail eventMemberDetail);

    public ApiResponse<EventMemberDetail> selectEventTimestampsAsMember(EventMemberDetail eventMemberDetail);

    public ApiResponse<Person> acceptEvent(Person member, String eventId, String memberId);

    public ApiResponse<EventMemberDetail> declineEvent(EventMemberDetail eventMemberDetail);

    public ApiResponse<EventMemberDetail> checkInEvent(EventMemberDetail eventMemberDetail);

    public ApiResponse<EventMemberDetail> setMinsToArriveAsMember(EventMemberDetail eventMemberDetail);

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

    public void subscribeDefaultChannel(String personId, AsyncCallback<List<Message>> defaultChannelMsgResponder, AsyncCallback<Subscription> methodCallback);

    public void subscribeEventChannel(String channelName, String personId, AsyncCallback<List<Message>> channelMsgResponder, AsyncCallback<Subscription> subscriptionResponder);

    public void publishEventChannelMessage(String channelName, String publisherId, boolean fromLeader, String receiverId, Message message);

    public void publishEventChannelMessage(String channelName, String publisherId, boolean fromLeader, List<String> receiverIds, Message message);

    public void publishEventChannelMemberStatus(String channelName, String publisherId, String leaderId, int memberStatus);

    public void publishEventChannelMemberSelectedTimestamps(String channelName, String publisherId, String leaderId, List<MemberSelectedTimestamp> memberSelectedTimestamps);

    public void publishEventChannelMemberProposedTimestamps(String channelName, String publisherId, String leaderId, List<MemberProposedTimestamp> memberProposedTimestamps);

    public void publishEventChannelMemberEstimateInMin(String channelName, String publisherId, String leaderId, int estimateInMin);

    public void broadcastEventChannel(String channelName, String eventId, Person eventLeader, String eventManagementState);

    /**
     * Data Persistence
     */
    public ApiResponse<Person> savePerson(Person person);

    public ApiResponse<Event> saveEvent(Event event);

    // TODO[sichao]: check following API implementation
    public ApiResponse<Long> removeEvent(Event event);

    public ApiResponse<Long> removeEventMember(EventMemberDetail eventMemberDetail);

    public ApiResponse<Long> removeLeaderProposedTimestamp(LeaderProposedTimestamp leaderProposedTimestamp);

    public ApiResponse<Long> removeMemberProposedTimestamp(MemberProposedTimestamp memberProposedTimestamp);

    public ApiResponse<Long> removeMemberSelectedTimestamp(MemberSelectedTimestamp memberSelectedTimestamp);

}
