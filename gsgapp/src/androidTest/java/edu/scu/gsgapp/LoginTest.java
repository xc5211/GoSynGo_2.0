package edu.scu.gsgapp;

import android.app.Application;
import android.test.ApplicationTestCase;

import edu.scu.core.ActionCallbackListener;

/**
 * Created by chuanxu on 4/18/16.
 */
public class LoginTest extends ApplicationTestCase<Application> {

    private GsgApplication gsgApplication;

    public LoginTest() {
        super(Application.class);
        this.gsgApplication = new GsgApplication();

        loginTest();
    }

    private void loginTest() {
        loginSuccess();
        loginFail();
    }

    private void loginFail() {

    }

    private void loginSuccess() {
        this.gsgApplication.getAppAction().login("whairong2011@gmail.com", "test", false, new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                assert (data != null);
            }

            @Override
            public void onFailure(String message) {
                assert false;
            }
        });
    }

}
