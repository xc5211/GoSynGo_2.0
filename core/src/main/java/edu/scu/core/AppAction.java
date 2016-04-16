package edu.scu.core;

import java.util.Date;
import java.util.List;

/**
 * Created by chuanxu on 4/14/16.
 */
public interface AppAction {

    // As user
    public void register(String userEmail, String password, String name, ActionCallbackListener<String> listener);
    public void login(String userEmail, String password, boolean stayLoggedIn, ActionCallbackListener<Void> listener);
    public void getMonthlyScheduledDates(ActionCallbackListener<List<Date>> listener);


    // As leader

    // As member
}
