package edu.scu.core;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.scu.api.Api;
import edu.scu.api.ApiImpl;
import edu.scu.core.callback.EventChannelMessageLeaderResponder;
import edu.scu.core.callback.EventChannelMessageMemberResponder;
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
import edu.scu.core.task.SetMinsToArriveAsMemberAsyncTask;
import edu.scu.core.task.SyncHostInformationAsyncTask;
import edu.scu.core.task.messaging.ChannelSubscription;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.EventUndecided;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;
import edu.scu.model.enumeration.EventManagementState;
import edu.scu.model.enumeration.StatusEvent;
import edu.scu.model.enumeration.StatusMember;
import edu.scu.util.lib.GoogleProjectSettings;

/**
 * Created by chuanxu on 4/14/16.
 */
public class AppActionImpl implements AppAction {

    private static String hostUserId;
    private static Person hostPerson;
    private static Map<String, ChannelSubscription> channelMap = new HashMap();

    private Api api;

    public AppActionImpl () {
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
    public void addToChannelMap(String channelName, AsyncCallback<List<Message>> channelMsgResponder, Subscription subscription) {
        ChannelSubscription channelSubscription = new ChannelSubscription(channelMsgResponder, subscription);
        channelMap.put(channelName, channelSubscription);
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

        LoginAsyncTask loginAsyncTask = new LoginAsyncTask(api, listener, handler, userEmail, password, stayLoggedIn);
        loginAsyncTask.execute();

        // Subscribe to default channel
//        defaultChannelMsgResponder = new AsyncCallback<List<Message>>() {
//
//            @Override
//            public void handleResponse(List<Message> messages) {
//
//                Event event = null;
//                for (Message message : messages) {
//                    if (!message.getHeaders().get(hostPerson.getObjectId()).equals("true")) {
//                        continue;
//                    }
//
//                    event = (Event) message.getData();
//                    undecidedEventList.add(event);
//                }
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//                Toast.makeText(context, "Subscribe to default channel fail", Toast.LENGTH_SHORT).show();
//            }
//        };

//        defaultChannelMsgResponder = new DefaultChannelMessageResponder(null, undecidedEventList);
//        api.subscribeDefaultChannel(null, defaultChannelMsgResponder, new SubscriptionResponder());
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
        if (targetEvent.hasEventMember(memberEmail)) {
            listener.onFailure(memberEmail + " has been added to current event");
            return;
        } else if (hostPerson.getEmail().equals(memberEmail)) {
            listener.onFailure("You are already in this event");
            return;
        }
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Event updatedEvent = (Event) bundle.getSerializable(Event.SERIALIZE_KEY);
                EventMemberDetail newEventMemberDetail = updatedEvent.getEventMemberDetail().get(0);

                if (targetEvent.getEventMemberDetail() == null) {
                    targetEvent.setEventMemberDetail(updatedEvent.getEventMemberDetail());
                } else {
                    targetEvent.getEventMemberDetail().add(newEventMemberDetail);
                }
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
    public void sendEventInvitation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, final String note, final ActionCallbackListener<Event> listener) {

        final Event targetEvent = hostPerson.getEventAsLeader(eventId);
        Event targetEventInProgress = AppActionImplHelper.getBaseEvent(targetEvent);
        targetEventInProgress.setTitle(title);
        targetEventInProgress.setLocation(location);
        targetEventInProgress.setDurationInMin(durationInMin);
        targetEventInProgress.setHasReminder(hasReminder);
        targetEventInProgress.setReminderInMin(reminderInMin);
        targetEventInProgress.setNote(note == null ? "" : note);
        targetEventInProgress.setStatusEvent(StatusEvent.Pending.getStatus());

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
                targetEvent.setStatusEvent(updatedEvent.getStatusEvent());
                listener.onSuccess(null);
                return true;
            }
        });

        AddEventInformationAsyncTask addEventInformationAsyncTask = new AddEventInformationAsyncTask(api, listener, handler, targetEventInProgress);
        addEventInformationAsyncTask.execute();

        // Handle messaging
        final EventChannelMessageLeaderResponder channelMsgLeaderResponder = new EventChannelMessageLeaderResponder(hostPerson);
        AsyncCallback<Subscription> subscriptionResponder = new AsyncCallback<Subscription>() {
            @Override
            public void handleResponse(Subscription response) {
                addToChannelMap(targetEvent.getObjectId(), channelMsgLeaderResponder, response);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
            }
        };

        try {
            api.registerEventChannelMessaging(eventId);
            api.subscribeEventChannel(eventId, hostPerson.getObjectId(), channelMsgLeaderResponder, subscriptionResponder);
            api.broadcastEventChannel(GoogleProjectSettings.DEFAULT_CHANNEL, eventId, hostPerson, EventManagementState.SEND_INVITATION.getStatus());
        } catch (BackendlessException e) {
            e.printStackTrace();
        }

        // TODO[later]: refactor using BaseMessagingAsyncTask below?
