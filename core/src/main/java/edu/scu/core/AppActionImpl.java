package edu.scu.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;

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
import edu.scu.core.task.SelectEventTimestampsAsMemberAsyncTask;
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
import edu.scu.model.enumeration.StatusEvent;
import edu.scu.model.enumeration.StatusMember;

/**
 * Created by chuanxu on 4/14/16.
 */
public class AppActionImpl implements AppAction {

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

    @Override
    public void register(final String userEmail, final String password, final String firstName, final String lastName, final ActionCallbackListener<Person> listener) {

        // check userEmail
        if (TextUtils.isEmpty(userEmail)) {
            if (listener != null) {
                listener.onFailure("Please enter your email");
            }
        }

        // check password
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure("Please enter your password");
            }
        }

        // check first name
        if (TextUtils.isEmpty(firstName)) {
            if (listener != null) {
                listener.onFailure("Please enter your first name");
            }
        }

        // check last name
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure("Please enter your last name");
            }
        }

        // TODO: validate parameters...


        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                hostPerson = (Person) bundle.getSerializable(Person.SERIALIZE_KEY);
                listener.onSuccess(hostPerson);
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
                listener.onSuccess(userId);
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
    // TODO[test]
    @Override
    public void importGoogleCalendar(final ActionCallbackListener<Void> listener) {

    }

    @Override
    public void proposeEvent(final ActionCallbackListener<Event> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Person updatedPerson = (Person) bundle.getSerializable(Person.SERIALIZE_KEY);
                Event newEvent = updatedPerson.getEventsAsLeader().get(0);
                boolean isSuccess = hostPerson.addEventAsLeader(newEvent);
                if (isSuccess) {
                    listener.onSuccess(newEvent);
                } else {
                    listener.onFailure(String.valueOf(R.string.local_update_error));
                }
                return isSuccess;
            }
        });

        EventLeaderDetail eventLeaderDetail = new EventLeaderDetail();
        Person hostPersonInProgress = AppActionImplHelper.getBasePerson(hostPerson);
        eventLeaderDetail.setLeader(hostPersonInProgress);
        eventLeaderDetail.setIsCheckedIn(false);
        eventLeaderDetail.setMinsToArrive(-1);

        Event event = new Event();
        event.setStatusEvent(StatusEvent.Tentative.getStatus());
        event.setEventLeaderDetail(eventLeaderDetail);

        hostPersonInProgress.getEventsAsLeader().add(event);

        ProposeEventAsyncTask proposeEventAsyncTask = new ProposeEventAsyncTask(api, listener, handler, hostPersonInProgress);
        proposeEventAsyncTask.execute();
    }

    @Override
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<Event> listener) {

        final Event targetEvent = hostPerson.getEventAsLeader(eventId);
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                EventMemberDetail newEventMemberDetail = updatedEvent.getEventMemberDetail().get(0);

                targetEvent.getEventMemberDetail().add(newEventMemberDetail);
                listener.onSuccess(updatedEvent);
                return true;
            }
        });

        AddEventMemberAsyncTask addEventMemberAsyncTask = new AddEventMemberAsyncTask(api, listener, handler, hostPerson.getObjectId(), memberEmail, targetEventInProgress);
        addEventMemberAsyncTask.execute();
    }

    // TODO[later/refactor]: notify member
    // TODO[test]
    @Override
    public void removeEventMember(final String eventId, final String memberId, final ActionCallbackListener<Event> listener) {

        Event passEvent = hostPerson.getEventAsLeader(eventId);
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

    @Override
    public void addEventInformation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, final String note, final ActionCallbackListener<Event> listener) {

        final Event targetEvent = hostPerson.getEventAsLeader(eventId);
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);
        targetEventInProgress.setTitle(title);
        targetEventInProgress.setLocation(location);
        targetEventInProgress.setDurationInMin(durationInMin);
        targetEventInProgress.setHasReminder(hasReminder);
        targetEventInProgress.setReminderInMin(reminderInMin);
        targetEventInProgress.setNote(note);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);

                targetEvent.setTitle(updatedEvent.getTitle());
                targetEvent.setLocation(updatedEvent.getLocation());
                targetEvent.setDurationInMin(updatedEvent.getDurationInMin());
                targetEvent.setHasReminder(updatedEvent.getHasReminder());
                targetEvent.setReminderInMin(updatedEvent.getReminderInMin());
                targetEvent.setNote(updatedEvent.getNote());
                listener.onSuccess(null);
                return true;
            }
        });

        AddEventInformationAsyncTask addEventInformationAsyncTask = new AddEventInformationAsyncTask(api, listener, handler, targetEventInProgress);
        addEventInformationAsyncTask.execute();
    }

    // TODO[test-messaging]
    @Override
    public void sendEventInvitation(final String eventId, final ActionCallbackListener<Event> listener) {

        // Handle data
        final Event targetEvent = hostPerson.getEventAsLeader(eventId);
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);
        targetEventInProgress.setStatusEvent(StatusEvent.Pending.getStatus());

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);

                targetEvent.setStatusEvent(updatedEvent.getStatusEvent());
                listener.onSuccess(null);
                return true;
            }
        });

        SendEventInvitationAsyncTask sendEventInvitationAsyncTask = new SendEventInvitationAsyncTask(api, listener, handler, targetEventInProgress);
        sendEventInvitationAsyncTask.execute();

        // Handle messaging
        AsyncCallback<List<Message>> channelMsgResponderForLeader = new AsyncCallback<List<Message>>() {
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
                Log.i("cxu", backendlessFault.getMessage());
            }
        };

        AsyncCallback<Subscription> subscriptionResponder = new AsyncCallback<Subscription>() {
            @Override
            public void handleResponse(Subscription subscription) {
                Log.i("cxu", subscription.getChannelName());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("cxu", backendlessFault.getMessage());
            }
        };

        api.registerEventChannelMessaging(eventId);
        api.subscribeEventChannelAsLeader(eventId, hostPerson.getObjectId(), channelMsgResponderForLeader, subscriptionResponder);
