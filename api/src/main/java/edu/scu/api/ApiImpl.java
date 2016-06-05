package edu.scu.api;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scu.api.query.BackendlessQueryHelper;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.EventUndecided;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;
import edu.scu.model.enumeration.BroadcastEventChannelArgKeyName;
import edu.scu.model.enumeration.PublishEventChannelArgKeyName;
import edu.scu.model.enumeration.StatusMember;
import edu.scu.util.lib.GoogleProjectSettings;

/**
 * Created by chuanxu on 4/13/16.
 */
public class ApiImpl implements Api {

    private final static String SUCCESS_EVENT = "0";
    private final static String FAIL_EVENT = "1";

    // TODO[Later]: Delete after development
    private final static String idPersonChuan = "43501AA8-843D-113C-FF5A-7F015D78F300";
    private final static String idPersonSichao = "9F289070-D392-2C21-FF7F-2D20409CA400";
    private final static String idPersonHairong = "28E7440B-B6F3-8715-FF64-0F08AF049400";
    private final static String idUserChuan = "FA3F28DA-362E-3492-FF03-18E2DE3E2400";
    private final static String idUserSichao = "C91D6698-A1D0-0A85-FF20-9A8475F93600";
    private final static String idUserHairong = "F6651BEB-EDF4-65BD-FFA3-8CD20C756C00";

    @Override
    public ApiResponse<Person> register(String userEmail, String password, String firstName, String lastName) {

        // Register
        BackendlessUser user = new BackendlessUser();
        user.setEmail(userEmail);
        user.setPassword(password);
        user.setProperty("firstName", firstName);
        user.setProperty("lastName", lastName);
        try {
            user = Backendless.UserService.register(user);
        } catch(BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Create Person object
        Person person = new Person();
        person.setUserId(user.getObjectId());
        person.setEmail(userEmail);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setIsGoogleCalendarImported(false);
        try {
            person = Backendless.Data.of(Person.class).save(person);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "You have successfully registered", person);
    }

    @Override
    public ApiResponse<String> login(String userEmail, String password, boolean stayLoggedIn) {
        // Step 1: start login process
        BackendlessUser user;
        try {
            user = Backendless.UserService.login(userEmail, password, stayLoggedIn);
        } catch( BackendlessException exception ) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Step 2: validate login
        boolean isValidLogin = Backendless.UserService.isValidLogin();
        if (isValidLogin) {
            return new ApiResponse<>(SUCCESS_EVENT, "You have successfully logged in", user.getUserId());
        }
        return new ApiResponse<>(FAIL_EVENT, "Invalid login information");
    }

    @Override
    public ApiResponse<Void> logout() {
        // Step 1: validate login
        boolean isValidLogin = Backendless.UserService.isValidLogin();
        if (!isValidLogin) {
            return new ApiResponse<>(FAIL_EVENT, "");
        }

        // Step 2: start logout process
        try {
            Backendless.UserService.logout();
        } catch( BackendlessException exception ) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "You have successfully logged out");
    }

    // TODO[later]
    @Override
    public ApiResponse<Void> importGoogleCalendar(String personId) {
        return null;
    }

