package edu.scu.core;

import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.scu.core.task.messaging.ChannelSubscription;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.EventUndecided;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/14/16.
 */
public interface AppAction {

    /**
     * Core data
     */
    public String getHostUserId();
    public Person getHostPerson();
    public Map<String, ChannelSubscription> getChannelMap();
    public List<EventUndecided> getUndecidedEventList();
    public void setHostUserId(String hostUserId);
    public void setHostPerson(Person person);
    public void setChannelMap(Map<String, ChannelSubscription> channelMap);
    public void addToChannelMap(String channelName, AsyncCallback<List<Message>> channelMsgResponder, Subscription subscription);

    /**
     * As user
     */
    public void register(final String userEmail, final String password, final String firstName, final String lastName, final ActionCallbackListener<Person> listener);
    public void validateLogin(final ActionCallbackListener<Void> listener);
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<String> listener);
    public void logout(final ActionCallbackListener<Void> listener);
    public void importGoogleCalendar(final ActionCallbackListener<Void> listener);

    /**
     * As leader
     */
    public void proposeEvent(final ActionCallbackListener<Event> listener);
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<Event> listener);
    public void removeEventMember(final String eventId, final String memberId, final ActionCallbackListener<Event> listener);
    public void addEventInformation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, final String note, final ActionCallbackListener<Event> listener);
    public void sendEventInvitation(final String eventId, final ActionCallbackListener<Event> listener);
    public void initiateEvent(final String eventId, final ActionCallbackListener<Integer> listener, final Date eventFinalTimestamp);
    public void cancelEvent(final String eventId, final ActionCallbackListener<Boolean> listener);
    public void proposeEventTimestampsAsLeader(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventLeaderDetail> listener);

    /**
     * As member
     */
    public void proposeEventTimestampsAsMember(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener);
    public void selectEventTimestampsAsMember(final String eventId, final List<String> selectedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener);
    public void acceptEvent(final Event undecidedEvent, final String leaderId, final ActionCallbackListener<Boolean> listener);
    public void declineEvent(final Event undecidedEvent, final String leaderId, final ActionCallbackListener<Boolean> listener);
    public void checkInEvent(final String eventId, final ActionCallbackListener<Boolean> listener);
    public void setMinsToArriveAsMember(final String eventId, final int estimateInMin, final ActionCallbackListener<Integer> listener);

    /**
     * Sync
     */
    public void syncHostInformation(final String userId, final ActionCallbackListener<Void> listener);

    /**
     * Timer
     */
    public void startMemberInvitationTimer(final AppAction appAction, final String eventId);

    /**
     * Publish/Subscribe
     */


}
