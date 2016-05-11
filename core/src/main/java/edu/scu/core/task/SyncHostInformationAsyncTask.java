package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/10/16.
 */
public class SyncHostInformationAsyncTask extends BaseAsyncTask {

    private String userId;

    public SyncHostInformationAsyncTask(Api api, ActionCallbackListener<Void> listener, Handler handler, String userId) {
        super(api, listener, handler);
        this.userId = userId;
    }

    @Override
    protected ApiResponse<Person> doInBackground(Object... params) {
        return api.syncHostInformation(userId);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Person syncedPerson = (Person) response.getObj();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Person.SERIALIZE_KEY, syncedPerson);
                message.setData(bundle);
                handler.sendMessage(message);
                listener.onSuccess(null);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