//        SendEventInvitationMessagingAsyncTask sendEventInvitationMessagingAsyncTask = new SendEventInvitationMessagingAsyncTask(api, null, null, eventId, hostPerson.getObjectId(), channelMsgResponderForLeader, new SubscriptionResponder());
//        sendEventInvitationMessagingAsyncTask.execute();
    }

    // TODO[test]
    @Override
    public void initiateEvent(final String eventId, final Date eventFinalTimestamp, final ActionCallbackListener<Integer> listener) {

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

    // TODO[server side cancellation/notification]
    @Override
    public void cancelEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        final Event targetEvent = hostPerson.getEventAsLeader(eventId);

        // Invitation has not been sent out
        if (targetEvent.getStatusEvent().equals(StatusEvent.Tentative.getStatus())) {

            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(android.os.Message msg) {
                    Bundle bundle = msg.getData();
                    Long result = (Long) bundle.getSerializable(Event.SERIALIZE_KEY);
                    boolean isSuccess = hostPerson.getEventsAsLeader().remove(targetEvent);
                    if (isSuccess) {
                        listener.onSuccess(true);
                    } else {
                        listener.onFailure(String.valueOf(R.string.local_update_error));
                    }
                    return isSuccess;
                }
            });

            // No need to handle any proposed or selected time in this async task as the invitation has not been sent out
            CancelEventAsyncTask cancelEventAsyncTask = new CancelEventAsyncTask(api, listener, handler, targetEvent);
            cancelEventAsyncTask.execute();

        } else {
            // Invitation has been sent out -> take care of cancellation on server side

            String channelName = eventId;
            try {
                // TODO[later]: unregister from channel
                // TODO[later]: notify member
                // TODO[later]: member unregister from channel
                api.broadcastEventChannel(channelName, eventId, hostPerson, EventManagementState.CANCEL_EVENT.getStatus());
            } catch (BackendlessException e) {
                e.printStackTrace();
            }
        }

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

        String channelName = eventId;
        try {
            api.broadcastEventChannel(channelName, eventId, hostPerson, EventManagementState.REMIND_TO_VOTE.getStatus());
        } catch (BackendlessException e) {
            e.printStackTrace();
        }
    }

    // TODO[test]
    @Override
    public void proposeEventTimestampsAsMember(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {

        final Event targetEvent = hostPerson.getEventAsMember(eventId);
        final EventMemberDetail targetEventMemberDetail = targetEvent.getEventMemberDetail(hostPerson.getObjectId());
        EventMemberDetail targetEventMemberDetailInProgress = AppActionImplHelper.getBaseEventMemberDetail(targetEventMemberDetail);

        List<MemberProposedTimestamp> proposeEventTimestampsAsMember = AppActionImplHelper.proposeEventTimestampsAsMember(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        targetEventMemberDetailInProgress.setProposedTimestamps(proposeEventTimestampsAsMember);

        // TODO[test]: get leaderId
        String leaderId = targetEventMemberDetail.getLeaderId();

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

        try {
            api.publishEventChannelMemberProposedTimestamps(eventId, hostPerson.getObjectId(), leaderId, proposeEventTimestampsAsMember);
        } catch (BackendlessException e) {
            e.printStackTrace();
        }
    }

    // TODO[test]
    @Override
    public void selectEventTimestampsAsMember(final String eventId, final List<String> selectedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {

        final Event targetEvent = hostPerson.getEventAsMember(eventId);
        final EventMemberDetail targetEventMemberDetail = targetEvent.getEventMemberDetail(hostPerson.getObjectId());
        EventMemberDetail targetEventMemberDetailInProgress = AppActionImplHelper.getBaseEventMemberDetail(targetEventMemberDetail);

        List<MemberSelectedTimestamp> selectEventTimestampsAsMember = AppActionImplHelper.selectEventTimestampsAsMember(eventId, hostPerson.getObjectId(), selectedEventTimestamps);
        targetEventMemberDetailInProgress.setSelectedTimestamps(selectEventTimestampsAsMember);

        // TODO[test]: get leaderId
        String leaderId = targetEventMemberDetail.getLeaderId();

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

        try {
            api.publishEventChannelMemberSelectedTimestamps(eventId, hostPerson.getObjectId(), leaderId, selectEventTimestampsAsMember);
        } catch (BackendlessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void acceptEvent(final String eventId, final String leaderId, final ActionCallbackListener<Boolean> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Bundle bundle = msg.getData();
                Person updatedPerson = (Person) bundle.getSerializable(Person.SERIALIZE_KEY);
                Event acceptedEvent = updatedPerson.getEventsAsMember().get(0);
                boolean isSuccess = hostPerson.getEventsAsMember().add(acceptedEvent);
                if (isSuccess) {
                    EventUndecided undecidedEvent = hostPerson.getEventUndecided(eventId);
                    hostPerson.getEventsUndecided().remove(undecidedEvent);
                    listener.onSuccess(true);
                } else {
                    listener.onFailure(String.valueOf(R.string.local_update_error));
                }
                return isSuccess;
            }
        });

        Person baseMember = AppActionImplHelper.getBasePerson(hostPerson);
        String memberId = hostPerson.getObjectId();
        AcceptEventAsyncTask acceptTask = new AcceptEventAsyncTask(api, listener, handler, baseMember, eventId, memberId);
        acceptTask.execute();

        // Handle messaging
        final EventChannelMessageMemberResponder channelMsgMemberResponder = new EventChannelMessageMemberResponder(memberId);
        AsyncCallback<Subscription> subscriptionResponder = new AsyncCallback<Subscription>() {
            @Override
            public void handleResponse(Subscription response) {
                addToChannelMap(eventId, channelMsgMemberResponder, response);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
            }
        };

        try {
            api.registerEventChannelMessaging(eventId);
            api.subscribeEventChannel(eventId, memberId, channelMsgMemberResponder, subscriptionResponder);
            api.publishEventChannelMemberStatus(eventId, memberId, leaderId, StatusMember.Accept.getStatus());
        } catch (BackendlessException e) {
            e.printStackTrace();
        }

        // TODO: TIMER!!!
        //timer.cancel();
    }

    @Override
    public void declineEvent(final String eventId, final String leaderId, final ActionCallbackListener<Boolean> listener) {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                EventUndecided undecidedEvent = hostPerson.getEventUndecided(eventId);
                hostPerson.getEventsUndecided().remove(undecidedEvent);
                listener.onSuccess(true);
                return false;
            }
        });

        Person baseMember = AppActionImplHelper.getBasePerson(hostPerson);
        String memberId = hostPerson.getObjectId();
        DeclineEventAsyncTask declineEvent = new DeclineEventAsyncTask(api, listener, handler, baseMember, eventId, memberId);
        declineEvent.execute();

        // Handle messaging
        try {
            api.publishEventChannelMemberStatus(eventId, memberId, leaderId, StatusMember.Declined.getStatus());
        } catch (BackendlessException e) {
            e.printStackTrace();
        }

        // TODO: TIMER!!!
        //timer.cancel();
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
                assert syncedPerson != null;

                if (syncedPerson.getEventsAsLeader() == null) {
                    syncedPerson.setEventsAsLeader(new ArrayList<Event>());
                }
                if (syncedPerson.getEventsAsMember() == null) {
                    syncedPerson.setEventsAsMember(new ArrayList<Event>());
                }
                if (syncedPerson.getEventsUndecided() == null) {
                    syncedPerson.setEventsUndecided(new ArrayList<EventUndecided>());
                }

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
                appAction.declineEvent(null, hostPerson.getObjectId(), null);
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 30 * 60 * 1000);
    }

}
