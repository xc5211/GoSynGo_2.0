package edu.scu.core.callback;

import android.util.Log;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;

import java.util.List;
import java.util.Map;

import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.MemberSelectedTimestamp;
import edu.scu.model.Person;
import edu.scu.model.enumeration.BroadcastEventChannelArgKeyName;
import edu.scu.model.enumeration.PublishEventChannelArgKeyName;

/**
 * Created by chuanxu on 5/21/16.
 */
public class EventChannelMessageLeaderResponder implements AsyncCallback<List<Message>> {

    private Person leader;

    public EventChannelMessageLeaderResponder(Person leader) {
        this.leader = leader;
    }

    @Override
    public void handleResponse(List<Message> messages) {

        String memberId;
        Map<String, String> msgHeader;
        Object msg;
        for (Message message : messages) {
            memberId = message.getPublisherId();
            msgHeader = message.getHeaders();
            msg = message.getData();

            String eventId = msgHeader.get(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName());
            Event targetEvent = leader.getEventAsLeader(eventId);
            EventMemberDetail targetEventMemberDetail = targetEvent.getEventMemberDetail(memberId);

            // Assume only leader receives message from event channel
            // So no receiver check
            if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_STATUS.getKeyName()).equals("true")) {
                int memberStatus = (int) msg;
                targetEventMemberDetail.setStatusMember(memberStatus);
            } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_SELECTED_TIME.getKeyName()).equals("true")) {
                List<MemberSelectedTimestamp> memberSelectedTimestamps = (List<MemberSelectedTimestamp>) msg;
                targetEventMemberDetail.setSelectedTimestamps(memberSelectedTimestamps);
            } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_PROPOSED_TIME.getKeyName()).equals("true")) {
                List<MemberProposedTimestamp> memberProposedTimestamps = (List<MemberProposedTimestamp>) msg;
                targetEventMemberDetail.setProposedTimestamps(memberProposedTimestamps);
            } else if (msgHeader.get(PublishEventChannelArgKeyName.MEMBER_MINS_TO_ARRIVE.getKeyName()).equals("true")) {
                int estimateInMin = (int) msg;
                targetEventMemberDetail.setMinsToArrive(estimateInMin);
            } else {
                assert false;
            }

        }

    }

    @Override
    public void handleFault(BackendlessFault backendlessFault) {
        // TODO:
        Log.i("cxu", backendlessFault.getMessage());
    }

}
