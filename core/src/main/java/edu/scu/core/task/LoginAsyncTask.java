package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.util.lib.GoogleProjectSettings;

/**
 * Created by chuanxu on 5/6/16.
 */
public class LoginAsyncTask extends BaseAsyncTask {

    private String userEmail;
    private String password;
    private boolean stayLoggedIn;

    public LoginAsyncTask(Api api, ActionCallbackListener listener, Handler handler, String userEmail, String password, boolean stayLoggedIn) {
        super(api, listener, handler);
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
                String userId = (String) response.getObj();

                // TODO: move next line to register async task
                api.registerEventChannelMessaging(GoogleProjectSettings.DEFAULT_CHANNEL);

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("userId", userId);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }
}
