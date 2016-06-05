package edu.scu.core.callback;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by chuanxu on 5/21/16.
 */
public class EventChannelMessageMemberResponder implements AsyncCallback<List<Message>> {

    private static String selector;
    private String memberId;

    public EventChannelMessageMemberResponder(String memberPersonObjectId) {
        selector = "receiverId" + memberPersonObjectId.replace("-", "");
        this.memberId = memberPersonObjectId;
    }

    @Override
    public void handleResponse(List<Message> messages) {

        Map<String, String> msgHeader;
        Object msg;
        for (Message message : messages) {

            msgHeader = message.getHeaders();
            msg = message.getData();
            String receiverIdValue = msgHeader.get(selector);
            if (receiverIdValue == null) {
                continue;
            }
            assert receiverIdValue.equals("true");

            // Handle server or member message
            String from = msgHeader.get("sentFrom");
            if (from.equals("server")) {
                handleServerMessage(msgHeader, msg);
            } else if (from.equals("leader")) {
                String leaderId = message.getPublisherId();
                handleLeaderMessage(leaderId, msgHeader, msg);
            } else if (from.equals("member")) {
                String memberId = message.getPublisherId();
                handleMemberMessage(memberId, msgHeader, msg);
            } else {
                assert false;
            }
        }
    }

    private void handleServerMessage(Map<String, String> msgHeader, Object msg) {

    }

    private void handleLeaderMessage(String leaderId, Map<String, String> msgHeader, Object msg) {

    }

    private void handleMemberMessage(String memberId, Map<String, String> msgHeader, Object msg) {

    }

    @Override
    public void handleFault(BackendlessFault backendlessFault) {

    }

}
