package edu.scu.core;

import java.util.Date;
import java.util.List;

import edu.scu.model.Event;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/14/16.
 */
public interface AppAction {

    // As user
    public void register(final String userEmail, final String password, final String name, final ActionCallbackListener<String> listener);
    public void validateLogin(final ActionCallbackListener<Void> listener);
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<String> listener);
    public void logout(final ActionCallbackListener<Void> listener);
    public void getMonthlyScheduledDates(final ActionCallbackListener<List<Date>> listener);
    public void getEventsAsLeader(final ActionCallbackListener<List<Event>> listener);
    public void getEventsAsMember(final ActionCallbackListener<List<Event>> listener);


    // As leader
    public void proposeEvent(String leaderId, ActionCallbackListener<String> listener);
    public void addEventMember(final String leaderId, final String eventId, final String memberEmail, final ActionCallbackListener<String> listener);
    public void getAllEventMembers(final String eventId, final ActionCallbackListener<List<Person>> listener);

    // As member

    // Event - Shared to both leader and member

}
