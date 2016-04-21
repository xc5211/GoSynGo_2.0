package edu.scu.core;

import java.util.Date;
import java.util.List;

/**
 * Created by chuanxu on 4/14/16.
 */
public interface AppAction {

    // As user
    public void register(String userEmail, String password, String name, ActionCallbackListener<String> listener);
    public void validateLogin(ActionCallbackListener<Void> listener);
    public void login(String userEmail, String password, boolean stayLoggedIn, ActionCallbackListener<String> listener);
    public void logout(ActionCallbackListener<Void> listener);
    public void getMonthlyScheduledDates(ActionCallbackListener<List<Date>> listener);


    // As leader
    public void proposeEvent(String leaderId, ActionCallbackListener<String> listener);
    public void addEventMember(String leaderId, String eventId, String memberEmail, ActionCallbackListener<String> listener);

    // As member

    // Event - Shared to both leader and member

}
