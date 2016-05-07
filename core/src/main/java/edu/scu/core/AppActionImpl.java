package edu.scu.core;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.scu.api.Api;
import edu.scu.api.ApiImpl;
import edu.scu.api.ApiResponse;
import edu.scu.core.task.AcceptEventAsyncTask;
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
import edu.scu.core.task.AddEventInformationAsyncTask;
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

    private Context context;
    private Api api;

    private static String hostUserId;
    private static Person hostPerson;

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

        RegisterDeviceAsyncTask registerDeviceTask = new RegisterDeviceAsyncTask(api, null, null);
        registerDeviceTask.execute();
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
        ProposeEventAsyncTask proposeEventAsyncTask = new ProposeEventAsyncTask(api, listener, hostPerson);
        proposeEventAsyncTask.execute();
    }

    // TODO
    @Override
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<Event> listener) {

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
    public void proposeEventTimestampsAsLeader(final String eventId, final List<LeaderProposedTimestamp> proposedEventTimestamps, final ActionCallbackListener<EventLeaderDetail> listener) {
        ProposeEventTimestampsAsLeaderAsyncTask proposeEventTimestampsAsLeaderAsyncTask = new ProposeEventTimestampsAsLeaderAsyncTask(api, listener, hostPerson, eventId, proposedEventTimestamps);
        proposeEventTimestampsAsLeaderAsyncTask.execute();
    }

    @Override
    public void proposeEventTimestampsAsMember(final String eventId, final List<MemberProposedTimestamp> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener) {
        ProposeEventTimestampsAsMemberAsyncTask proposeEventTimestampsAsMemberAsyncTask = new ProposeEventTimestampsAsMemberAsyncTask(api, listener, hostPerson, eventId, proposedEventTimestamps);
        proposeEventTimestampsAsMemberAsyncTask.execute();
    }

    // TODO[Hairong]
    @Override
    public void selectEventTimestampsAsMember(final String eventId, final List<MemberSelectedTimestamp> selectedEventTimestamps, final ActionCallbackListener<Event> listener) {

    }

    @Override
    public void acceptEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
        AcceptEventAsyncTask acceptTask = new AcceptEventAsyncTask(api, listener, hostPerson, eventId);
        acceptTask.execute();
    }

    @Override
    public void declineEvent(final String eventId, final ActionCallbackListener<Boolean> listener) {
        DeclineEventAsyncTask declineEvent = new DeclineEventAsyncTask(api, listener, hostPerson, eventId);
        declineEvent.execute();
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
        AsyncTask<Void, Void, ApiResponse<String>> asyncTask = new AsyncTask<Void, Void, ApiResponse<String>>() {

            @Override
            protected ApiResponse<String> doInBackground(Void... params) {
                return api.getEventLocation(eventId);
            }

            @Override
            protected void onPostExecute(ApiResponse<String> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        //TODO:UPDATE
                        listener.onSuccess(response.getObj());
                    } else {
                        listener.onFailure(response.getMsg());
                    }
                }
            }

        };
        asyncTask.execute();
    }

    @Override
    public void getEventDurationInMin(final String eventId, final ActionCallbackListener<Integer> listener) {

    }

    public void getEventsAsLeader(final ActionCallbackListener<List<Event>> listener) {
        AsyncTask<Void, Void, ApiResponse<List<Event>>> asyncTask = new AsyncTask<Void, Void, ApiResponse<List<Event>>>() {

            @Override
            protected ApiResponse<List<Event>> doInBackground(Void... params) {
                // TODO: Fix argument in the next line
                return api.getEventsAsLeader("");
            }

            @Override
            protected void onPostExecute(ApiResponse<List<Event>> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response.getObj());
                    } else {
                        listener.onFailure(response.getMsg());
                    }
                }
            }

        };
        asyncTask.execute();
    }

    @Override
    public void getEventsAsMember(final ActionCallbackListener<List<Event>> listener) {
        AsyncTask<Void, Void, ApiResponse<List<Event>>> asyncTask = new AsyncTask<Void, Void, ApiResponse<List<Event>>>() {

            @Override
            protected ApiResponse<List<Event>> doInBackground(Void... params) {
                // TODO: Fix argument in the next line
                return api.getEventsAsMember("");
            }

            @Override
            protected void onPostExecute(ApiResponse<List<Event>> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response.getObj());
                    } else {
                        listener.onFailure(response.getMsg());
                    }
                }
            }

        };
        asyncTask.execute();
    }

}