//        api.broadcastEventChannel(GoogleProjectSettings.DEFAULT_CHANNEL, eventId, hostPerson.getObjectId(), EventManagementState.SEND_INVITATION.getStatus());

        // TODO[later]: refactor using BaseMessagingAsyncTask below?
//        SendEventInvitationMessagingAsyncTask sendEventInvitationMessagingAsyncTask = new SendEventInvitationMessagingAsyncTask(api, null, null, eventId, hostPerson.getObjectId(), channelMsgResponderForLeader, subscriptionResponder);
//        sendEventInvitationMessagingAsyncTask.execute();
    }

    // TODO[test]
    @Override
    public void initiateEvent(final String eventId, final ActionCallbackListener<Integer> listener, final Date eventFinalTimestamp) {

        final Event targetEvent = hostPerson.getEventAsLeader(eventId);
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);
        targetEventInProgress.setTimestamp(eventFinalTimestamp);
        targetEventInProgress.setStatusEvent(StatusEvent.Ready.getStatus());

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);

                targetEvent.setTimestamp(updatedEvent.getTimestamp());
                targetEvent.setStatusEvent(updatedEvent.getStatusEvent());
                listener.onSuccess(null);
                return true;
            }
        });

        InitiateEventAsyncTask initiateEvent = new InitiateEventAsyncTask(api, listener, handler, targetEventInProgress);
        initiateEvent.execute();
    }

    // TODO[later/refactor]
    // TODO[test]
    @Override
    public void cancelEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        Event targetEvent = hostPerson.getEventAsLeader(eventId);
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);
        targetEventInProgress.setStatusEvent(StatusEvent.Cancelled.getStatus());

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                boolean isSuccess = hostPerson.getEventsAsLeader().remove(updatedEvent);
                if (isSuccess) {
                    listener.onSuccess(true);
                } else {
                    listener.onFailure(String.valueOf(R.string.local_update_error));
                }
                return isSuccess;
            }
        });

        CancelEventAsyncTask cancelEventAsyncTask = new CancelEventAsyncTask(api, listener, handler, eventId, targetEventInProgress);
        cancelEventAsyncTask.execute();
    }

    @Override
    public void proposeEventTimestampsAsLeader(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventLeaderDetail> listener) {

        final Event targetEvent = hostPerson.getEventAsLeader(eventId);
        EventLeaderDetail targetEventLeaderDetail = targetEvent.getEventLeaderDetail();
        EventLeaderDetail targetEventLeaderDetailInProgress = AppActionImplHelper.getBaseEventLeaderDetail(targetEventLeaderDetail);

        List<LeaderProposedTimestamp> proposeEventTimestampsAsLeader = AppActionImplHelper.proposeEventTimestampsAsLeader(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        targetEventLeaderDetailInProgress.setProposedTimestamps(proposeEventTimestampsAsLeader);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventLeaderDetail updatedEventLeaderDetail = (EventLeaderDetail) bundle.getSerializable(EventLeaderDetail.SERIALIZE_KEY);

                targetEvent.getEventLeaderDetail().setProposedTimestamps(updatedEventLeaderDetail.getProposedTimestamps());
                listener.onSuccess(updatedEventLeaderDetail);
                return false;
            }
        });

        ProposeEventTimestampsAsLeaderAsyncTask proposeEventTimestampsAsLeaderAsyncTask = new ProposeEventTimestampsAsLeaderAsyncTask(api, listener, handler, eventId, targetEventLeaderDetailInProgress);
        proposeEventTimestampsAsLeaderAsyncTask.execute();
    }

    // TODO[test]
    @Override
    public void proposeEventTimestampsAsMember(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {

        final Event targetEvent = hostPerson.getEventAsMember(eventId);
        final EventMemberDetail targetEventMemberDetail = targetEvent.getEventMemberDetail(hostPerson.getObjectId());
        EventMemberDetail targetEventMemberDetailInProgress = AppActionImplHelper.getBaseEventMemberDetail(targetEventMemberDetail);
        List<MemberProposedTimestamp> proposeEventTimestampsAsMember = AppActionImplHelper.proposeEventTimestampsAsMember(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        targetEventMemberDetailInProgress.setProposedTimestamps(proposeEventTimestampsAsMember);

        // TODO[not done yet]: get leaderId
        String leaderId = null;
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                targetEventMemberDetailInProgress = eventAsMember.getEventMemberDetail().get(0);
                leaderId = eventAsMember.getEventLeaderDetail().getLeader().getObjectId();
                break;
            }
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) bundle.getSerializable(EventMemberDetail.SERIALIZE_KEY);

                targetEventMemberDetail.setProposedTimestamps(updatedEventMemberDetail.getProposedTimestamps());
                listener.onSuccess(updatedEventMemberDetail);
                return false;
            }
        });

        ProposeEventTimestampsAsMemberAsyncTask proposeEventTimestampsAsMemberAsyncTask = new ProposeEventTimestampsAsMemberAsyncTask(api, listener, handler, eventId, hostPerson.getObjectId(), leaderId, targetEventMemberDetailInProgress);
        proposeEventTimestampsAsMemberAsyncTask.execute();
    }

    // TODO[test]
    @Override
    public void selectEventTimestampsAsMember(final String eventId, final List<String> selectedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {

        final Event targetEvent = hostPerson.getEventAsMember(eventId);
        final EventMemberDetail targetEventMemberDetail = targetEvent.getEventMemberDetail(hostPerson.getObjectId());
        EventMemberDetail targetEventMemberDetailInProgress = AppActionImplHelper.getBaseEventMemberDetail(targetEventMemberDetail);
        List<MemberSelectedTimestamp> selectEventTimestampsAsMember = AppActionImplHelper.selectEventTimestampsAsMember(eventId, hostPerson.getObjectId(), selectedEventTimestamps);
        targetEventMemberDetailInProgress.setSelectedTimestamps(selectEventTimestampsAsMember);

        // TODO[not done yet]: get leaderId
        String leaderId = null;
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                targetEventMemberDetailInProgress = eventAsMember.getEventMemberDetail().get(0);
                leaderId = eventAsMember.getEventLeaderDetail().getLeader().getObjectId();
                break;
            }
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) bundle.getSerializable(EventMemberDetail.SERIALIZE_KEY);

                targetEventMemberDetail.setSelectedTimestamps(updatedEventMemberDetail.getSelectedTimestamps());
                listener.onSuccess(updatedEventMemberDetail);
                return false;
            }
        });

        SelectEventTimestampsAsMemberAsyncTask selectEventTimestampsAsMemberAsyncTask = new SelectEventTimestampsAsMemberAsyncTask(api, listener, handler, eventId, hostPerson.getObjectId(), leaderId, targetEventMemberDetailInProgress);
        selectEventTimestampsAsMemberAsyncTask.execute();
    }

    // TODO[refactor]: already received event object from broadcast receiver
    // TODO[test]
    @Override
    public void acceptEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        AsyncCallback<List<Message>> channelMsgResponderForMember = new AsyncCallback<List<Message>>() {

            @Override
            public void handleResponse(List<Message> messages) {
                // TODO[later]: no need to support member monitor channel yet
//                String memberId;
//                Map<String, String> msgHeader;
//                EventMemberDetail eventMemberDetail;
//
//                for (Message message : messages) {
//                    memberId = message.getPublisherId();
//                    msgHeader = message.getHeaders();
//                    eventMemberDetail = hostPerson.getEventMemberMemberDetail(eventId, memberId);
//
//                    if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_STATUS.getKeyName()).equals("true")) {
//                        int memberStatus = (int) message.getData();
//                        eventMemberDetail.setStatusMember(memberStatus);
//                    } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_PROPOSED_TIME.getKeyName()).equals("true")) {
//                        List<MemberProposedTimestamp> memberProposedTimestamps = (List<MemberProposedTimestamp>) message.getData();
//                        eventMemberDetail.setProposedTimestamps(memberProposedTimestamps);
//                    } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_SELECTED_TIME.getKeyName()).equals("true")) {
//                        List<MemberSelectedTimestamp> memberSelectedTimestamps = (List<MemberSelectedTimestamp>) message.getData();
//                        eventMemberDetail.setSelectedTimestamps(memberSelectedTimestamps);
//                    } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_MINS_TO_ARRIVE.getKeyName()).equals("true")) {
//                        int estimateInMin = (int) message.getData();
//                        eventMemberDetail.setMinsToArrive(estimateInMin);
//                    } else {
//                        assert false;
//                    }
//                }

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
            }
        };

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                boolean isSuccess = hostPerson.getEventsAsMember().add(updatedEvent);
                if (isSuccess) {
                    listener.onSuccess(true);
                } else {
                    listener.onFailure(String.valueOf(R.string.local_update_error));
                }
                return isSuccess;
            }
        });

        AcceptEventAsyncTask acceptTask = new AcceptEventAsyncTask(api, listener, channelMsgResponderForMember, handler, eventId, hostPerson.getObjectId());
        acceptTask.execute();

        // TODO: TIMER!!!
        //timer.cancel();
    }

    // TODO[refactor]: already received event object from broadcast receiver
    // TODO[test]
    @Override
    public void declineEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        EventMemberDetail targetEventMemberDetail = null;
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                targetEventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
                break;
            }
        }
        targetEventMemberDetail.setStatusMember(StatusMember.Declined.getStatus());

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

        DeclineEventAsyncTask declineEvent = new DeclineEventAsyncTask(api, listener, handler, eventId, hostPerson.getObjectId(), targetEventMemberDetail);
        declineEvent.execute();
    }

    // TODO: need this any more? We have SetMinsToArriveAsMember(..)
    @Override
    public void checkInEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
