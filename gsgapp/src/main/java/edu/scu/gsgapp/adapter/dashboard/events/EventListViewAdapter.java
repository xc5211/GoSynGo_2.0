package edu.scu.gsgapp.adapter.dashboard.events;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.scu.gsgapp.R;
import edu.scu.model.Event;
import edu.scu.model.enumeration.StatusEvent;

/**
 * Created by Hairong on 5/20/16.
 */
public class EventListViewAdapter extends ArrayAdapter<Event> {

    private boolean isReadyEventAdapter;
    private final Map<Event, Boolean> eventLeaderMap;
    private final List<Event> events;

    public EventListViewAdapter(Context context, @LayoutRes int resource, boolean isReadyEventAdapter, List<Event> events, Map<Event, Boolean> eventLeaderMap) {
        super(context, resource, events);
        this.isReadyEventAdapter = isReadyEventAdapter;
        this.eventLeaderMap = eventLeaderMap;
        this.events = events;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = setEventRow(inflater, view, position, parent);
        }
        return view;
    }

    private View setEventRow(LayoutInflater inflater, View rowView, int position, final ViewGroup parent) {

        final Event event = events.get(position);

        if (isReadyEventAdapter) {

            rowView = inflater.inflate(R.layout.fragment_event_ready_view_pager_custom_row, parent, false);

            TextView titleTextView = (TextView) rowView.findViewById(R.id.text_view_fragment_event_ready_view_pager_custom_row_event_title);
            titleTextView.setText(event.getTitle());

            TextView timeTextView = (TextView) rowView.findViewById(R.id.text_view_fragment_event_ready_view_pager_custom_row_event_time);
            timeTextView.setText(event.getTimestamp().toString());

            // Check if host is leader for "current" event
            if (this.eventLeaderMap.get(event)) {
                ImageView leaderImageView = (ImageView) rowView.findViewById(R.id.text_view_fragment_event_ready_view_pager_custom_row_event_is_leader);
                leaderImageView.setImageResource(R.drawable.icon_king_leader);
            }
        } else {

            rowView = inflater.inflate(R.layout.fragment_event_not_ready_view_pager_custom_row, parent, false);

            TextView textViewTitle = (TextView) rowView.findViewById(R.id.text_view_fragment_event_not_ready_view_pager_custom_row_event_title);
            textViewTitle.setText(event.getTitle());

            TextView textViewStatus = (TextView) rowView.findViewById(R.id.text_view_fragment_event_not_ready_view_pager_custom_row_event_status);
            String statusString = getStatusString(event);
            textViewStatus.setText(statusString);

            // Check if host is leader for "current" event
            if (this.eventLeaderMap.get(event)) {
                ImageView leaderImageView = (ImageView) rowView.findViewById(R.id.text_view_fragment_event_not_ready_view_pager_custom_row_event_is_leader);
                leaderImageView.setImageResource(R.drawable.icon_king_leader);
            }
        }
        return rowView;
    }

    private String getStatusString(Event event) {

        String statusString = null;
        if (event.getStatusEvent() == StatusEvent.Ready.getStatus()) {
            statusString = "Ready";
        } else if (event.getStatusEvent() == StatusEvent.Cancelled.getStatus()) {
            statusString = "Cancelled";
        } else if (event.getStatusEvent() == StatusEvent.Past.getStatus()) {
            statusString = "Past";
        } else if (event.getStatusEvent() == StatusEvent.Tentative.getStatus()) {
            statusString = "Draft";
        } else if (event.getStatusEvent() == StatusEvent.Pending.getStatus()) {
            statusString = "Pending";
        } else {
            assert false;
        }
        return statusString;
    }

}
