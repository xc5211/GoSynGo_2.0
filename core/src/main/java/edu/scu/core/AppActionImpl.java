package edu.scu.core;

import android.content.Context;
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
import java.util.HashMap;
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
import edu.scu.core.task.CheckInEventAsyncTask;
import edu.scu.core.task.DeclineEventAsyncTask;
import edu.scu.core.task.InitiateEventAsyncTask;
import edu.scu.core.task.LoginAsyncTask;
import edu.scu.core.task.LogoutAsyncTask;
import edu.scu.core.task.ProposeEventAsyncTask;
import edu.scu.core.task.ProposeEventTimestampsAsLeaderAsyncTask;
import edu.scu.core.task.ProposeEventTimestampsAsMemberAsyncTask;
import edu.scu.core.task.RegisterAsyncTask;
import edu.scu.core.task.RegisterDeviceAsyncTask;
import edu.scu.core.task.RemoveEventMemberAsyncTask;
import edu.scu.core.task.SendEventInvitationAsyncTask;
import edu.scu.core.task.SetMinsToArriveAsMemberAsyncTask;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/14/16.
 */
public class AppActionImpl implements AppAction {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static String hostUserId;
    private static Person hostPerson;

    private Context context;
    private Api api;

    private Map<String, Timer> memberInvitationTimerMap;

    public AppActionImpl (Context context) {
        this.context = context;
        this.api = new ApiImpl();
        this.memberInvitationTimerMap = new HashMap<>();
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


        RegisterAsyncTask registerTask = new RegisterAsyncTask(api, listener, hostPerson, userEmail, password, firstName, lastName);
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


        LoginAsyncTask loginAsyncTask = new LoginAsyncTask(api, listener, hostPerson, this, userEmail, password, stayLoggedIn);
        loginAsyncTask.execute();

        RegisterDeviceAsyncTask registerDeviceAsyncTask = new RegisterDeviceAsyncTask(api, null, null);
        registerDeviceAsyncTask.execute();
    }

    @Override
    public void logout(final ActionCallbackListener<Void> listener) {
        LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask(api, listener, hostPerson, this);
        logoutAsyncTask.execute();
    }

    @Override
    public void getMonthlyScheduledDates(final ActionCallbackListener<List<Date>> actionCallbackListener) {

    }

    @Override
    public void isGoogleCalendarImported(final ActionCallbackListener<Boolean> listener) {

    }

    // TODO[later]
    @Override
    public void importGoogleCalendar(final ActionCallbackListener<Void> listener) {

    }

    @Override
    public void getScheduledDates(final ActionCallbackListener<List<Date>> listener) {

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

        ProposeEventAsyncTask proposeEventAsyncTask = new ProposeEventAsyncTask(api, listener, memberMsgResponder, hostPerson);
        proposeEventAsyncTask.execute();
    }

