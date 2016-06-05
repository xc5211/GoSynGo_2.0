package edu.scu.gsgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.adapter.dashboard.events.MemberHorizontalViewAdapter;
import edu.scu.gsgapp.adapter.propose.ProposeEventAddMemberAdapter;
import edu.scu.gsgapp.fragment.BaseWeekViewFragment;
import edu.scu.gsgapp.fragment.FragmentDateCommunicator;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;

/**
 * Created by chuanxu on 5/26/16.
 */
public class EventDetailActivity extends GsgBaseActivity implements FragmentDateCommunicator {

    // Common
    private Toolbar toolbar;

    // Leader not ready
    private EditText locationEditText;
    private EditText noteEditText;
    private Spinner durationSpinner;
    private Spinner reminderSpinner;
    private HorizontalGridView memberHorizontalViewForLeader;
    private Button nextButton;
    private Button cancelEventButton;

    // Member not ready
    private TextView locationTextView;
    private TextView noteTextView;
    private TextView durationTextView;
    private TextView reminderTextView;
    private HorizontalGridView memberHorizontalViewForMember;
    private Button leaveButton;

    // Ready
    private TextView titleReadyTextView;
    private TextView locationReadyTextView;
    private TextView noteReadyTextView;
    private TextView durationReadyTextView;
    private TextView timeReadyTextView;
    private TextView reminderReadyTextView;
    private GridView eventRedyGridview;

