package edu.scu.api;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.backendless.Backendless;

import edu.scu.util.lib.BackendlessSettings;

/**
 * Created by chuanxu on 4/16/16.
 */
public class RegisterTest extends ApplicationTestCase<Application> {

    String userEmail1 = "huangsichao2015@gmail.com";
    String password1 = "test";
    String name1 = "Sichao";

    String userEmail2 = "whairong2011@gmail.com";
    String password2 = "test";
    String name2 = "Hairong";

    Api api;

    public RegisterTest(Class<Application> applicationClass) {
        super(applicationClass);

        init();
        startTest();
    }

    private void startTest() {
        registerWithWrongEmailFormatTest();
        registerWithMissingPasswordTest();
        registerSuccessTest();
    }

    private void registerSuccessTest() {
        registerSuccess(userEmail1, password1, name1);
    }

    private void registerSuccess(String userEmail, String password, String name) {
        ApiResponse<String> response = api.register(userEmail, password, name);

        assert response.getObj() != null;
        assert response.getObj() instanceof String;
        assert response.getObjLists() == null;
        assert response.getEvent() == "0";
        assert response.getMsg() == "Registration is successful";
    }

    private void registerWithMissingPasswordTest() {
        registerWithMissingPassword(userEmail1, "", name1);
    }

    private void registerWithMissingPassword(String userEmail, String password, String name) {
        ApiResponse<String> response = api.register(userEmail, password, name);

        assert response.getObj() == null;
        assert response.getObjLists() == null;
        assert response.getEvent() == "1";
        assert response.getMsg() == "3011";
    }

    private void registerWithWrongEmailFormatTest() {
        String wrongEmail = "wrongformat@gmail";
        registerWithWrongEmailFormat(wrongEmail, password1, name1);
    }

    private void registerWithWrongEmailFormat(String userEmail, String password, String name) {
        ApiResponse<String> response = api.register(userEmail, password, name);

        assert response.getObj() == null;
        assert response.getObjLists() == null;
        assert response.getEvent() == "1";
        assert response.getMsg() == "3034";
    }

    private void init() {
        api = new ApiImpl();
        Backendless.initApp(this.getContext(), BackendlessSettings.APP_ID, BackendlessSettings.SECRET_KEY_ANDROID, BackendlessSettings.APP_VERSION);
    }

}
