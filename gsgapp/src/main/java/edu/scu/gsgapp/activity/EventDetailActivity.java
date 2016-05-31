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

import java.util.ArrayList;
import java.util.List;

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

    // Ready


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
                Integer[] durations = { 30, 45, 60, 90, 120};
                ArrayAdapter<Integer> durationSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
                durationSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.durationSpinner.setAdapter(durationSpinnerArrayAdapter);

                this.reminderSpinner = (Spinner) findViewById(R.id.spinner_event_detail_not_ready_leader_reminder);
                Integer[] reminders = { 0, 15, 30, 45, 60 };
                ArrayAdapter<Integer> remindSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
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

                break;

            default:    // "leaderReady" || "memberReady"

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
