package edu.scu.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.scu.api.Api;
import edu.scu.api.ApiImpl;
import edu.scu.core.task.AcceptEventAsyncTask;
import edu.scu.core.task.AddEventInformationAsyncTask;
import edu.scu.core.task.AddEventMemberAsyncTask;
import edu.scu.core.task.CancelEventAsyncTask;
import edu.scu.core.task.DeclineEventAsyncTask;
import edu.scu.core.task.InitiateEventAsyncTask;
import edu.scu.core.task.LoginAsyncTask;
import edu.scu.core.task.LogoutAsyncTask;
import edu.scu.core.task.ProposeEventAsyncTask;
import edu.scu.core.task.ProposeEventTimestampsAsLeaderAsyncTask;
import edu.scu.core.task.ProposeEventTimestampsAsMemberAsyncTask;
import edu.scu.core.task.RegisterAsyncTask;
import edu.scu.core.task.RemoveEventMemberAsyncTask;
import edu.scu.core.task.SendEventInvitationAsyncTask;
import edu.scu.core.task.SetMinsToArriveAsMemberAsyncTask;
import edu.scu.core.task.SyncHostInformationAsyncTask;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;
import edu.scu.model.enumeration.PublishEventChannelArgKeyName;
import edu.scu.model.enumeration.StatusEvent;
import edu.scu.model.enumeration.StatusMember;

/**
 * Created by chuanxu on 4/14/16.
 */
public class AppActionImpl implements AppAction {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static String hostUserId;
    private static Person hostPerson;

    private Context context;
    private Api api;

