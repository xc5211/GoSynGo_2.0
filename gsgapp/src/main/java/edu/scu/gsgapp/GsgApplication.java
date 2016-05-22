package edu.scu.gsgapp;

import android.app.Application;

import com.backendless.Backendless;

import edu.scu.core.AppAction;
import edu.scu.core.AppActionImpl;
import edu.scu.util.lib.BackendlessSettings;

/**
 * Created by chuanxu on 4/16/16.
 */
public class GsgApplication extends Application {

    private AppAction appAction;

    @Override
    public void onCreate() {
        super.onCreate();
        appAction = new AppActionImpl();

        // Init 3rd party API
        Backendless.initApp(this, BackendlessSettings.APP_ID, BackendlessSettings.SECRET_KEY_ANDROID, BackendlessSettings.APP_VERSION);
    }

    public AppAction getAppAction() {
        return this.appAction;
    }
}
