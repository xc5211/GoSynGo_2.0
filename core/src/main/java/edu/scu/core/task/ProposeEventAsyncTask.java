package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventAsyncTask extends BaseAsyncTask {

    private Person person;
    private AsyncCallback<List<Message>> channelMsgResponderForLeader;

    public ProposeEventAsyncTask(Api api, ActionCallbackListener listener, AsyncCallback<List<Message>> channelMsgResponderForLeader, Person person, Handler handler) {
        super(api, listener, handler);
        this.person = person;
        this.channelMsgResponderForLeader = channelMsgResponderForLeader;
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
                // TODO: check channelName
                String channelName = updatedPerson.getEventsAsLeader().get(0).getObjectId();

//                api.registerEventChannelMessaging(channelName);
//                api.subscribeEventChannelAsLeader(channelName, leaderId, channelMsgResponderForLeader);

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