    public AppActionImpl (Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public String getHostUserId() {
        return hostUserId;
    }

    @Override
    public Person getHostPerson() {
        return hostPerson;
    }

    @Override
    public void setHostUserId(String hostUserId) {
        AppActionImpl.hostUserId = hostUserId;
    }

    @Override
    public void setHostPerson(Person hostPerson) {
        AppActionImpl.hostPerson = hostPerson;
    }


    private static List<LeaderProposedTimestamp> proposeEventTimestampsAsLeader(String eventId, String leaderId, List<String> datesInString) {

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

    private static List<MemberProposedTimestamp> proposeEventTimestampsAsMember(String eventId, String leaderId, List<String> datesInString) {

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

    private static List<MemberSelectedTimestamp> selectEventTimestampsAsMember(String eventId, String leaderId, List<String> datesInString) {

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


    @Override
    public void register(final String userEmail, final String password, final String firstName, final String lastName, final ActionCallbackListener<Person> listener) {

//        // check userEmail
//        if (TextUtils.isEmpty(userEmail)) {
//            if (listener != null) {
//                listener.onFailure("Email is empty");
//            }
//        }
//
//        // check password
//        if (TextUtils.isEmpty(password)) {
//            if (listener != null) {
//                listener.onFailure("Password is empty");
//            }
//        }

        // TODO: check name

        // TODO: validate parameters...


        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                return true;
            }
        });

        RegisterAsyncTask registerTask = new RegisterAsyncTask(api, listener, handler, userEmail, password, firstName, lastName);
        registerTask.execute();
    }

    @Override
    public void validateLogin(final ActionCallbackListener<Void> listener) {
        // Check local userToken. If available, it means the user has already logged in.
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        if(userToken != null && !userToken.equals("")) {
            hostUserId = UserIdStorageFactory.instance().getStorage().get();
            listener.onSuccess(null);
        } else {
            listener.onFailure(null);
        }
    }

    @Override
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<String> listener) {

        // check userEmail
        if (TextUtils.isEmpty(userEmail)) {
            if (listener != null) {
                listener.onFailure("User email is empty");
            }
        }

        // check password
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure("Password is empty");
            }
        }

        // TODO: validate password


        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                String userId = bundle.getString("userId");
                hostUserId = userId;
                return true;
            }
        });

        LoginAsyncTask loginAsyncTask = new LoginAsyncTask(api, listener, handler, this, userEmail, password, stayLoggedIn);
        loginAsyncTask.execute();
    }

    @Override
    public void logout(final ActionCallbackListener<Void> listener) {
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event event = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return true;
            }
        });

        LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask(api, listener, handler, this);
        logoutAsyncTask.execute();
    }

    // TODO[later]
    @Override
    public void importGoogleCalendar(final ActionCallbackListener<Void> listener) {

    }

    @Override
    public void proposeEvent(final ActionCallbackListener<Event> listener) {

        AsyncCallback<List<Message>> memberMsgResponder = new AsyncCallback<List<Message>>() {
            @Override
            public void handleResponse(List<Message> messages) {

                String memberId;
                Map<String, String> msgHeader;
                Object msg;
                for (Message message : messages) {
                    memberId = message.getPublisherId();
                    msgHeader = message.getHeaders();
                    msg = message.getData();
                    // TODO: update model

                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                // TODO:
            }
        };

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                boolean isSuccess = hostPerson.addEventAsLeader(updatedEvent);
                if (isSuccess) {
                    listener.onSuccess(updatedEvent);
                } else {
                    listener.onFailure(String.valueOf(R.string.local_update_error));
                }
                return isSuccess;
            }
        });

        EventLeaderDetail eventLeaderDetail = new EventLeaderDetail();
        Person hostPersonInProgress = hostPerson.getBasePerson();
        eventLeaderDetail.setLeader(hostPersonInProgress);
        eventLeaderDetail.setIsCheckedIn(false);
        Event event = new Event();
        event.setStatusEvent(StatusEvent.Tentative.getStatus());
        event.setEventLeaderDetail(eventLeaderDetail);

        ProposeEventAsyncTask proposeEventAsyncTask = new ProposeEventAsyncTask(api, listener, memberMsgResponder, hostPerson.getObjectId(), event, handler);
        proposeEventAsyncTask.execute();
    }

    @Override
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<Event> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                for (Event eventAsMember : hostPerson.getEventsAsLeader()) {
                    if (eventAsMember.getObjectId().equals(eventId)) {
                        EventMemberDetail newEventMemberDetail = updatedEvent.getEventMemberDetail().get(0);
                        eventAsMember.addEventMemberDetail(newEventMemberDetail);
                        listener.onSuccess(updatedEvent);
                        return true;
                    }
                }
                listener.onFailure(String.valueOf(R.string.local_update_error));
                return false;
            }
        });

        Event targetEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                targetEvent = eventAsLeader.getBaseEvent();
                break;
            }
        }
        Event targetEventInProgress = targetEvent.getBaseEvent();

        AddEventMemberAsyncTask addEventMemberAsyncTask = new AddEventMemberAsyncTask(api, listener, handler, eventId, hostPerson.getObjectId(), memberEmail, targetEventInProgress);
        addEventMemberAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void removeEventMember(final String eventId, final String memberId, final ActionCallbackListener<Event> listener) {
        Event passEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                passEvent = eventAsLeader;
                break;
            }
        }

        for (EventMemberDetail memberDetail : passEvent.getEventMemberDetail()) {
            if (memberDetail.getObjectId().equals(memberId)) {
                // TODO: Check if event needs to be removed from member eventsAsMember list
                passEvent.getEventMemberDetail().remove(memberDetail);
            }
        }

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event event = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return true;
            }
        });

        RemoveEventMemberAsyncTask removeEventMemberAsyncTask = new RemoveEventMemberAsyncTask(api, listener, handler, passEvent);
        removeEventMemberAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void addEventInformation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, ActionCallbackListener<Event> listener) {
        Event passEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                eventAsLeader.setTitle(title);
                eventAsLeader.setLocation(location);
                eventAsLeader.setDurationInMin(durationInMin);
                eventAsLeader.setHasReminder(hasReminder);
                eventAsLeader.setReminderInMin(reminderInMin);
                passEvent = eventAsLeader;
            }
        }

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event event = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return true;
            }
        });

        AddEventInformationAsyncTask addEventInformationAsyncTask = new AddEventInformationAsyncTask(api, listener, handler, passEvent);
        addEventInformationAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void sendEventInvitation(final String eventId, final ActionCallbackListener<Event> listener) {
        Event passEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                eventAsLeader.setStatusEvent(StatusEvent.Pending.getStatus());
                passEvent = eventAsLeader;
            }
        }

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event event = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return true;
            }
        });

        SendEventInvitationAsyncTask sendEventInvitationAsyncTask = new SendEventInvitationAsyncTask(api, listener,handler, passEvent);
        sendEventInvitationAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void initiateEvent(final String eventId, final ActionCallbackListener<Integer> listener, final Date eventFinalTimestamp) {
        Event passEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                eventAsLeader.setTimestamp(eventFinalTimestamp);
                eventAsLeader.setStatusEvent(StatusEvent.Ready.getStatus());
                passEvent = eventAsLeader;
            }
        }

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event event = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return true;
            }
        });

        InitiateEventAsyncTask initiateEvent = new InitiateEventAsyncTask(api, listener, handler, passEvent);
        initiateEvent.execute();
    }

    // TODO[refactor]
    @Override
    public void cancelEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return hostPerson.getEventsAsLeader().remove(updatedEvent);
            }
        });

        Event targetEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                targetEvent = eventAsLeader;
                break;
            }
        }
        targetEvent.setStatusEvent(StatusEvent.Cancelled.getStatus());

        CancelEventAsyncTask cancelEventAsyncTask = new CancelEventAsyncTask(api, listener, handler, eventId, targetEvent);
        cancelEventAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void proposeEventTimestampsAsLeader(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventLeaderDetail> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventLeaderDetail updatedEventLeaderDetail = (EventLeaderDetail) bundle.getSerializable(EventLeaderDetail.SERIALIZE_KEY);
                for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
                    if (eventAsLeader.getObjectId().equals(eventId)) {
                        eventAsLeader.updateEventLeaderDetail(updatedEventLeaderDetail);
                        return true;
                    }
                }
                return false;
            }
        });

        List<LeaderProposedTimestamp> proposeEventTimestampsAsLeader = proposeEventTimestampsAsLeader(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        Event targetEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                targetEvent = eventAsLeader;
                break;
            }
        }
        EventLeaderDetail leaderDetail = targetEvent.getEventLeaderDetail();
        leaderDetail.setProposedTimestamps(proposeEventTimestampsAsLeader);

        ProposeEventTimestampsAsLeaderAsyncTask proposeEventTimestampsAsLeaderAsyncTask = new ProposeEventTimestampsAsLeaderAsyncTask(api, listener, handler, eventId, leaderDetail);
        proposeEventTimestampsAsLeaderAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void proposeEventTimestampsAsMember(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) bundle.getSerializable(EventMemberDetail.SERIALIZE_KEY);
                for (Event eventAsMember : hostPerson.getEventsAsMember()) {
                    if (eventAsMember.getObjectId().equals(eventId)) {
                        eventAsMember.updateEventMemberDetail(updatedEventMemberDetail);
                        return true;
                    }
                }
                return false;
            }
        });

        List<MemberProposedTimestamp> proposeEventTimestampsAsMember = proposeEventTimestampsAsMember(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        EventMemberDetail targetEventMemberDetail = null;
        String leaderId = null;
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                targetEventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
                leaderId = eventAsMember.getEventLeaderDetail().getLeader().getObjectId();
                break;
            }
        }
        targetEventMemberDetail.setProposedTimestamps(proposeEventTimestampsAsMember);

        ProposeEventTimestampsAsMemberAsyncTask proposeEventTimestampsAsMemberAsyncTask = new ProposeEventTimestampsAsMemberAsyncTask(api, listener, handler, eventId, hostPerson.getObjectId(), leaderId, targetEventMemberDetail);
        proposeEventTimestampsAsMemberAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void selectEventTimestampsAsMember(final String eventId, final List<String> selectedEventTimestamps, final ActionCallbackListener<Event> listener) {

    }

    // TODO[refactor]
    @Override
    public void acceptEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        AsyncCallback<List<Message>> leaderMsgResponder = new AsyncCallback<List<Message>>() {
            @Override
            public void handleResponse(List<Message> messages) {

                String memberId;
                Map<String, String> msgHeader;
                EventMemberDetail eventMemberDetail;

                for (Message message : messages) {
                    memberId = message.getPublisherId();
                    msgHeader = message.getHeaders();
                    eventMemberDetail = hostPerson.getEventMemberMemberDetail(eventId, memberId);

                    if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_STATUS.getKeyName()).equals("true")) {
                        int memberStatus = (int) message.getData();
                        eventMemberDetail.setStatusMember(memberStatus);
                    } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_PROPOSED_TIME.getKeyName()).equals("true")) {
                        List<MemberProposedTimestamp> memberProposedTimestamps = (List<MemberProposedTimestamp>) message.getData();
                        eventMemberDetail.setProposedTimestamps(memberProposedTimestamps);
                    } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_SELECTED_TIME.getKeyName()).equals("true")) {
                        List<MemberSelectedTimestamp> memberSelectedTimestamps = (List<MemberSelectedTimestamp>) message.getData();
                        eventMemberDetail.setSelectedTimestamps(memberSelectedTimestamps);
                    } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_MINS_TO_ARRIVE.getKeyName()).equals("true")) {
                        int estimateInMin = (int) message.getData();
                        eventMemberDetail.setMinsToArrive(estimateInMin);
                    } else {
                        assert false;
                    }

                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                // TODO:
            }
        };

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                return hostPerson.getEventsAsMember().add(updatedEvent);
            }
        });

        AcceptEventAsyncTask acceptTask = new AcceptEventAsyncTask(api, listener, leaderMsgResponder, handler, eventId, hostPerson.getObjectId());
        acceptTask.execute();

        // TODO: TIMER!!!
        //timer.cancel();
    }

    // TODO[refactor]
    @Override
    public void declineEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) bundle.getSerializable(EventMemberDetail.SERIALIZE_KEY);
                for (Event eventAsMember : hostPerson.getEventsAsMember()) {
                    if (eventAsMember.getObjectId().equals(eventId)) {
                        eventAsMember.updateEventMemberDetail(updatedEventMemberDetail);
                        return true;
                    }
                }
                return false;
            }
        });

        EventMemberDetail targetEventMemberDetail = null;
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                targetEventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
                break;
            }
        }
        targetEventMemberDetail.setStatusMember(StatusMember.Declined.getStatus());

        DeclineEventAsyncTask declineEvent = new DeclineEventAsyncTask(api, listener, handler, eventId, hostPerson.getObjectId(), targetEventMemberDetail);
        declineEvent.execute();
    }

    // TODO: need this any more? We have SetMinsToArriveAsMember(..)
    @Override
    public void checkInEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
