package edu.scu.api;

import com.backendless.Backendless;

import org.junit.Before;
import org.junit.Test;

import edu.scu.util.lib.BackendlessSettings;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RegisterTest {

    private Api api = new ApiImpl();

    String userEmail1 = "huangsichao2015@gmail.com";
    String password1 = "test";
    String name1 = "Sichao";

    String userEmail2 = "whairong2011@gmail.com";
    String password2 = "test";
    String name2 = "Hairong";

    @Before
    public void before() throws Exception {
        Backendless.initApp(BackendlessSettings.APP_ID, BackendlessSettings.SECRET_KEY_ANDROID, BackendlessSettings.APP_VERSION);
    }

    @Test
    public void registerWithWrongEmailFormat() throws Exception {
        String wrongEmail = "wrongformat@gmail";
        registerWithWrongEmailFormat(wrongEmail, password1, name1);
    }

    @Test
    public void registerWithMissingPassword() throws Exception {
        registerWithMissingPassword(userEmail1, "", name1);
    }

    @Test
    public void registerSuccess() throws Exception {
        registerSuccess(userEmail1, password1, name1);
        //registerSuccess(userEmail2, password2, name2);
    }

    private void registerSuccess(String userEmail, String password, String name) {

        ApiResponse<String> response = api.register(userEmail, password, name);

        assert response.getObj() != null;
        assert response.getObj() instanceof String;
        assert response.getObjLists() == null;
        assert response.getEvent() == "0";
        assert response.getMsg() == "Registration is successful";
    }

    private void registerWithMissingPassword(String userEmail, String password, String name) {

        ApiResponse<String> response = api.register(userEmail, password, name);

        assert response.getObj() == null;
        assert response.getObjLists() == null;
        assert response.getEvent() == "1";
        assert response.getMsg() == "3011";
    }

    private void registerWithWrongEmailFormat(String userEmail, String password, String name) {

        ApiResponse<String> response = api.register(userEmail, password, name);

        assert response.getObj() == null;
        assert response.getObjLists() == null;
        assert response.getEvent() == "1";
        assert response.getMsg() == "3034";
    }

}
