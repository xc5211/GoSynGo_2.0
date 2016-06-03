package edu.scu.gsgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.adapter.dashboard.events.MemberHorizontalViewAdapter;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;

/**
 * Created by chuanxu on 5/26/16.
 */
public class EventDetailActivity extends GsgBaseActivity {

    // Common
    private Toolbar toolbar;
    private HorizontalGridView memberHorizontalView;

    // Leader not ready
    private EditText locationEditText;
    private EditText noteEditText;
    private Spinner durationSpinner;
    private Spinner reminderSpinner;
    private Button cancelButton;

    // Member not ready
    private TextView locationTextView;
    private TextView noteTextView;
    private TextView durationTextView;
    private TextView reminderTextView;
    private Button deleteButton;

    // Ready
    private TextView titleReadyTextView;
    private TextView locationReadyTextView;
    private TextView noteReadyTextView;
    private TextView durationReadyTextView;
    private TextView timeReadyTextView;
    private TextView reminderReadyTextView;


    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");
        String eventState = intent.getStringExtra("eventState");

        boolean isEventLeader = isEventLeader(eventId);
        String hostRole = isEventLeader ? "leader" : "member";
        event = isEventLeader ? appAction.getHostPerson().getEventAsLeader(eventId) : appAction.getHostPerson().getEventAsMember(eventId);