//        CheckInEventAsyncTask checkInEventAsyncTask = new CheckInEventAsyncTask(api, listener, handler, eventId);
//        checkInEventAsyncTask.execute();
    }

    // TODO[refactor]
    @Override
    public void setMinsToArriveAsMember(final String eventId, final int estimateInMin, final ActionCallbackListener<Integer> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) bundle.getSerializable(EventMemberDetail.SERIALIZE_KEY);
                for (Event eventAsMember : hostPerson.getEventsAsMember()) {
                    if (eventAsMember.getObjectId().equals(eventId)) {
                        eventAsMember.updateEventMemberDetail(updatedEventMemberDetail);
                        return true;
                    }
                }
                return false;
            }
        });

        EventMemberDetail targetEventMemberDetail = null;
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                targetEventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
                break;
            }
        }
        targetEventMemberDetail.setMinsToArrive(estimateInMin);

        SetMinsToArriveAsMemberAsyncTask setMinsToArriveAsMember = new SetMinsToArriveAsMemberAsyncTask(api, listener, handler, targetEventMemberDetail, eventId, hostPerson.getObjectId());
        setMinsToArriveAsMember.execute();
    }

    @Override
    public void syncHostInformation(String userId, ActionCallbackListener<Void> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                hostPerson = (Person) bundle.getSerializable(Person.SERIALIZE_KEY);
                return true;
            }
        });
        SyncHostInformationAsyncTask syncHostInformationAsyncTask = new SyncHostInformationAsyncTask(api, listener, handler, userId);
        syncHostInformationAsyncTask.execute();
    }

    @Override
    public void startMemberInvitationTimer(final AppAction appAction, final String eventId) {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO: Decline and don't subscribe to event channel
                appAction.declineEvent(eventId, null);
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 30 * 60 * 1000);
    }

}
