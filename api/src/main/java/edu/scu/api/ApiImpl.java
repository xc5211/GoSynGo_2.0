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

    // TODO[Later]: Delete after development
    private final static String idChuan = "43501AA8-843D-113C-FF5A-7F015D78F300";
    private final static String idSichao = "9F289070-D392-2C21-FF7F-2D20409CA400";
    private final static String idHairong = "28E7440B-B6F3-8715-FF64-0F08AF049400";

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    @Override
    public ApiResponse<String> register(String userEmail, String password, String name) {

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
        return new ApiResponse<>(SUCCESS_EVENT, "You have successfully registered", user.getUserId());
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
    public ApiResponse<Void> isGoogleCalendarImported(String userId) {
        return null;
    }

    @Override
    public ApiResponse<Void> importGoogleCalendar(String userId) {
        return null;
    }

    @Override
    public ApiResponse<Void> getMonthlyScheduledDates(String userId) {
        return null;
    }

    @Override
    public ApiResponse<List<Event>> getEventsAsLeader(String userId) {

        // TODO[later]: Remove next line after testing
        String leaderObjectId = idChuan;
        StringBuilder whereClause = new StringBuilder();
        whereClause.append( "leader" );
        whereClause.append(".objectId = '").append(leaderObjectId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );
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
    public ApiResponse<List<Event>> getEventsAsMember(String userId) {

        // TODO[later]: Remove next line after testing
        String  personObjectId = idSichao;
        StringBuilder whereClause = new StringBuilder();
        whereClause.append( "eventMemberDetail" );
        whereClause.append( ".member" );
        whereClause.append( ".objectId = '" ).append( personObjectId ).append( "'" );

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );
        BackendlessCollection<Event> result = null;
        try {
            result = Backendless.Data.of( Event.class ).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        List<Event> eventsAsMember = result.getData();
        return new ApiResponse<List<Event>>(SUCCESS_EVENT, "", eventsAsMember);
    }

    @Override
    public ApiResponse<String> proposeEvent(String leaderId) {
        return null;
    }

    @Override
    public ApiResponse<Void> addEventMember(String leaderId, String eventId, String memberEmail) {
        return null;
    }

    @Override
    public ApiResponse<Void> removeEventMember(String leaderId, String eventId, String memberEmail) {
        return null;
    }

    @Override
    public ApiResponse<Void> sendEventInvitation(String leaderId, String name, int durationInMin, boolean hasReminder, int reminderInMin, Text location, List<Timestamp> proposedTimestamps) {
        return null;
    }

    @Override
    public ApiResponse<Void> initiateEvent(String leaderId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> cancelEvent(String leaderId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> getAllEventMembersStatusAndEstimate(String leaderId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<List<Person>> getAllEventMembers(String eventId) {

        // TODO[later]: Remove next line after testing
        String  eventObjectId = "260378B7-0725-107A-FF4A-F1EEA4768400";
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("eventsAsMember");
        whereClause.append( ".objectId = '" ).append(eventObjectId).append("'");

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );

        QueryOptions queryOptions = new QueryOptions();
        BackendlessCollection<Person> result = null;
        try {
            result = Backendless.Data.of( Person.class ).find(dataQuery);
        } catch (BackendlessException exception) {
            return new ApiResponse<>(FAIL_EVENT, "Error code: " + exception.getCode());
        }

        List<Person> eventsAsMember = result.getData();
        return new ApiResponse<List<Person>>(SUCCESS_EVENT, "", eventsAsMember);
    }

    @Override
    public ApiResponse<Void> proposeEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps) {
        return null;
    }

    @Override
    public ApiResponse<Void> selectEventTimestamps(String memberId, String eventId, List<Timestamp> proposedEventTimestamps) {
        return null;
    }

    @Override
    public ApiResponse<Void> acceptEvent(String memberId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> declineEvent(String memberId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> checkInEvent(String memberId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> estimateEvent(String memberId, String eventId, int estimateInMin) {
        return null;
    }

    @Override
    public ApiResponse<Void> getEventStatus(String userId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> getEventLeader(String userId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> getEventLocation(String userId, String eventId) {
        return null;
    }

    @Override
    public ApiResponse<Void> getEventDurationInMin(String userId, String eventId) {
        return null;
    }

}