    @Override
    public ApiResponse<Person> proposeEvent(Person person) {

        try {
            person = Backendless.Data.of(Person.class).save(person);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Propose event success", person);
    }

    @Override
    public ApiResponse<Event> addEventMember(Event event, String leaderId, String memberEmail) {

        // Query member
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryPerson(memberEmail));
        BackendlessCollection<Person> result;
        try {
            result = Backendless.Data.of(Person.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member doesn't exist");
        }

        // Create new EventMemberDetail object
        Person member = result.getData().get(0);
        EventMemberDetail eventMemberDetail = new EventMemberDetail();
        eventMemberDetail.setLeaderId(leaderId);
        eventMemberDetail.setEventId(event.getObjectId());
        eventMemberDetail.setMember(member);
        eventMemberDetail.setStatusMember(StatusMember.Pending.getStatus());
        eventMemberDetail.setIsCheckedIn(false);
        eventMemberDetail.setMinsToArrive(-1);

        List<EventMemberDetail> eventMemberDetails = new ArrayList<>();
        eventMemberDetails.add(eventMemberDetail);
        event.setEventMemberDetail(eventMemberDetails);
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Add event member success", event);
    }

    @Override
    public ApiResponse<Event> removeEventMember(Event event) {

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event member success", event);
    }

    @Override
    public ApiResponse<Event> sendEventInvitation(Event event) {

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Send invitation success", event);
    }

    @Override
    public ApiResponse<Event> initiateEvent(Event event) {

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Event Initiate", event);
    }

    @Override
    public ApiResponse<Event> cancelEvent(Event event) {

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Event Initiate", event);
    }

    @Override
    public ApiResponse<EventLeaderDetail> proposeEventTimestampsAsLeader(EventLeaderDetail eventLeaderDetail) {

        try {
            eventLeaderDetail = Backendless.Data.of(EventLeaderDetail.class).save(eventLeaderDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Propose event time success", eventLeaderDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> proposeEventTimestampsAsMember(EventMemberDetail eventMemberDetail) {

        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Propose event time success", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> selectEventTimestampsAsMember(EventMemberDetail eventMemberDetail) {

        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Select event time success", eventMemberDetail);
    }

    @Override
    public ApiResponse<Person> acceptEvent(Person member, String eventId, String memberId) {

        // Get target event
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        assert event != null;

        // Load undecidedEvent with eventMemberDetail
        ArrayList<String> relationProps = new ArrayList<>();
        relationProps.add( "eventMemberDetail" );
        relationProps.add( "eventMemberDetail.member" );
        try {
            Backendless.Data.of(Event.class).loadRelations(event, relationProps);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        // TODO[later]: pass the fully loaded eventMemberDetail below to host member in order to show event member details
        EventMemberDetail eventMemberDetail = event.getEventMemberDetail(memberId);

        // Change member status
        List<EventMemberDetail> eventMemberDetails = new ArrayList<>();
        eventMemberDetail.setStatusMember(StatusMember.Accept.getStatus());
        eventMemberDetails.add(eventMemberDetail);
        event.setEventMemberDetail(eventMemberDetails);

        // Member accepts the event
        List<Event> eventsAsMember = new ArrayList<>();
        eventsAsMember.add(event);
        member.setEventsAsMember(eventsAsMember);

        // Load member with undecidedEvent
        relationProps = new ArrayList<>();
        relationProps.add( "eventsUndecided" );
        try {
            Backendless.Data.of(Person.class).loadRelations(member, relationProps);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        List<EventUndecided> eventUndecidedList = member.getEventsUndecided();

        // Get target undecidedEvent
        EventUndecided undecidedEvent = null;
        for (EventUndecided undecided : eventUndecidedList) {
            if (undecided.eventId.equals(eventId)) {
                undecidedEvent = undecided;
                break;
            }
        }
        assert undecidedEvent != null;

        // Remove target undecidedEvent from EventUndecided table
        try {
             Backendless.Data.of(EventUndecided.class).remove(undecidedEvent);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Trigger server logic as server monitors EventMemberDetail table
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Accepted event", member);
    }

    @Override
    public ApiResponse<EventMemberDetail> declineEvent(Person member, String eventId, String memberId) {

        // Get target event
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        assert event != null;

        // Load undecidedEvent with event member detail
        ArrayList<String> relationProps = new ArrayList<>();
        relationProps.add( "eventMemberDetail" );
        relationProps.add( "eventMemberDetail.member" );
        try {
            Backendless.Data.of(Event.class).loadRelations(event, relationProps);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Get target EventMemberDetail object
        EventMemberDetail eventMemberDetail = event.getEventMemberDetail(memberId);
        eventMemberDetail.setStatusMember(StatusMember.Declined.getStatus());
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Load member with undecidedEvent
        relationProps = new ArrayList<>();
        relationProps.add( "eventsUndecided" );
        try {
            Backendless.Data.of(Person.class).loadRelations(member, relationProps);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        List<EventUndecided> eventUndecidedList = member.getEventsUndecided();

        // Get target undecidedEvent
        EventUndecided undecidedEvent = null;
        for (EventUndecided undecided : eventUndecidedList) {
            if (undecided.getObjectId().equals(eventId)) {
                undecidedEvent = undecided;
                break;
            }
        }
        assert undecidedEvent != null;

        // Remove target undecidedEvent from EventUndecided table
        try {
            Backendless.Data.of(EventUndecided.class).remove(undecidedEvent);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Accepted event", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> checkInEvent(EventMemberDetail eventMemberDetail) {

        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Checked in event", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> setMinsToArriveAsMember(EventMemberDetail eventMemberDetail) {

        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Set estimate success", eventMemberDetail);
    }

    @Override
    public ApiResponse<Void> registerDevice() {
        List<String> channels = new ArrayList<>();
        channels.add(GoogleProjectSettings.DEFAULT_CHANNEL);
        Date expireDate = new Date();
        expireDate.setTime(System.currentTimeMillis() + 500 * 365 * 24 * 60 * 60 * 1000);
        Backendless.Messaging.registerDevice(GoogleProjectSettings.GOOGLE_PROJECT_NUMBER, channels, expireDate);
        return new ApiResponse<>(SUCCESS_EVENT, "Register device success");
    }

    @Override
    public ApiResponse<Void> unregisterDevice() {
        Backendless.Messaging.unregisterDevice();
        return new ApiResponse<>(SUCCESS_EVENT, "Unregister device success");
    }

    @Override
    public ApiResponse<Person> syncHostInformation(String userId) {
        Person hostPerson;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        StringBuilder whereClause;

        //get this person
        whereClause = new StringBuilder();
        whereClause.append("userId = '").append(userId).append("'");
        dataQuery.setWhereClause(whereClause.toString());
        BackendlessCollection<Person> hostPersonCollection;
        try {
            hostPersonCollection = Backendless.Data.of(Person.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        List<Person> personList = hostPersonCollection.getData();
        if (personList.size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "User doesn't exist");
        }
        hostPerson = personList.get(0);

        ArrayList<String> relationProps = new ArrayList<>();
        //first level
        relationProps.add( "contacts" );
        relationProps.add( "eventsAsLeader" );
        relationProps.add( "eventsAsMember" );
        relationProps.add( "eventsUndecided" );

        //second level
        relationProps.add( "eventsAsLeader.eventLeaderDetail" );
        relationProps.add( "eventsAsLeader.eventMemberDetail" );

        relationProps.add( "eventsAsMember.eventLeaderDetail" );
        relationProps.add( "eventsAsMember.eventMemberDetail" );

        //third level
        relationProps.add( "eventsAsLeader.eventLeaderDetail.proposedTimestamps" );
        relationProps.add( "eventsAsLeader.eventLeaderDetail.leader" );
        relationProps.add( "eventsAsLeader.eventMemberDetail.selectedTimestamps" );
        relationProps.add( "eventsAsLeader.eventMemberDetail.proposedTimestamps" );
        relationProps.add( "eventsAsLeader.eventMemberDetail.member" );

        relationProps.add( "eventsAsMember.eventLeaderDetail.proposedTimestamps" );
        relationProps.add( "eventsAsMember.eventLeaderDetail.leader" );
        relationProps.add( "eventsAsMember.eventMemberDetail.selectedTimestamps" );
        relationProps.add( "eventsAsMember.eventMemberDetail.proposedTimestamps" );
        relationProps.add( "eventsAsMember.eventMemberDetail.member" );

        try {
            Backendless.Data.of( Person.class ).loadRelations( hostPerson, relationProps );
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<> (SUCCESS_EVENT, "Sync with server success", hostPerson);
    }

    @Override
    public void registerEventChannelMessaging(String channelName) {
        Date expireDate = new Date();
        expireDate.setTime(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000);
        Backendless.Messaging.registerDevice(GoogleProjectSettings.GOOGLE_PROJECT_NUMBER, channelName);
    }

    @Override
    public void subscribeDefaultChannel(String personId, AsyncCallback<List<Message>> defaultChannelMsgResponder, AsyncCallback<Subscription> subscriptionResponder) {
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        String selector = "receiverId" + personId.replace("-", "") + " = 'true'";
        subscriptionOptions.setSelector(selector);
        Backendless.Messaging.subscribe(GoogleProjectSettings.DEFAULT_CHANNEL, defaultChannelMsgResponder, subscriptionOptions);
    }

    @Override
    public void subscribeEventChannel(String channelName, String personId, AsyncCallback<List<Message>> channelMsgResponder, AsyncCallback<Subscription> subscriptionResponder) {
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        String selector = "receiverId" + personId.replace("-", "") + " = 'true'";
        subscriptionOptions.setSelector(selector);
        Backendless.Messaging.subscribe(channelName, channelMsgResponder, subscriptionOptions, subscriptionResponder);
    }

    @Override
    public void publishEventChannelMessage(String channelName, String publisherId, boolean fromLeader, String receiverId, Message message) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(publisherId);
        publishOptions.putHeader("sentFrom", fromLeader ? "leader" : "member");
        publishOptions.putHeader("receiverId" + receiverId.replace("-", ""), "true");
        Backendless.Messaging.publish(channelName, message, publishOptions);
    }

    @Override
    public void publishEventChannelMessage(String channelName, String publisherId, boolean fromLeader, List<String> receiverIds, Message message) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(publisherId);
        publishOptions.putHeader("sentFrom", fromLeader ? "leader" : "member");
        for (String memberId : receiverIds) {
            publishOptions.putHeader("receiverId" + memberId.replace("-", ""), "true");
        }
        Backendless.Messaging.publish(channelName, message, publishOptions);
    }

    @Override
    public void publishEventChannelMemberStatus(String channelName, String publisherId, String leaderId, int memberStatus) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(publisherId);
        publishOptions.putHeader("sentFrom", "member");
        publishOptions.putHeader("receiverId" + leaderId.replace("-", ""), "true");
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName(), channelName);
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName(), leaderId);
        publishOptions.putHeader(PublishEventChannelArgKeyName.MEMBER_STATUS.getKeyName(), "true");
        Backendless.Messaging.publish(channelName, memberStatus, publishOptions, new AsyncCallback<MessageStatus>() {

            @Override
            public void handleResponse(MessageStatus messageStatus) {
                Log.i("cxu", "Message sent success: " + messageStatus.getMessageId());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("cxu", "Message sent fail: " + backendlessFault.getCode());
            }
        });
    }

    @Override
    public void publishEventChannelMemberSelectedTimestamps(String channelName, String publisherId, String leaderId, List<MemberSelectedTimestamp> memberSelectedTimestamps) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(publisherId);
        publishOptions.putHeader("sentFrom", "member");
        publishOptions.putHeader("receiverId" + leaderId.replace("-", ""), "true");
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName(), channelName);
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName(), leaderId);
        publishOptions.putHeader(PublishEventChannelArgKeyName.MEMBER_SELECTED_TIME.getKeyName(), "true");
        Backendless.Messaging.publish(channelName, memberSelectedTimestamps, publishOptions, new AsyncCallback<MessageStatus>() {

            @Override
            public void handleResponse(MessageStatus messageStatus) {
                Log.i("cxu", "Message sent success: " + messageStatus.getMessageId());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("cxu", "Message sent fail: " + backendlessFault.getCode());
            }
        });
    }

    @Override
    public void publishEventChannelMemberProposedTimestamps(String channelName, String publisherId, String leaderId, List<MemberProposedTimestamp> memberProposedTimestamps) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(publisherId);
        publishOptions.putHeader("sentFrom", "member");
        publishOptions.putHeader("receiverId" + leaderId.replace("-", ""), "true");
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName(), channelName);
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName(), leaderId);
        publishOptions.putHeader(PublishEventChannelArgKeyName.MEMBER_PROPOSED_TIME.getKeyName(), "true");
        Backendless.Messaging.publish(channelName, memberProposedTimestamps, publishOptions, new AsyncCallback<MessageStatus>() {

            @Override
            public void handleResponse(MessageStatus messageStatus) {
                Log.i("cxu", "Message sent success: " + messageStatus.getMessageId());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("cxu", "Message sent fail: " + backendlessFault.getCode());
            }
        });
    }

