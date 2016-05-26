package edu.scu.gsgapp.activity;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.scu.gsgapp.R;
import edu.scu.model.Event;
import edu.scu.model.enumeration.StatusEvent;

/**
 * Created by Hairong on 5/20/16.
 */
public class EventListViewAdapter extends ArrayAdapter<Event> {

    private final List<Event> events;

    public EventListViewAdapter(Context context, @LayoutRes int resource, List<Event> events) {
        super(context, resource, events);
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

        if (position == 1) {
            rowView = inflater.inflate(R.layout.fragment_event_ready_view_pager_custom_row, parent, false);
            TextView textViewTime = (TextView) rowView.findViewById(R.id.text_view_fragment_event_ready_view_pager_custom_event_row_time);
            textViewTime.setText(event.getTimestamp().toString());

            TextView textViewTitle = (TextView) rowView.findViewById(R.id.text_view_fragment_event_ready_view_pager_custom_row_event_title);
            textViewTitle.setText(event.getTitle());

            TextView textViewStatus = (TextView) rowView.findViewById(R.id.text_view_fragment_event_ready_view_pager_custom_row_event_status);
            String statusString = getStatusString(event);
            textViewStatus.setText(statusString);

        } else if (position == 0) {

            rowView = inflater.inflate(R.layout.fragment_event_not_ready_view_pager_custom_row, parent, false);
            TextView textViewTitle = (TextView) rowView.findViewById(R.id.text_view_fragment_event_not_ready_view_pager_custom_row_event_title);
            textViewTitle.setText(event.getTitle());

            TextView textViewStatus = (TextView) rowView.findViewById(R.id.text_view_fragment_event_not_ready_view_pager_custom_row_event_status);
            String statusString = getStatusString(event);
            textViewStatus.setText(statusString);

        } else {
            assert false;
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
            statusString = "Tentative";
        } else if (event.getStatusEvent() == StatusEvent.Pending.getStatus()) {
            statusString = "Pending";
        } else {
            assert false;
        }
        return statusString;
    }

}