    @Override
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<EventMemberDetail> listener) {
        AddEventMemberAsyncTask addEventMemberAsyncTask = new AddEventMemberAsyncTask(api, listener, hostPerson, eventId, memberEmail);
        addEventMemberAsyncTask.execute();
    }

    @Override
    public void getAllEventMembers(final String eventId, final ActionCallbackListener<List<Person>> listener) {

    }

    @Override
    public void removeEventMember(final String eventId, final String memberId, final ActionCallbackListener<Event> listener) {
        RemoveEventMemberAsyncTask removeEventMemberAsyncTask = new RemoveEventMemberAsyncTask(api, listener, hostPerson, eventId, memberId);
        removeEventMemberAsyncTask.execute();
    }

    @Override
    public void addEventInformation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, ActionCallbackListener<Event> listener) {
        AddEventInformationAsyncTask addEventInformationAsyncTask = new AddEventInformationAsyncTask(api, listener, hostPerson, eventId, title, location, durationInMin, hasReminder, reminderInMin);
        addEventInformationAsyncTask.execute();
    }

    @Override
    public void sendEventInvitation(final String eventId, final ActionCallbackListener<Event> listener) {
        SendEventInvitationAsyncTask sendEventInvitationAsyncTask = new SendEventInvitationAsyncTask(api, listener, hostPerson, eventId);
        sendEventInvitationAsyncTask.execute();
    }

    @Override
    public void initiateEvent(final String eventId, final ActionCallbackListener<Integer> listener, final Date eventFinalTimestamp) {
        InitiateEventAsyncTask initiateEvent = new InitiateEventAsyncTask(api, listener, hostPerson, eventId, eventFinalTimestamp);
        initiateEvent.execute();
    }

    @Override
    public void cancelEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
        CancelEventAsyncTask cancelEventAsyncTask = new CancelEventAsyncTask(api, listener, hostPerson, eventId);
        cancelEventAsyncTask.execute();
    }

    @Override
    public void getAllEventMembersStatusAndEstimate(final String eventId, final ActionCallbackListener<Map<Person, Integer>> lisener) {

    }

    @Override
    public void proposeEventTimestampsAsLeader(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventLeaderDetail> listener) {
        List<LeaderProposedTimestamp> proposeEventTimestampsAsLeader = proposeEventTimestampsAsLeader(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        ProposeEventTimestampsAsLeaderAsyncTask proposeEventTimestampsAsLeaderAsyncTask = new ProposeEventTimestampsAsLeaderAsyncTask(api, listener, hostPerson, eventId, proposeEventTimestampsAsLeader);
        proposeEventTimestampsAsLeaderAsyncTask.execute();
    }

    @Override
    public void proposeEventTimestampsAsMember(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {
        List<MemberProposedTimestamp> proposeEventTimestampsAsMember = proposeEventTimestampsAsMember(eventId, hostPerson.getObjectId(), proposedEventTimestamps);
        ProposeEventTimestampsAsMemberAsyncTask proposeEventTimestampsAsMemberAsyncTask = new ProposeEventTimestampsAsMemberAsyncTask(api, listener, hostPerson, eventId, proposeEventTimestampsAsMember);
        proposeEventTimestampsAsMemberAsyncTask.execute();
    }

    // TODO[Hairong]
    @Override
    public void selectEventTimestampsAsMember(final String eventId, final List<String> selectedEventTimestamps, final ActionCallbackListener<Event> listener) {

    }

    @Override
    public void acceptEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {

        AsyncCallback<List<Message>> leaderMsgResponder = new AsyncCallback<List<Message>>() {
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

        AcceptEventAsyncTask acceptTask = new AcceptEventAsyncTask(api, listener, leaderMsgResponder, hostPerson, eventId);
        acceptTask.execute();

        Timer timer = memberInvitationTimerMap.get(eventId);
        timer.cancel();
        memberInvitationTimerMap.remove(eventId);
    }

    @Override
    public void declineEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
        DeclineEventAsyncTask declineEvent = new DeclineEventAsyncTask(api, listener, hostPerson, eventId);
        declineEvent.execute();

        memberInvitationTimerMap.remove(eventId);
    }

    @Override
    public void checkInEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
        CheckInEventAsyncTask checkInEventAsyncTask = new CheckInEventAsyncTask(api, listener, hostPerson, eventId);
        checkInEventAsyncTask.execute();
    }

    @Override
    public void setMinsToArriveAsMember(final String eventId, final int estimateInMin, final ActionCallbackListener<Integer> listener) {
        SetMinsToArriveAsMemberAsyncTask setMinsToArriveAsMember = new SetMinsToArriveAsMemberAsyncTask(api, listener, hostPerson, eventId, estimateInMin);
        setMinsToArriveAsMember.execute();
    }

    @Override
    public void getEventStatus(final String eventId, final ActionCallbackListener<Integer> listener) {

    }

    @Override
    public void getEventLeader(final String eventId, final ActionCallbackListener<Person> listener) {

    }

    @Override
    public void getEventLocation(final String eventId, final ActionCallbackListener<String> listener) {

    }

    @Override
    public void getEventDurationInMin(final String eventId, final ActionCallbackListener<Integer> listener) {

    }

    public void getEventsAsLeader(final ActionCallbackListener<List<Event>> listener) {

    }

    @Override
    public void getEventsAsMember(final ActionCallbackListener<List<Event>> listener) {

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
        memberInvitationTimerMap.put(eventId, timer);
    }

}
