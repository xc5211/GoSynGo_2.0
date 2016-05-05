package edu.scu.api;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;
import edu.scu.model.StatusMember;
import edu.scu.api.query.BackendlessQueryHelper;

/**
 * Created by chuanxu on 4/13/16.
 */
public class ApiImpl implements Api {

    private final static String SUCCESS_EVENT = "0";
    private final static String FAIL_EVENT = "1";

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    // TODO[Later]: Delete after development
    private final static String idPersonChuan = "43501AA8-843D-113C-FF5A-7F015D78F300";
    private final static String idPersonSichao = "9F289070-D392-2C21-FF7F-2D20409CA400";
    private final static String idPersonHairong = "28E7440B-B6F3-8715-FF64-0F08AF049400";
    private final static String idUserChuan = "FA3F28DA-362E-3492-FF03-18E2DE3E2400";
    private final static String idUserSichao = "C91D6698-A1D0-0A85-FF20-9A8475F93600";
    private final static String idUserHairong = "F6651BEB-EDF4-65BD-FFA3-8CD20C756C00";


    private static List<LeaderProposedTimestamp> proposeEventTimestampsAsLeader(List<String> datesInString) {
//        String dateInString = "2016/10/15 16:00:00";

        List<LeaderProposedTimestamp> leaderProposedTimestamps = new ArrayList<>();
        try {
            Date date;
            for (String dateInString : datesInString) {
                date = sdf.parse(dateInString);
                LeaderProposedTimestamp leaderProposedTimestamp = new LeaderProposedTimestamp();
                leaderProposedTimestamp.setTimestamp(date);
                leaderProposedTimestamps.add(leaderProposedTimestamp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return leaderProposedTimestamps;
    }

    private static List<MemberProposedTimestamp> proposeEventTimestampsAsMember(List<String> datesInString) {
//        String dateInString = "2016/10/15 16:00:00";

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
        person.setEmail(user.getEmail());
        person.setFirstName((String) user.getProperty("firstName"));
        person.setLastName((String) user.getProperty("lastName"));
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
        BackendlessUser user = null;
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

    @Override
    public ApiResponse<Boolean> isGoogleCalendarImported(String personId) {
        // Get person
        Person person = null;
        try{
            person = Backendless.Data.of(Person.class).findById(personId);
        }
        catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code:" + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "", person.getIsGoogleCalendarImported());
    }

    // TODO[later]
    @Override
    public ApiResponse<Void> importGoogleCalendar(String personId) {
        return null;
    }

    @Override
    public ApiResponse<List<Date>> getScheduledDates(String personId) {

        // Get events as member
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventsAsMember(personId));
        BackendlessCollection<Event> result = null;
        try {
            result = Backendless.Data.of(Event.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        List<Event> scheduledEventsAsMember = result.getData();

        // Get events as leader
        dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventsAsLeader(personId));
        try {
            result = Backendless.Data.of(Event.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        List<Event> scheduledEventsAsLeader = result.getData();

        List<Date> scheduledDates = new ArrayList<>();
        for (Event eventAsMember : scheduledEventsAsMember) {
            scheduledDates.add(eventAsMember.getTimestamp());
        }
        for (Event eventsAsLeader : scheduledEventsAsLeader) {
            scheduledDates.add(eventsAsLeader.getTimestamp());
        }
        return new ApiResponse<List<Date>>(SUCCESS_EVENT, "Get scheduled dates success", scheduledDates);
    }

    @Override
    public ApiResponse<List<Event>> getEventsAsLeader(String personId) {

        // TODO[later]: Remove next line after testing
        personId = idPersonChuan;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventsAsLeader(personId));
        BackendlessCollection<Event> result = null;
        try {
            result = Backendless.Data.of(Event.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        List<Event> eventsAsLeader = result.getData();
        return new ApiResponse<List<Event>>(SUCCESS_EVENT, "", eventsAsLeader);
    }

    @Override
    public ApiResponse<List<Event>> getEventsAsMember(String personId) {

        // TODO[later]: Remove next line after testing
        personId = idPersonSichao;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventsAsMember(personId));
        BackendlessCollection<Event> result = null;
        try {
            result = Backendless.Data.of(Event.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        List<Event> eventsAsMember = result.getData();
        return new ApiResponse<List<Event>>(SUCCESS_EVENT, "", eventsAsMember);
    }

    @Override
    public ApiResponse<Event> proposeEvent(String leaderId) {

        // Get leader
        Person leader = null;
        try {
            leader = Backendless.Data.of( Person.class ).findById(leaderId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Propose new event
        EventLeaderDetail eventLeaderDetail = new EventLeaderDetail();
        eventLeaderDetail.setLeader(leader);
        eventLeaderDetail.setIsCheckedIn(false);

        Event event = new Event();
        event.setStatusEvent(StatusEvent.Tentative.getStatus());
        event.setEventLeaderDetail(eventLeaderDetail);

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Propose event success", event);
    }

    @Override
    public ApiResponse<Event> addEventMember(String eventId, String memberEmail) {

        // Get event
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Get member
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

        // Add member into event
        Person member = result.getData().get(0);
        EventMemberDetail eventMemberDetail = new EventMemberDetail();
        eventMemberDetail.setMember(member);
        eventMemberDetail.setStatusMember(StatusMember.Pending.getStatus());
        event.getEventMemberDetail().add(eventMemberDetail);
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Add event member success", event);
    }

    @Override
    public ApiResponse<Event> removeEventMember(String eventId, String memberId) {
        // Get event
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Get EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member doesn't exist");
        }

        //Delete member from the event
        EventMemberDetail eventMemberDetail = result.getData().get(0);
        event.getEventMemberDetail().remove(eventMemberDetail);
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Remove event member success", event);
    }

    @Override
    public ApiResponse<Event> sendEventInvitation(String eventId, String title, String location, int durationInMin, boolean hasReminder, int reminderInMin, List<LeaderProposedTimestamp> proposedTimestamps) {

        Event event;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        event.setTitle(title);
        event.setLocation(location);
        event.setDurationInMin(durationInMin);
        event.setHasReminder(hasReminder);
        event.setReminderInMin(reminderInMin);
        // event.setProposedTimestamps(proposedTimestamps);
        event.getEventLeaderDetail().setProposedTimestamps(proposedTimestamps);
        event.setStatusEvent(StatusEvent.Pending.getStatus());
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Send invitation success", event);
    }

    @Override
    public ApiResponse<Event> initiateEvent(String eventId, Date eventTimestamp) {

        // Get target Event object
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Update event status
        event.setTimestamp(eventTimestamp);
        event.setStatusEvent(StatusEvent.Ready.getStatus());

        // Save target Event object
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Event Initiate", event);
    }

    @Override
    public ApiResponse<Event> cancelEvent(String eventId) {

        // Get target Event object
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Update event status
        event.setStatusEvent(StatusEvent.Cancelled.getStatus());

        // Save target Event object
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Event Initiate", event);
    }

    @Override
    public ApiResponse<Map<Person, Integer>> getAllEventMembersStatusAndEstimate(String eventId) {

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetails(eventId));
        BackendlessCollection<EventMemberDetail> result = null;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        List<EventMemberDetail> eventMemberDetails = result.getData();
        Map<Person, Integer> statusMap = new HashMap<>();
        for (EventMemberDetail memberDetail : eventMemberDetails) {
            statusMap.put(memberDetail.getMember(), memberDetail.getMinsToArrive());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "", statusMap);
    }

    @Override
    public ApiResponse<List<Person>> getAllEventMembers(String eventId) {

        // TODO[later]: Remove next line after testing
        eventId = "0ED31A48-13E1-2EDC-FFF8-F2EF8764AC00";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMembers(eventId));
        BackendlessCollection<Person> result = null;
        try {
            result = Backendless.Data.of(Person.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        List<Person> eventsAsMember = result.getData();
        return new ApiResponse<List<Person>>(SUCCESS_EVENT, "", eventsAsMember);
    }

    @Override
    public ApiResponse<EventLeaderDetail> proposeEventTimestampsAsLeader(String leaderId, String eventId, List<LeaderProposedTimestamp> proposedEventTimestamps) {

        // Get target EventLeaderDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventleaderDetail(eventId, leaderId));
        BackendlessCollection<EventLeaderDetail> result;
        try {
            result = Backendless.Data.of(EventLeaderDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member doesn't exist");
        }

        // Set leader proposed time
        EventLeaderDetail eventLeaderDetail = result.getData().get(0);
        eventLeaderDetail.setProposedTimestamps(proposedEventTimestamps);

        // Save
        try {
            eventLeaderDetail = Backendless.Data.of(EventLeaderDetail.class).save(eventLeaderDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Propose event time success", eventLeaderDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> proposeEventTimestampsAsMember(String memberId, String eventId, List<MemberProposedTimestamp> proposedEventTimestamps) {

        // Get target EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member doesn't exist");
        }

        // Set member proposed time
        EventMemberDetail eventMemberDetail = result.getData().get(0);
        eventMemberDetail.setProposedTimestamps(proposedEventTimestamps);

        // Save
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Propose event time success", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> selectEventTimestamps(String memberId, String eventId, List<MemberSelectedTimestamp> selectedEventTimestamps) {

        // Get target EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member doesn't exist");
        }

        // Set member selected time
        List<EventMemberDetail> eventMemberDetails = result.getData();
        EventMemberDetail eventMemberDetail = eventMemberDetails.get(0);
        eventMemberDetail.setSelectedTimestamps(selectedEventTimestamps);

        // Send back the event
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Select event time success", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> acceptEvent(String memberId, String eventId) {

        // Get target EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result = null;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member detail doesn't exist");
        }

        // Update member status
        EventMemberDetail eventMemberDetail = result.getData().get(0);
        eventMemberDetail.setStatusMember(StatusMember.Accept.getStatus());

        // Save
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Accepted event", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> declineEvent(String memberId, String eventId) {

        // Get target EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result = null;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if (result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member detail doesn't exist");
        }

        // Update member status
        EventMemberDetail eventMemberDetail = result.getData().get(0);
        eventMemberDetail.setStatusMember(StatusMember.Declined.getStatus());

        // Save
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Accepted event", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> checkInEvent(String memberId, String eventId) {

        // Get target EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result = null;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        if(result.getData().size() != 1) {
            return new ApiResponse<>(FAIL_EVENT, "Member detail doesn't exsit");
        }

        // Update member status
        EventMemberDetail eventMemberDetail = result.getData().get(0);
        eventMemberDetail.setIsCheckedIn(true);

        // Save
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Checked in event", eventMemberDetail);
    }

    @Override
    public ApiResponse<EventMemberDetail> setMinsToArriveAsMember(String memberId, String eventId, int estimateInMin) {

        // Get target EventMemberDetail object
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(BackendlessQueryHelper.queryEventMemberDetail(eventId, memberId));
        BackendlessCollection<EventMemberDetail> result;
        try {
            result = Backendless.Data.of(EventMemberDetail.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Update
        EventMemberDetail eventMemberDetail = result.getData().get(0);
        eventMemberDetail.setMinsToArrive(estimateInMin);

        // Send back the event
        try {
            eventMemberDetail = Backendless.Data.of(EventMemberDetail.class).save(eventMemberDetail);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Set estimate success", eventMemberDetail);
    }

    @Override
    public ApiResponse<Integer> getEventStatus(String eventId) {
        //get event
        Event event = null;
        try {
            event = Backendless.Data.of( Event.class ).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<> (SUCCESS_EVENT, "Status OK", event.getStatusEvent());
    }

    @Override
    public ApiResponse<Person> getEventLeader(String eventId) {
        //get event
        Event event = null;
        try {
            event = Backendless.Data.of( Event.class ).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<Person> (SUCCESS_EVENT, "Get event leader success", event.getEventLeaderDetail().getLeader());
    }

    @Override
    public ApiResponse<String> getEventLocation(String eventId) {
        //get event
        Event event = null;
        try {
            event = Backendless.Data.of( Event.class ).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<> (SUCCESS_EVENT, "Get event leader success", event.getLocation());
    }

    @Override
    public ApiResponse<Integer> getEventDurationInMin(String eventId) {
        //get event
        Event event = null;
        try {
            event = Backendless.Data.of( Event.class ).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<> (SUCCESS_EVENT,"Get event duration min success", event.getDurationInMin());
    }

}
