package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/10/16.
 */
public class SyncHostInformationAsyncTask extends BaseAsyncTask {

    private String userId;

    public SyncHostInformationAsyncTask(Api api, ActionCallbackListener<Void> listener, Person hostPerson, String userId) {
        super(api, listener, hostPerson);
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
                hostPerson = syncedPerson;
                listener.onSuccess(null);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
