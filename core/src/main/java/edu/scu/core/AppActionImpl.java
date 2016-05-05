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
import edu.scu.model.Event;
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


        AsyncTask<Void, Void, ApiResponse<Person>> asyncTask = new AsyncTask<Void, Void, ApiResponse<Person>>() {

            @Override
            protected ApiResponse<Person> doInBackground(Void... params) {
                return api.register(userEmail, password, firstName, lastName);
            }

            @Override
            protected void onPostExecute(ApiResponse<Person> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        hostPerson = response.getObj();
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


        AsyncTask<Void, Void, ApiResponse<String>> asyncTask = new AsyncTask<Void, Void, ApiResponse<String>>() {

            @Override
            protected ApiResponse<String> doInBackground(Void... params) {
                return api.login(userEmail, password, stayLoggedIn);
            }

            @Override
            protected void onPostExecute(ApiResponse<String> response) {
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
    public void logout(final ActionCallbackListener<Void> listener) {

        AsyncTask<Void, Void, ApiResponse<Void>> asyncTask = new AsyncTask<Void, Void, ApiResponse<Void>>() {

            @Override
            protected ApiResponse<Void> doInBackground(Void... params) {
                return api.logout();
            }

            @Override
            protected void onPostExecute(ApiResponse<Void> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(null);
                    } else {
                        listener.onFailure(response.getMsg());
                    }
                }
            }

        };
        asyncTask.execute();
    }

    @Override
    public void getMonthlyScheduledDates(final ActionCallbackListener<List<Date>> actionCallbackListener) {

    }

    // TODO[Hairong]
    @Override
    public void isGoogleCalendarImported(ActionCallbackListener<Boolean> listener) {

    }

    @Override
    public void importGoogleCalendar(ActionCallbackListener<Void> listener) {

    }

    @Override
    public void getScheduledDates(String personId) {

    }

    @Override
    public void proposeEvent(final ActionCallbackListener<Event> listener) {
        AsyncTask<Void, Void, ApiResponse<Event>> asyncTask = new AsyncTask<Void, Void, ApiResponse<Event>>() {

            @Override
            protected ApiResponse<Event> doInBackground(Void... params) {
                return api.proposeEvent(hostUserId);
            }

            @Override
            protected void onPostExecute(ApiResponse<Event> response) {
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
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<Event> listener) {

    }

    @Override
    public void getAllEventMembers(final String eventId, final ActionCallbackListener<List<Person>> listener) {
        AsyncTask<Void, Void, ApiResponse<List<Person>>> asyncTask = new AsyncTask<Void, Void, ApiResponse<List<Person>>>() {

            @Override
            protected ApiResponse<List<Person>> doInBackground(Void... params) {
                // TODO: Fix argument in the next line
                return api.getAllEventMembers("");
            }

            @Override
            protected void onPostExecute(ApiResponse<List<Person>> response) {
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

    // TODO[Hairong]
    @Override
    public void removeEventMember(String eventId, String memberId, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void sendEventInvitation(String eventId, String title, String location, int durationInMin, boolean hasReminder, int reminderInMin, List<LeaderProposedTimestamp> proposedTimestamps, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void initiateEvent(String eventId, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void cancelEvent(String eventId, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void getAllEventMembersStatusAndEstimate(String eventId, ActionCallbackListener<Map<Person, Integer>> lisener) {

    }

    @Override
    public void proposeEventTimestampsAsLeader(String eventId, List<LeaderProposedTimestamp> proposedEventTimestamps, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void proposeEventTimestampsAsMember(String memberId, String eventId, List<MemberProposedTimestamp> proposedEventTimestamps, ActionCallbackListener<Event> listener) {

    }

    // TODO[Hairong]
    @Override
    public void selectEventTimestamps(String memberId, String eventId, List<MemberSelectedTimestamp> selectedEventTimestamps, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void acceptEvent(String memberId, String eventId, ActionCallbackListener<Event> listener) {

    }

    // TODO[Hairong]
    @Override
    public void declineEvent(String memberId, String eventId, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void checkInEvent(String memberId, String eventId, ActionCallbackListener<Event> listener) {

    }

    // TODO[Hairong]
    @Override
    public void setMinsToArriveAsMember(String memberId, String eventId, int estimateInMin, ActionCallbackListener<Event> listener) {

    }

    @Override
    public void getEventStatus(String eventId, ActionCallbackListener<Integer> listner) {

    }

    @Override
    public void getEventLeader(String eventId, ActionCallbackListener<Person> listner) {

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
    public void getEventDurationInMin(String eventId, ActionCallbackListener<Integer> listener) {

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
