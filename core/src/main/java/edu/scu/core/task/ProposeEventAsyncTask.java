package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventAsyncTask extends BaseAsyncTask {

    private Person person;

    public ProposeEventAsyncTask(Api api, ActionCallbackListener listener, Handler handler, Person person) {
        super(api, listener, handler);
        this.person = person;
    }

    @Override
    protected ApiResponse<Person> doInBackground(Object... params) {
        return api.proposeEvent(person);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Person updatedPerson = (Person) response.getObj();
                android.os.Message message = new android.os.Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Person.SERIALIZE_KEY, updatedPerson);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
