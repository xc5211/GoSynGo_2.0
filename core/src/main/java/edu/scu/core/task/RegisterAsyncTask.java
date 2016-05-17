package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class RegisterAsyncTask extends BaseAsyncTask {

    private String userEmail;
    private String password;
    private String firstName;
    private String lastName;

    public RegisterAsyncTask(Api api, ActionCallbackListener listener, Handler handler, String userEmail, String password, String firstName, String lastName) {
        super(api, listener, handler);
        this.userEmail = userEmail;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    protected ApiResponse<Person> doInBackground(Object... params) {
        return api.register(userEmail, password, firstName, lastName);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Person hostPerson = (Person) response.getObj();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Person.SERIALIZE_KEY, hostPerson);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