        String eventDetailProperty = hostRole + eventState;
        switch (eventDetailProperty) {
            case "leaderNotReady":
                setContentView(R.layout.activity_event_detail_not_ready_leader);
                initWidgetable(eventDetailProperty);
                initListener(eventDetailProperty);
                break;
            case "memberNotReady":
                setContentView(R.layout.activity_event_detail_not_ready_member);
                initWidgetable(eventDetailProperty);
                initListener(eventDetailProperty);
                break;
            default:    // "leaderReady" || "memberReady"
                setContentView(R.layout.activity_event_detail_ready);
                initWidgetable(eventDetailProperty);
                initListener(eventDetailProperty);
                break;
        }
    }

    private boolean isEventLeader(String eventId) {
        boolean isEventLeader = appAction.getHostPerson().getEventAsLeader(eventId) != null;
        return isEventLeader;
    }

    private void initWidgetable(String eventDetailProperty) {

        initWidgetableCommon();

        switch (eventDetailProperty) {
            case "leaderNotReady":

                ((TextView) findViewById(R.id.toolbar_event_detail_not_ready_leader_title)).setText(event.getTitle());

                this.locationEditText = (EditText) findViewById(R.id.edit_text_event_detail_not_ready_leader_location);
                this.locationEditText.setText(event.getLocation());

                this.noteEditText = (EditText) findViewById(R.id.edit_text_event_detail_not_ready_leader_note);
                this.noteEditText.setText(event.getNote());

                this.durationSpinner = (Spinner) findViewById(R.id.spinner_event_detail_not_ready_leader_duration);
                Integer[] durations = { 30, 45, 60, 90, 120 };
                ArrayAdapter<Integer> durationSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
                durationSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.durationSpinner.setAdapter(durationSpinnerArrayAdapter);

                this.reminderSpinner = (Spinner) findViewById(R.id.spinner_event_detail_not_ready_leader_reminder);
                Integer[] reminders = { 0, 15, 30, 45, 60 };
                ArrayAdapter<Integer> remindSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reminders);
                remindSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.reminderSpinner.setAdapter(remindSpinnerArrayAdapter);

                this.memberHorizontalView = (HorizontalGridView) findViewById(R.id.event_detail_not_ready_leader_event_member_grid_view);
                List<String> memberList = new ArrayList<>();
                for(EventMemberDetail eventMemberDetail : event.getEventMemberDetail()) {
                    memberList.add(eventMemberDetail.getMember().getFirstName());
                }
                MemberHorizontalViewAdapter adapter = new MemberHorizontalViewAdapter(this, memberList);
                this.memberHorizontalView.setAdapter(adapter);

                this.cancelButton = (Button) findViewById(R.id.button_event_detail_not_ready_leader_cancel);
                break;

            case "memberNotReady":
                ((TextView) findViewById(R.id.toolbar_event_detail_not_ready_member_title)).setText(event.getTitle());

                this.locationTextView = (TextView) findViewById(R.id.text_view_event_detail_not_ready_member_location);
                this.locationTextView.setText(event.getLocation());

                this.noteTextView = (TextView) findViewById(R.id.text_view_event_detail_not_ready_member_note);
                this.noteTextView.setText(event.getNote());

                this.durationTextView = (TextView) findViewById(R.id.text_view_event_detail_not_ready_member_duration);
                this.durationTextView.setText(event.getDurationInMin().toString());

                this.memberHorizontalView = (HorizontalGridView) findViewById(R.id.event_detail_not_ready_member_event_member_grid_view);
                List<String> memberListInMember = new ArrayList<>();
                for(EventMemberDetail eventMemberDetail : event.getEventMemberDetail()) {
                    memberListInMember.add(eventMemberDetail.getMember().getFirstName());
                }
                MemberHorizontalViewAdapter adapterInmember = new MemberHorizontalViewAdapter(this, memberListInMember);
                this.memberHorizontalView.setAdapter(adapterInmember);

                this.deleteButton = (Button) findViewById(R.id.button_event_detail_not_ready_member_delete);

                break;

            default:    // "leaderReady" || "memberReady"
                ((TextView) findViewById(R.id.toolbar_event_detail_ready_title)).setText(event.getTitle());

                this.titleReadyTextView = (TextView) findViewById(R.id.text_view_event_detail_ready_title);
                this.titleReadyTextView.setText(event.getTitle());

                this.locationReadyTextView = (TextView) findViewById(R.id.text_view_event_detail_ready_location);
                this.locationReadyTextView.setText((event.getLocation()));

                this.noteReadyTextView = (TextView) findViewById(R.id.text_view_event_detail_ready_note);
                this.noteReadyTextView.setText(event.getNote());

                this.durationReadyTextView = (TextView) findViewById(R.id.text_view_event_detail_ready_duration);
                this.durationReadyTextView.setText(event.getDurationInMin().toString());

                this.timeReadyTextView = (TextView) findViewById(R.id.text_view_event_detail_ready_time);
                this.timeReadyTextView.setText(event.getTimestamp().toString());

                this.reminderReadyTextView = (TextView) findViewById(R.id.text_view_event_detail_ready_reminder);
                this.reminderReadyTextView.setText(event.getReminderInMin().toString());

                this.memberHorizontalView = (HorizontalGridView) findViewById(R.id.event_detail_ready_event_member_grid_view);
                List<String> memberListInReady = new ArrayList<>();
                for(EventMemberDetail eventMemberDetail : event.getEventMemberDetail()) {
                    memberListInReady.add(eventMemberDetail.getMember().getFirstName());
                }
                MemberHorizontalViewAdapter adapterInReady = new MemberHorizontalViewAdapter(this, memberListInReady);
                this.memberHorizontalView.setAdapter(adapterInReady);

                break;
        }
    }

    private void initWidgetableCommon() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar_event_detail_not_ready_leader);
        //this.toolbar.setTitle(event.getTitle());
    }

    private void initListener(String eventDetailProperty) {

        initListenerCommon();

        switch (eventDetailProperty) {
            case "leaderNotReady":
                super.appAction.cancelEvent(event.getObjectId(),new ActionCallbackListener<Boolean>() {

                    @Override
                    public void onSuccess(Boolean data) {

                        Toast.makeText(context, "Cancel invitation successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventDetailActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case "memberNotReady":

                break;
            default:    // "leaderReady" || "memberReady"

                break;
        }
    }

    private void initListenerCommon() {

    }

}
