package edu.scu.gsgapp.messaging;

import android.content.Context;
import android.content.Intent;

import com.backendless.push.BackendlessBroadcastReceiver;

import edu.scu.core.AppAction;
import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.activity.DashboardActivity;
import edu.scu.model.Person;
import edu.scu.model.enumeration.BroadcastEventChannelArgKeyName;
import edu.scu.model.enumeration.BroadcastEventChannelArgValueName;
import edu.scu.model.enumeration.EventManagementState;

/**
 * Created by chuanxu on 5/8/16.
 */

/**
 * Push Notification (all with target Event object attached as message):
 *
 *  - Leader
 *      * Propose Time Notification
 *          i. Selector: "leaderId='abc-agea'", "eventId='abc-egdd'", "msg='propose'"
 *      * Select Final Time Notification
 *          i. Selector: "leaderId='abc-agea'", "eventId='abc-egdd'", "msg='select'"
 *
 *      * Event Reminder (Future)
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(leaderId)='true'", "eventManagementState='3'"
 *      * Checkin/Estimate (Future)
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(leaderId)='true'", "eventManagementState='4'"
 *
 *  - Member
 *      * Invitation
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(memberId)='true'", "eventManagementState='0'"
 *          -> Subscribe/Not Subscribe to Channel
 *      * Vote Reminder
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(memberId)='true'", "eventManagementState='1'"
 *      * Final Time Notification
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(memberId)='true'", "eventManagementState='2'"
 *      * Event Reminder (Future)
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(memberId)='true'", "eventManagementState='3'"
 *      * Checkin/Estimate (Future)
 *          i. Selector: "leaderId='abc-agea'", "'abc-agdg'(memberId)='true'", "eventManagementState='4'"
 *
 *
 *
 * Procedure Pseudo-code:
 *
 *  Get/check eventManagementState value
 *      if null -> Server triggered
 *          if leaderId matches host Person object id
 *              accept message
 *              if msg for propose
 *                  handle propose event times notification
 *              else if msg for select
 *                  handle select final time notification
 *          else
 *              reject message
 *      else    -> Leader triggered
 *          if headers contain host Person object id as "{hostPersonId}=true"
 *              accept message
 *              if host Person object id matches leaderId   -> As Leader
 *                  if eventManagementState == 3
 *                      handle event reminder notification
 *                  else if eventManagementState == 4
 *                      handle event checkin/estimate notification
 *              else                                        -> As member
 *                  if eventManagementState == 0
 *                      handle event invitation notification
 *                  else if eventManagementState == 1
 *                      handle event vote reminder
 *                  else if eventManagementState == 2
 *                      handle event final time notification
 *                  else if eventManagementState == 3
 *                      handle event reminder notification
 *                  else if eventManagementState == 4
 *                      handle event checkin/estimate notification
 *          else
 *              reject message
 *
 */
public class EventStateServerPushReceiver extends BackendlessBroadcastReceiver {

    private Context context;
    private GsgApplication gsgApplication;
    private AppAction appAction;
    private Person hostPerson;
    private String hostPersonId;

    @Override
    public boolean onMessage(Context context, Intent intent) {

        this.context = context;
        this.gsgApplication = (GsgApplication) context.getApplicationContext();
        this.appAction = gsgApplication.getAppAction();
        this.hostPerson = appAction.getHostPerson();
        this.hostPersonId = hostPerson.getObjectId();

        boolean receiveMessage = false;
        String eventManagementState = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_MANAGEMENT_STATE.getKeyName());
        if (eventManagementState == null) {
            receiveMessage = handleServerTriggeredNotification(intent);
        } else {
            receiveMessage = handleLeaderTriggeredNotification(intent, eventManagementState);
        }

