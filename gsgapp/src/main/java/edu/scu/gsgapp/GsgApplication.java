package edu.scu.gsgapp;

import android.app.Application;

import edu.scu.core.AppAction;
import edu.scu.core.AppActionImpl;

/**
 * Created by chuanxu on 4/16/16.
 */
public class GsgApplication extends Application {

    private AppAction appAction;

    @Override
    public void onCreate() {
        super.onCreate();
        appAction = new AppActionImpl(this);
    }

    public AppAction getAppAction() {
        return this.appAction;
    }
}
