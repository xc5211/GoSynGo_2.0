package edu.scu.core.task;

import android.os.Handler;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class RegisterDeviceAsyncTask extends BaseAsyncTask {

    public RegisterDeviceAsyncTask(Api api, ActionCallbackListener listener, Handler handler) {
        super(api, listener, handler);
    }

    @Override
    protected ApiResponse<Void> doInBackground(Object... params) {
        return api.registerDevice();
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        // do nothing
    }

}
