package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.AppAction;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class LoginAsyncTask extends BaseAsyncTask {

    private AppAction appAction;
    private String userEmail;
    private String password;
    private boolean stayLoggedIn;

    public LoginAsyncTask(Api api, ActionCallbackListener listener, Person hostPerson, AppAction appAction, String userEmail, String password, boolean stayLoggedIn) {
        super(api, listener, hostPerson);
        this.appAction = appAction;
        this.userEmail = userEmail;
        this.password = password;
        this.stayLoggedIn = stayLoggedIn;
    }

    @Override
    protected ApiResponse<String> doInBackground(Object... params) {
        return api.login(userEmail, password, stayLoggedIn);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                appAction.setHostUserId((String) response.getObj());
                listener.onSuccess(response.getObj());
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }
}
