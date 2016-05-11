package edu.scu.core.task;

import android.os.Handler;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.AppAction;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class LogoutAsyncTask extends BaseAsyncTask {

    private AppAction appAction;

    public LogoutAsyncTask(Api api, ActionCallbackListener listener, Handler handler, AppAction appAction) {
        super(api, listener, handler);
        this.appAction = appAction;
    }

    @Override
    protected ApiResponse<Void> doInBackground(Object... params) {
        return api.logout();
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                // TODO: Make sure the next line is valid here
                appAction.setHostUserId(null);
                listener.onSuccess(null);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
