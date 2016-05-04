package edu.scu.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.scu.model.Event;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/14/16.
 */
public interface AppAction {

    /**
     * As user
     */
    public void register(final String userEmail, final String password, final String name, final ActionCallbackListener<Person> listener);
    public void validateLogin(final ActionCallbackListener<Void> listener);
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<String> listener);
    public void logout(final ActionCallbackListener<Void> listener);
    public void getMonthlyScheduledDates(final ActionCallbackListener<List<Date>> listener);
    public void isGoogleCalendarImported(final ActionCallbackListener<Boolean> listener);
    public void importGoogleCalendar(final ActionCallbackListener<Void> listener);
    public void getScheduledDates(final String personId);
    public void getEventsAsLeader(final ActionCallbackListener<List<Event>> listener);
    public void getEventsAsMember(final ActionCallbackListener<List<Event>> listener);

    /**
     * As leader
     */
    public void proposeEvent(ActionCallbackListener<Event> listener);
    public void addEventMember(final String eventId, final String memberEmail, final ActionCallbackListener<Event> listener);
    public void getAllEventMembers(final String eventId, final ActionCallbackListener<List<Person>> listener);
    public void removeEventMember(final String eventId, final String memberId, final ActionCallbackListener<Event> listener);
    public void sendEventInvitation(final String eventId, final String title, final String location, final int durationInMin, final boolean hasReminder, final int reminderInMin, final List<LeaderProposedTimestamp> proposedTimestamps, final ActionCallbackListener<Event> listener);
    public void initiateEvent(final String eventId, final ActionCallbackListener<Event> listener);
    public void cancelEvent(final String eventId, final ActionCallbackListener<Event> listener);
    public void getAllEventMembersStatusAndEstimate(final String eventId, final ActionCallbackListener<Map<Person, Integer>> lisener);
    public void proposeEventTimestampsAsLeader(final String eventId, final List<LeaderProposedTimestamp> proposedEventTimestamps, final ActionCallbackListener<Event> listener);

    /**
     * As member
     */
    public void proposeEventTimestampsAsMember(final String memberId, final String eventId, final List<MemberProposedTimestamp> proposedEventTimestamps, final ActionCallbackListener<Event> listener);
    public void selectEventTimestamps(final String memberId, final String eventId, final List<MemberSelectedTimestamp> selectedEventTimestamps, final ActionCallbackListener<Event> listener);
    public void acceptEvent(final String memberId, final String eventId, final ActionCallbackListener<Event> listener);
    public void declineEvent(final String memberId, final String eventId, final ActionCallbackListener<Event> listener);
    public void checkInEvent(final String memberId, final String eventId, final ActionCallbackListener<Event> listener);
    public void setMinsToArriveAsMember(final String memberId, final String eventId, final int estimateInMin, final ActionCallbackListener<Event> listener);

    /**
     * Event - Shared to both leader and member
     */
    public void getEventStatus(final String eventId, final ActionCallbackListener<Integer> listner);
    public void getEventLeader(final String eventId, final ActionCallbackListener<Person> listner);
    public void getEventLocation(final String eventId, final ActionCallbackListener<String> listner);
    public void getEventDurationInMin(final String eventId, final ActionCallbackListener<Integer> listner);

}