    private Event event;
    private List<Date> leaderProposedTimestamps;
    private Date eventTimestamp;
    
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
                BaseWeekViewFragment baseWeekViewFragmentLeader = new BaseWeekViewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.event_detail_not_ready_leader_day_calendar_container, baseWeekViewFragmentLeader).commit();
                break;
            case "memberNotReady":
                setContentView(R.layout.activity_event_detail_not_ready_member);
                initWidgetable(eventDetailProperty);
                initListener(eventDetailProperty);
                BaseWeekViewFragment baseWeekViewFragmentMember = new BaseWeekViewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.event_detail_not_ready_member_day_calendar_container, baseWeekViewFragmentMember).commit();
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

                this.memberHorizontalViewForLeader = (HorizontalGridView) findViewById(R.id.event_detail_not_ready_leader_event_member_grid_view);
                List<String> memberList = new ArrayList<>();
                for(EventMemberDetail eventMemberDetail : event.getEventMemberDetail()) {
                    memberList.add(eventMemberDetail.getMember().getFirstName());
                }
                MemberHorizontalViewAdapter adapter = new MemberHorizontalViewAdapter(this, memberList);
                this.memberHorizontalViewForLeader.setAdapter(adapter);

                this.nextButton = (Button) findViewById(R.id.button_event_detail_not_ready_leader_next);
                this.nextButton.setEnabled(false);
                if (event.getEventLeaderDetail().getProposedTimestamps().isEmpty()) {
                    this.nextButton.setText("Start time vote");
                } else {
                    this.nextButton.setText("Notify final time");
                }

                this.cancelEventButton = (Button) findViewById(R.id.button_event_detail_not_ready_leader_cancel);
                break;

            case "memberNotReady":
                ((TextView) findViewById(R.id.toolbar_event_detail_not_ready_member_title)).setText(event.getTitle());

                this.locationTextView = (TextView) findViewById(R.id.text_view_event_detail_not_ready_member_location);
                this.locationTextView.setText(event.getLocation());

                this.noteTextView = (TextView) findViewById(R.id.text_view_event_detail_not_ready_member_note);
                this.noteTextView.setText(event.getNote());

                this.durationTextView = (TextView) findViewById(R.id.text_view_event_detail_not_ready_member_duration);
                this.durationTextView.setText(event.getDurationInMin().toString());

                this.memberHorizontalViewForMember = (HorizontalGridView) findViewById(R.id.event_detail_not_ready_member_event_member_grid_view);
                List<String> memberListInMember = new ArrayList<>();
                for(EventMemberDetail eventMemberDetail : event.getEventMemberDetail()) {
                    memberListInMember.add(eventMemberDetail.getMember().getFirstName());
                }
                MemberHorizontalViewAdapter adapterInmember = new MemberHorizontalViewAdapter(this, memberListInMember);
                this.memberHorizontalViewForMember.setAdapter(adapterInmember);

                this.leaveButton = (Button) findViewById(R.id.button_event_detail_not_ready_member_leave);
                break;

            default:    // "leaderReady" || "memberReady"
                ((TextView) findViewById(R.id.toolbar_event_detail_ready_title)).setText(event.getTitle());

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

                this.eventRedyGridview = (GridView) findViewById(R.id.gridview_event_detail_ready);
                List<String> memberListInReady = new ArrayList<>();
                for(EventMemberDetail eventMemberDetail : event.getEventMemberDetail()) {
                    memberListInReady.add(eventMemberDetail.getMember().getFirstName());
                }
                ProposeEventAddMemberAdapter adapterInReady = new ProposeEventAddMemberAdapter(this, memberListInReady);
                this.eventRedyGridview.setAdapter(adapterInReady);
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

                this.nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (event.getEventLeaderDetail().getProposedTimestamps().isEmpty()) {
                            proposeEventTimestampsAsLeader();
                        } else {
                            initiateEvent();
                        }
                    }
                });

                this.cancelEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelEvent();
                    }
                });

                break;
            case "memberNotReady":

                this.leaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        memberLeaveEvent();
                    }
                });
                break;
            default:    // "leaderReady" || "memberReady"

                break;
        }
    }

    private void initListenerCommon() {

    }

    private void proposeEventTimestampsAsLeader() {

        List<String> proposedEventTimestamps = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for(Date date: leaderProposedTimestamps) {
            proposedEventTimestamps.add(sdf.format(date));
        }

        appAction.proposeEventTimestampsAsLeader(event.getObjectId(), proposedEventTimestamps, new ActionCallbackListener<EventLeaderDetail>() {
            @Override
            public void onSuccess(EventLeaderDetail data) {
                Toast.makeText(getApplicationContext(), "Propose time success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initiateEvent() {

        super.appAction.initiateEvent(event.getObjectId(), eventTimestamp, new ActionCallbackListener<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                Toast.makeText(getApplicationContext(), "Initiate event success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelEvent() {

        super.appAction.cancelEvent(event.getObjectId(),new ActionCallbackListener<Boolean>() {

            @Override
            public void onSuccess(Boolean data) {

                Toast.makeText(context, "Event cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EventDetailActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void memberLeaveEvent() {
        // TODO[later]
    }

    @Override
    public void updateSelectedDates(List<Date> selectedDates) {

        if (event.getEventLeaderDetail().getProposedTimestamps().isEmpty()) {
            // Vote time
            this.leaderProposedTimestamps = selectedDates;
            if (!selectedDates.isEmpty()) {
                this.nextButton.setEnabled(true);
            } else {
                this.nextButton.setEnabled(false);
            }
        } else {
            // Final time
            if (!selectedDates.isEmpty() && selectedDates.size() == 1) {
                this.eventTimestamp = selectedDates.get(0);
                this.nextButton.setEnabled(true);
            } else {
                this.nextButton.setEnabled(false);
            }
        }

        BaseWeekViewFragment baseWeekViewFragmentLeader = new BaseWeekViewFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> encodedDates = encodeDate(leaderProposedTimestamps);
        bundle.putStringArrayList("encodedDates", encodedDates);
        baseWeekViewFragmentLeader.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.event_detail_not_ready_leader_day_calendar_container, baseWeekViewFragmentLeader).commit();
    }

    private ArrayList<String> encodeDate(List<Date> leaderProposedTimestamps) {
        ArrayList<String> encodedDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Date date: leaderProposedTimestamps) {
            encodedDates.add(sdf.format(date));
        }
        return encodedDates;
    }
}
