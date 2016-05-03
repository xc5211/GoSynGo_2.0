package edu.scu.api;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;
import edu.scu.model.StatusMember;

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
    private final static String getIdPersonHairong = "F6651BEB-EDF4-65BD-FFA3-8CD20C756C00";


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
    public ApiResponse<Person> register(String userEmail, String password, String name) {

        // Register
        BackendlessUser user = new BackendlessUser();
        user.setEmail(userEmail);
        user.setPassword(password);
        user.setProperty("name", name);
        user.setProperty("isGoogleCalendarImported", false);
        try {
            user = Backendless.UserService.register(user);
        } catch(BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Create Person object
        Person person = new Person();
        person.setUserId(user.getObjectId());
        person.setEmail(user.getEmail());
        person.setName((String) user.getProperty("name"));
        person.setIsGoogleCalendarImported((boolean) user.getProperty("isGoogleCalendarImported"));
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

    // TODO[Hairong]
    @Override
    public ApiResponse<Boolean> isGoogleCalendarImported(String personId) {
        return null;
    }

    @Override
    public ApiResponse<Void> importGoogleCalendar(String personId) {
        return null;
    }

    @Override
    public ApiResponse<List<Date>> getMonthlyScheduledDates(String personId) {
        return null;
    }

    @Override
    public ApiResponse<List<Event>> getEventsAsLeader(String personId) {

        // TODO: Change whereClause to match argument userId instead of personObjectId used for testing
        // TODO[later]: Remove next line after testing
        personId = idPersonChuan;
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventLeaderDetail");
        whereClause.append(".leader");
        whereClause.append(".objectId = '").append(personId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause.toString());
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

        // TODO: Change whereClause to match argument userId instead of personObjectId used for testing
        // TODO[later]: Remove next line after testing
        personId = idPersonSichao;
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventMemberDetail");
        whereClause.append(".member");
        whereClause.append(".objectId = '").append(personId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause.toString());
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
    public ApiResponse<Event> addEventMember(String leaderId, String eventId, String memberEmail) {

        // Get event
        Event event = null;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Get member
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("email = '").append(memberEmail).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );

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
        //eventMemberDetail.setMemberObjectId(member.getObjectId());
        eventMemberDetail.setStatusMember(StatusMember.Pending.getStatus());

        List<EventMemberDetail> eventMemberDetails = new ArrayList<>();
        eventMemberDetails.add(eventMemberDetail);
        event.setEventMemberDetail(eventMemberDetails);
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        return new ApiResponse<>(SUCCESS_EVENT, "Add event member success", event);
    }

    // TODO[Hairong]
    @Override
    public ApiResponse<Event> removeEventMember(String leaderId, String eventId, String memberId) {
        return null;
    }

    @Override
    public ApiResponse<Event> sendEventInvitation(String leaderId, String title, int durationInMin, boolean hasReminder, int reminderInMin, Text location, List<Timestamp> proposedTimestamps) {
        return null;
    }

    @Override
    public ApiResponse<Event> initiateEvent(String leaderId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> cancelEvent(String leaderId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Integer> getAllEventMembersStatusAndEstimate(String leaderId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<List<Person>> getAllEventMembers(String eventId) {

        // TODO[later]: Remove next line after testing
        eventId = "260378B7-0725-107A-FF4A-F1EEA4768400";
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventsAsMember");
        whereClause.append(".objectId = '").append(eventId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );

        QueryOptions queryOptions = new QueryOptions();
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
    public ApiResponse<Event> proposeEventTimestampsAsLeader(String eventId, List<Timestamp> proposedEventTimestamps) {

        // TODO: convert parameter to fit local call
        List<LeaderProposedTimestamp> leaderProposedTimestamps = proposeEventTimestampsAsLeader(null);

        StringBuilder whereClause = new StringBuilder();
        whereClause.append("objectId = '").append(eventId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause.toString());
        BackendlessCollection<Event> result = null;
        try {
            result = Backendless.Data.of(Event.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        Event event = result.getData().get(0);
        event.setProposedTimestamps(leaderProposedTimestamps);

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        return new ApiResponse<>(SUCCESS_EVENT, "Propose event time success", event);
    }

    @Override
    public ApiResponse<Event> proposeEventTimestampsAsMember(String memberId, String eventId, List<Timestamp> proposedEventTimestamps) {

        // TODO: convert parameter to fit local call
        List<MemberProposedTimestamp> memberProposedTimestamps = proposeEventTimestampsAsMember(null);

        StringBuilder whereClause = new StringBuilder();
        whereClause.append("objectId = '").append(eventId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause.toString());
        BackendlessCollection<Event> result = null;
        try {
            result = Backendless.Data.of(Event.class).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        Event event = result.getData().get(0);
        for (EventMemberDetail memberDetail : event.getEventMemberDetail()) {
            if (memberDetail.getMember().getObjectId().equals(memberId)) {
                memberDetail.setProposedTimestamps(memberProposedTimestamps);
                break;
            }
        }

        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        return new ApiResponse<>(SUCCESS_EVENT, "Propose event time success", event);
    }

    // TODO[Hairong]
    @Override
    public ApiResponse<Event> selectEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps) {
        return null;
    }

    @Override
    public ApiResponse<Event> acceptEvent(String memberId, String eventId) {

        // Get target Event object
        Event event;
        try {
            event = Backendless.Data.of(Event.class).findById(eventId);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        // Update member status
        for (EventMemberDetail memberDetail : event.getEventMemberDetail()) {
            if (memberDetail.getMember().getObjectId().equals(memberId)) {
                memberDetail.setStatusMember(StatusMember.Accept.getStatus());
                break;
            }
        }

        // Save target Event object
        try {
            event = Backendless.Data.of(Event.class).save(event);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }
        return new ApiResponse<>(SUCCESS_EVENT, "Accepted event", event);
    }

    // TODO[Hairong]
    @Override
    public ApiResponse<Event> declineEvent(String memberId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Event> checkInEvent(String memberId, String eventId) {
        return null;
    }

    // TODO[Hairong]
    @Override
    public ApiResponse<Event> setMinsToArriveAsMember(String memberId, String eventId, int estimateInMin) {
        return null;
    }

    @Override
    public ApiResponse<StatusEvent> getEventStatus(String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Person> getEventLeader(String eventId) {
        return null;
    }

    @Override
    public ApiResponse<String> getEventLocation(String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Integer> getEventDurationInMin(String eventId) {
        return null;
    }

}
