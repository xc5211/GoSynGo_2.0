package edu.scu.core;

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
 * Created by chuanxu on 4/14/16.
 */
public interface AppAction {

    public String getHostUserId();
    public Person getHostPerson();
    public void setHostUserId(String hostUserId);
    public void setHostPerson(Person person);

    /**
     * As user
     */
    public void register(final String userEmail, final String password, final String firstName, final String lastName, final ActionCallbackListener<Person> listener);
    public void validateLogin(final ActionCallbackListener<Void> listener);
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<String> listener);
    public void logout(final ActionCallbackListener<Void> listener);
    public void getMonthlyScheduledDates(final ActionCallbackListener<List<Date>> listener);
    public void isGoogleCalendarImported(final ActionCallbackListener<Boolean> listener);
    public void importGoogleCalendar(final ActionCallbackListener<Void> listener);
    public void getScheduledDates(final ActionCallbackListener<List<Date>> listener);
    public void getEventsAsLeader(final ActionCallbackListener<List<Event>> listener);
    public void getEventsAsMember(final ActionCallbackListener<List<Event>> listener);

    /**
     * As leader
     */
    public void proposeEvent(final ActionCallbackListener<Event> listener);
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<EventMemberDetail> listener);
    public void getAllEventMembers(final String eventId, final ActionCallbackListener<List<Person>> listener);
    public void removeEventMember(final String eventId, final String memberId, final ActionCallbackListener<Event> listener);
    public void addEventInformation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, final ActionCallbackListener<Event> listener);
    public void sendEventInvitation(final String eventId, final ActionCallbackListener<Event> listener);
    public void initiateEvent(final String eventId, final ActionCallbackListener<Integer> listener, final Date eventFinalTimestamp);
    public void cancelEvent(final String eventId, final ActionCallbackListener<Boolean> listener);
    public void getAllEventMembersStatusAndEstimate(final String eventId, final ActionCallbackListener<Map<Person, Integer>> lisener);
    public void proposeEventTimestampsAsLeader(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventLeaderDetail> listener);

    /**
     * As member
     */
    public void proposeEventTimestampsAsMember(final String eventId, final List<String> proposedEventTimestamps, final ActionCallbackListener<EventMemberDetail> listener);
    public void selectEventTimestampsAsMember(final String eventId, final List<String> selectedEventTimestamps, final ActionCallbackListener<Event> listener);
    public void acceptEvent(final String eventId, final ActionCallbackListener<Boolean> listener);
    public void declineEvent(final String eventId, final ActionCallbackListener<Boolean> listener);
    public void checkInEvent(final String eventId, final ActionCallbackListener<Boolean> listener);
    public void setMinsToArriveAsMember(final String eventId, final int estimateInMin, final ActionCallbackListener<Integer> listener);

    /**
     * Event - Shared to both leader and member
     */
    public void getEventStatus(final String eventId, final ActionCallbackListener<Integer> listener);
    public void getEventLeader(final String eventId, final ActionCallbackListener<Person> listener);
    public void getEventLocation(final String eventId, final ActionCallbackListener<String> listener);
    public void getEventDurationInMin(final String eventId, final ActionCallbackListener<Integer> listener);

    /**
     * Sync
     */
    public void syncHostInformation(final String userId, final ActionCallbackListener<Person> listener);

    /**
     * Timer
     */
    public void startMemberInvitationTimer(final AppAction appAction, final String eventId);

    /**
     * Publish/Subscribe
     */


}
