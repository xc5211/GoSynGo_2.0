package edu.scu.core.callback;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;

import java.util.List;

/**
 * Created by chuanxu on 5/21/16.
 */
public class EventChannelMessageMemberResponder implements AsyncCallback<List<Message>> {

    @Override
    public void handleResponse(List<Message> messages) {

    }

    @Override
    public void handleFault(BackendlessFault backendlessFault) {

    }

}