//        CheckInEventAsyncTask checkInEventAsyncTask = new CheckInEventAsyncTask(api, listener, handler, eventId);
//        checkInEventAsyncTask.execute();
    }

    // TODO[test]
    @Override
    public void setMinsToArriveAsMember(final String eventId, final int estimateInMin, final ActionCallbackListener<Integer> listener) {

        final Event targetEvent = hostPerson.getEventAsMember(eventId);
        final EventMemberDetail targetEventMemberDetail = targetEvent.getEventMemberDetail(hostPerson.getObjectId());
        EventMemberDetail targetEventMemberDetailInProgress = AppActionImplHelper.getBaseEventMemberDetail(targetEventMemberDetail);
        targetEventMemberDetailInProgress.setMinsToArrive(estimateInMin);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) bundle.getSerializable(EventMemberDetail.SERIALIZE_KEY);

                targetEventMemberDetail.setMinsToArrive(updatedEventMemberDetail.getMinsToArrive());
                listener.onSuccess(updatedEventMemberDetail.getMinsToArrive());
                return true;
            }
        });

        SetMinsToArriveAsMemberAsyncTask setMinsToArriveAsMember = new SetMinsToArriveAsMemberAsyncTask(api, listener, handler, targetEventMemberDetailInProgress, eventId, hostPerson.getObjectId());
        setMinsToArriveAsMember.execute();
    }

    @Override
    public void syncHostInformation(final String userId, final ActionCallbackListener<Void> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Person syncedPerson = (Person) bundle.getSerializable(Person.SERIALIZE_KEY);

                hostPerson = syncedPerson;
                listener.onSuccess(null);
                return true;
            }
        });

        SyncHostInformationAsyncTask syncHostInformationAsyncTask = new SyncHostInformationAsyncTask(api, listener, handler, userId);
        syncHostInformationAsyncTask.execute();
    }

    // TODO: refactor timer vs service
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
