package edu.scu.api;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by chuanxu on 4/13/16.
 */
public class ApiImpl implements Api {

    private final static String SUCCESS_EVENT = "0";
    private final static String FAIL_EVENT = "1";

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
    public ApiResponse<Void> getEventsAsLeader(String userId) {
        return null;
    }

    @Override
    public ApiResponse<Void> getEventsAsMember(String userId) {
        return null;
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
    public ApiResponse<Void> getAllEventMembers(String leaderId, String eventId) {
        return null;
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
