package edu.scu.gsgapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.scu.core.AppAction;
import edu.scu.gsgapp.GsgApplication;

/**
 * Created by chuanxu on 4/16/16.
 */
public abstract class GsgBaseActivity extends AppCompatActivity {

    public Context context;
    public GsgApplication gsgApplication;
    public AppAction appAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        gsgApplication = (GsgApplication) this.getApplication();
        appAction = gsgApplication.getAppAction();
    }

}