    @Override
    public void publishEventChannelMemberEstimateInMin(String channelName, String publisherId, String leaderId, int estimateInMin) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(publisherId);
        publishOptions.putHeader("sentFrom", "member");
        publishOptions.putHeader("receiverId" + leaderId.replace("-", ""), "true");
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName(), channelName);
        publishOptions.putHeader(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName(), leaderId);
        publishOptions.putHeader(PublishEventChannelArgKeyName.MEMBER_MINS_TO_ARRIVE.getKeyName(), "true");
        Backendless.Messaging.publish(channelName, estimateInMin, publishOptions, new AsyncCallback<MessageStatus>() {

            @Override
            public void handleResponse(MessageStatus messageStatus) {
                Log.i("cxu", "Message sent success: " + messageStatus.getMessageId());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("cxu", "Message sent fail: " + backendlessFault.getCode());
            }
        });
    }

    @Override
    public void broadcastEventChannel(String channelName, String eventId, Person eventLeader, String eventManagementState) {
        String dispatchedEventName = "ChannelMessaging";
        Map<String, String> args = new HashMap<>();
        args.put(BroadcastEventChannelArgKeyName.CHANNEL_NAME.getKeyName(), channelName);
        args.put(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName(), eventId);
        args.put(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName(), eventLeader.getObjectId());
        args.put(BroadcastEventChannelArgKeyName.EVENT_MANAGEMENT_STATE.getKeyName(), eventManagementState);
        args.put(BroadcastEventChannelArgKeyName.EVENT_LEADER_NAME.getKeyName(), eventLeader.getFirstName() + " " + eventLeader.getLastName());

        Backendless.Events.dispatch(dispatchedEventName, args, new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map result) {
                // This can be a place for leader to know when server finishes notifying all members
                Log.i( "cxu", "received result " + result );
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i( "cxu", "got error " + backendlessFault.toString() );
            }
        });
    }

    @Override
    public ApiResponse<Person> savePerson(Person person) {
        try {
            person = Backendless.Persistence.save(person);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Save person success", person);
    }

    @Override
    public ApiResponse<Event> saveEvent(Event event) {
        try {
            event = Backendless.Persistence.save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Save event success", event);
    }

    @Override
    public ApiResponse<Long> removeEvent(Event event) {

        Long result;
        try {

            if (event.getEventMemberDetail() != null) {
                for (EventMemberDetail memberDetail : event.getEventMemberDetail()) {
                    result = Backendless.Data.of(EventMemberDetail.class).remove(memberDetail);

                    if (memberDetail.getProposedTimestamps() != null) {
                        for (MemberProposedTimestamp proposedTimestamp : memberDetail.getProposedTimestamps()) {
                            result = Backendless.Data.of(MemberProposedTimestamp.class).remove(proposedTimestamp);
                        }
                    }
                    if (memberDetail.getSelectedTimestamps() != null) {
                        for (MemberSelectedTimestamp selectedTimestamp : memberDetail.getSelectedTimestamps()) {
                            result = Backendless.Data.of(MemberSelectedTimestamp.class).remove(selectedTimestamp);
                        }
                    }
                }
            }

            List<LeaderProposedTimestamp> leaderProposedTimestamps = event.getEventLeaderDetail().getProposedTimestamps();
            if (leaderProposedTimestamps != null) {

                for (LeaderProposedTimestamp leaderProposedTimestamp : leaderProposedTimestamps) {
                    result = Backendless.Data.of(LeaderProposedTimestamp.class).remove(leaderProposedTimestamp);
                }
                result = Backendless.Data.of(EventLeaderDetail.class).remove(event.getEventLeaderDetail());
            }

            EventLeaderDetail leaderDetail = event.getEventLeaderDetail();
            result = Backendless.Data.of(EventLeaderDetail.class).remove(leaderDetail);
            result = Backendless.Data.of(Event.class).remove(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event success", result);
    }

    @Override
    public ApiResponse<Long> removeEventMember(EventMemberDetail eventMemberDetail) {
        Long result;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).remove(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event success", result);
    }

    @Override
    public ApiResponse<Long> removeLeaderProposedTimestamp(LeaderProposedTimestamp leaderProposedTimestamp) {
        Long result;
        try {
            result = Backendless.Data.of(LeaderProposedTimestamp.class).remove(leaderProposedTimestamp);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event success", result);
    }

    @Override
    public ApiResponse<Long> removeMemberProposedTimestamp(MemberProposedTimestamp memberProposedTimestamp) {
        Long result;
        try {
            result = Backendless.Data.of(MemberProposedTimestamp.class).remove(memberProposedTimestamp);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event success", result);
    }

    @Override
    public ApiResponse<Long> removeMemberSelectedTimestamp(MemberSelectedTimestamp memberSelectedTimestamp) {
        Long result;
        try {
            result = Backendless.Data.of(MemberSelectedTimestamp.class).remove(memberSelectedTimestamp);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event success", result);
    }

}