        if (!receiveMessage) {
            // TODO: Reject message by returning false?
            return false;
        }
        return super.onMessage(context, intent);
    }

    private boolean handleLeaderTriggeredNotification(Intent intent, String eventManagementState) {
        boolean isPushToHostPerson = intent.getStringExtra(hostPersonId).equals("true");
        if (!isPushToHostPerson) {
            return false;
        }

        String leaderId = intent.getStringExtra(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName());
        boolean isHostPersonLeader = hostPersonId.equals(leaderId);
        if (isHostPersonLeader) {
            handleLeaderTriggeredNotificationAsLeader(intent, eventManagementState);
        } else {
            handleLeaderTriggeredNotificationAsMember(intent, eventManagementState);
        }
        return true;
    }

    private void handleLeaderTriggeredNotificationAsLeader(Intent intent, String eventManagementState) {

        String eventId = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName());
        Intent nextIntent = null;
        if (eventManagementState.equals(EventManagementState.EVENT_REMINDER.getStatus())) {
            // TODO: handle leader receives event reminder
            nextIntent = new Intent(context, DashboardActivity.class);
        } else if (eventManagementState.equals(EventManagementState.CHECKIN_OR_ESTIMATE.getStatus())) {
            // TODO: handle leader receives checkin notification
            nextIntent = new Intent(context, DashboardActivity.class);
        } else {
            assert false;
        }
        context.startActivity(nextIntent);
    }

    private void handleLeaderTriggeredNotificationAsMember(Intent intent, String eventManagementState) {

        String eventId = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName());
        Intent nextIntent = null;
        if (eventManagementState.equals(EventManagementState.SEND_INVITATION.getStatus())) {
            String eventTitle = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_TITLE.getKeyName());
            String eventNote = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_NOTE.getKeyName());
            String eventLocation = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_LOCATION.getKeyName());
            String eventLeader = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_LEADER.getKeyName());

            appAction.startMemberInvitationTimer(appAction, eventId);
            nextIntent = new Intent(context, DashboardActivity.class);
            nextIntent.putExtra(BroadcastEventChannelArgKeyName.EVENT_TITLE.getKeyName(), eventTitle);
            nextIntent.putExtra(BroadcastEventChannelArgKeyName.EVENT_NOTE.getKeyName(), eventNote);
            nextIntent.putExtra(BroadcastEventChannelArgKeyName.EVENT_LOCATION.getKeyName(), eventLocation);
            nextIntent.putExtra(BroadcastEventChannelArgKeyName.EVENT_LEADER.getKeyName(), eventLeader);
        } else if (eventManagementState.equals(EventManagementState.REMIND_TO_VOTE.getStatus())) {
            // TODO: handle member receives vote notification
            nextIntent = new Intent(context, DashboardActivity.class);
        } else if (eventManagementState.equals(EventManagementState.NOTIFY_FINAL_TIME.getStatus())) {
            // TODO: handle member receives event final time
            nextIntent = new Intent(context, DashboardActivity.class);
        } else if (eventManagementState.equals(EventManagementState.EVENT_REMINDER.getStatus())) {
            // TODO: handle member receives event reminder
            nextIntent = new Intent(context, DashboardActivity.class);
        } else if (eventManagementState.equals(EventManagementState.CHECKIN_OR_ESTIMATE.getStatus())) {
            // TODO: handle member receives checkin notification
            nextIntent = new Intent(context, DashboardActivity.class);
        } else {
            assert false;
        }
        context.startActivity(nextIntent);
    }

    private boolean handleServerTriggeredNotification(Intent intent) {
        String leaderId = intent.getStringExtra(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName());
        if (!hostPersonId.equals(leaderId)) {
            return false;
        }

        String message = intent.getStringExtra(BroadcastEventChannelArgKeyName.MESSAGE.getKeyName());
        String eventId = intent.getStringExtra(BroadcastEventChannelArgKeyName.EVENT_ID.getKeyName());
        Intent nextIntent = null;
        if (message.equals(BroadcastEventChannelArgValueName.PROPOSE_EVENT_TIME_MESSAGE.getValueName())) {
            // TODO: handle leader proposes event times notification
            nextIntent = new Intent(context, DashboardActivity.class);
        } else if (message.equals(BroadcastEventChannelArgValueName.SELECT_EVENT_FINAL_TIME_MESSAGE.getValueName())) {
            // TODO: handle leader selects event final time notification
            nextIntent = new Intent(context, DashboardActivity.class);
        } else {
            assert false;
        }
        context.startActivity(nextIntent);
        return true;
    }

}
